package com.vincents.taskscheduler.service.scheduling;
import com.google.api.services.classroom.Classroom;
import com.google.api.services.classroom.model.Course;
import com.google.api.services.classroom.model.CourseWork;
import com.google.api.services.classroom.model.TimeOfDay;
import com.vincents.taskscheduler.model.PlannedTask;
import org.springframework.stereotype.Service;
import com.google.api.services.classroom.model.Date;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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

    //TODO: delete this method???
//    public String listCourseAssignments(){
//        StringBuilder returnString = new StringBuilder();
//        returnString.append("<!DOCTYPE html>");
//        returnString.append("<html lang='en'>");
//        returnString.append("<head>");
//        returnString.append("<meta charset='UTF-8'>");
//        returnString.append("<title>Assignments</title>");
//        returnString.append("<link rel='stylesheet' href='/style.css'>");
//        returnString.append("</head>");
//        returnString.append("<body>");
//        returnString.append("<h1>Assignments</h1>");
//
//        Classroom service = classroomService.getClassroomService();
//        if(service == null){
//            return "failed to initialize classroom service";
//        }
//        List<Course> courses = classroomService.getCourseList();
//
//        for (Course course : courses) {
//            String courseName = course.getName();
//            String courseId = course.getId();
//            try {
//                PlannedTaskGenerator plannedTaskGenerator = new PlannedTaskGenerator(classroomService, this);
////                List<CourseWork> activeCourseWorkList = fetchActiveAssignments(course);
////                if(activeCourseWorkList == null || activeCourseWorkList.isEmpty()){
////                    continue;
////                }
//
//                List<PlannedTask> plannedTasks = plannedTaskGenerator.generateTasks(course);
//
//                List<String> assignmentHtmlList = new ArrayList<>();
//
//                for (PlannedTask plannedTask : plannedTasks) {
//
//
//
//                    LocalDate dueDate = plannedTask.getDueDate();
//                    String formattedDate = getFormattedDueDate(dueDate);
//
//                    String link = plannedTask.getAssignment().getAlternateLink();
//
//                    String assignmentHtml = "<div class='assignment-card'>" +
//                            "<a href='" + link + "' target='_blank'>" +
//                            "<h3>" + plannedTask.getTitle() + "</h3>" +
//                            "<p>" + (plannedTask.getDescription() != null ? plannedTask.getDescription() : "None") + "</p>" +
//                            "<p>" + formattedDate + "</p>" +
//                            "<p>" + plannedTask.getEstimatedMinutes() + "</p>" +
//                            "<p>" + plannedTask.getChunks() + "</p>" +
//                            "</a>" +
//                            "</div>";
//                    assignmentHtmlList.add(assignmentHtml);
//
//                }
//                if (assignmentHtmlList.isEmpty()) continue;
//
//                returnString.append("<div class='course-box'><div class='course'>")
//                        .append("<h2>").append(courseName).append("</h2>");
//                for (String assignment : assignmentHtmlList) {
//                    returnString.append(assignment);
//                }
//                returnString.append("</div></div>");
//
//            } catch (Exception e) {
//                returnString.append(String.format("Error fetching assignments for course %s: %s<br>", courseId, e.getMessage()));
//            }
//        }
//        returnString.append("</body>");
//        returnString.append("</html>");
//        return returnString.toString();
//    }

    public List<String> getCourseIDs(){
        List<Course> courses = classroomService.getCourseList();
        List<String> courseIDs = new ArrayList<>();
        for(Course course : courses){
            courseIDs.add(course.getId());
        }
        return courseIDs;
    }

    public List<CourseWork> fetchActiveAssignments(String courseId) {
        Classroom service = classroomService.getClassroomService();
        if(service == null){
           System.out.println("service is null");
        }
        List<CourseWork> assignments = new ArrayList<>();


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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMM d, yyyy", Locale.ENGLISH);
        String formattedDate = dueDate.format(formatter);
        return formattedDate;
    }

    public LocalDate getDueDate(CourseWork courseWork){
        Date due = courseWork.getDueDate();
        TimeOfDay dueTime = courseWork.getDueTime();
        if (due == null) return null;

        // Create the date-time in UTC first
        LocalDateTime dateTime = LocalDateTime.of(
                due.getYear(),
                due.getMonth(),
                due.getDay(),
                dueTime != null ? dueTime.getHours() : 23,
                dueTime != null ? dueTime.getMinutes() : 59
        );

        // Convert from UTC to Pacific time and extract just the date
        ZonedDateTime utcDateTime = dateTime.atZone(ZoneId.of("UTC"));
        ZonedDateTime pacificDateTime = utcDateTime.withZoneSameInstant(ZoneId.of("America/Los_Angeles"));

        return pacificDateTime.toLocalDate();
    }




}
