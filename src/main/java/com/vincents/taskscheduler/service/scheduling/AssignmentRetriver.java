package com.vincents.taskscheduler.service;
import com.google.api.services.classroom.Classroom;
import com.google.api.services.classroom.model.Course;
import com.google.api.services.classroom.model.CourseWork;
import com.vincents.taskscheduler.GoogleClassroomService;
import com.vincents.taskscheduler.PlannedTask;
import com.vincents.taskscheduler.PlannedTaskGenerator;
import org.springframework.stereotype.Service;
import com.google.api.services.classroom.model.Date;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class AssignmentRetriver {
    private final GoogleClassroomService classroomService;

    public AssignmentRetriver(GoogleClassroomService classroomService) {
        this.classroomService = classroomService;
    }

    public String listCourseAssignments(){
        StringBuilder returnString = new StringBuilder();
        returnString.append("<!DOCTYPE html>");
        returnString.append("<html lang='en'>");
        returnString.append("<head>");
        returnString.append("<meta charset='UTF-8'>");
        returnString.append("<title>Assignments</title>");
        returnString.append("<link rel='stylesheet' href='/style.css'>");
        returnString.append("</head>");
        returnString.append("<body>");
        returnString.append("<h1>Assignments</h1>");

        Classroom service = classroomService.getClassroomService();
        if(service == null){
            return "failed to initialize classroom service";
        }
        List<Course> courses = classroomService.getCourseList();

        /*
        I think I can use this class to display all of the planned task information
        I have modifies the plannedtaskgenerator class so that it takes in a course name so
        I can still easily organize tasks based on the course name
        I think I can declare the plannedtaskgenerator class where it is now
        and then use the generatetasks method which now also takes in the course name to
        make a list of all of the courses and then I can loop through that list and get the date info ect that I need
        One question I do have is how I can fill in the conrtructor for the planned task generator since it taked in an object of the assignment retervier class which Is where I am declaring it



         */

        for (Course course : courses) {
            String courseName = course.getName();
            String courseId = course.getId();
            try {
                PlannedTaskGenerator plannedTaskGenerator = new PlannedTaskGenerator(classroomService, this);
//                List<CourseWork> activeCourseWorkList = fetchActiveAssignments(course);
//                if(activeCourseWorkList == null || activeCourseWorkList.isEmpty()){
//                    continue;
//                }

                List<PlannedTask> plannedTasks = plannedTaskGenerator.generateTasks(/*Collections.emptyMap(), */ course);

                List<String> assignmentHtmlList = new ArrayList<>();

                for (PlannedTask plannedTask : plannedTasks) {



                    LocalDate dueDate = plannedTask.getDueDate();
                    String formattedDate = getFormattedDueDate(dueDate);

                    String link = plannedTask.getAssignment().getAlternateLink();

                    String assignmentHtml = "<div class='assignment-card'>" +
                            "<a href='" + link + "' target='_blank'>" +
                            "<h3>" + plannedTask.getTitle() + "</h3>" +
                            "<p>" + (plannedTask.getDescription() != null ? plannedTask.getDescription() : "None") + "</p>" +
                            "<p>" + formattedDate + "</p>" +
                            "<p>" + plannedTask.getEstimatedMinutes() + "</p>" +
                            "<p>" + plannedTask.getChunks() + "</p>" +
                            "</a>" +
                            "</div>";
                    assignmentHtmlList.add(assignmentHtml);

                }
                if (assignmentHtmlList.isEmpty()) continue;

                returnString.append("<div class='course-box'><div class='course'>")
                        .append("<h2>").append(courseName).append("</h2>");
                for (String assignment : assignmentHtmlList) {
                    returnString.append(assignment);
                }
                returnString.append("</div></div>");

            } catch (Exception e) {
                returnString.append(String.format("Error fetching assignments for course %s: %s<br>", courseId, e.getMessage()));
            }
        }
        returnString.append("</body>");
        returnString.append("</html>");
        return returnString.toString();
    }

    public List<String> getCourseIDs(){
        List<Course> courses = classroomService.getCourseList();
        List<String> courseIDs = new ArrayList<>();
        for(Course course : courses){
            courseIDs.add(course.getId());
        }
        return courseIDs;
    }

    public List<CourseWork> fetchActiveAssignments(Course course) {
        Classroom service = classroomService.getClassroomService();
        if(service == null){
           System.out.println("service is null");
        }
        List<CourseWork> assignments = new ArrayList<>();

            String courseId = course.getId();
            try {
                List<CourseWork> courseWorkList =
                        service.courses()
                                .courseWork()
                                .list(courseId)
                                .execute()
                                .getCourseWork();
                if (courseWorkList == null) return assignments;

                for (CourseWork courseWork : courseWorkList) {
                    LocalDate dueDate = getDueDate(courseWork);
                    if(dueDate == null){
                        continue;
                    }
                    if (!dueDate.isAfter(LocalDate.now().minusDays(1))) {
                        continue;
                    }
                    assignments.add(courseWork);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        return assignments;
        }


    public String getFormattedDueDate(LocalDate dueDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMM d", Locale.ENGLISH);
        String formattedDate = dueDate.format(formatter);
        return formattedDate;
    }

    public LocalDate getDueDate(CourseWork courseWork){
        Date due = courseWork.getDueDate();
        if (due == null) return null;

        return LocalDate.of(due.getYear(), due.getMonth(), due.getDay());

    }




}
