package com.vincents.taskscheduler.service.scheduling;

import com.google.api.services.classroom.Classroom;
import com.google.api.services.classroom.model.Course;
import com.google.api.services.classroom.model.CourseWork;
import com.google.api.services.classroom.model.TimeOfDay;
import com.vincents.taskscheduler.model.PlannedTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.time.temporal.ChronoUnit;

@Service
public class PlannedTaskGenerator {
    private final GoogleClassroomService classroomService;
    private final AssignmentRetriver assignmentRetriver;

    private static final Logger log = LoggerFactory.getLogger(PlannedTaskGenerator.class);

    public PlannedTaskGenerator(GoogleClassroomService classroomService, AssignmentRetriver assignmentRetriver) {
        this.classroomService = classroomService;
        this.assignmentRetriver = assignmentRetriver;
    }

    public Map<String, List<PlannedTask>> generateAllTasks(){
        List<Course> courses = classroomService.getCourseList();
        if (courses == null || courses.isEmpty()) return Collections.emptyMap();
        Map<String, List<PlannedTask>> plannedTasks = new HashMap<>();

        for(Course course : courses){
            List<PlannedTask> plannedTaskList = generateTasks(course);
            if(plannedTaskList.isEmpty()) continue;
            plannedTasks.put(course.getName(), plannedTaskList);
        }

        return plannedTasks;

    }

    public List<PlannedTask> generateTasks(/*Map<String, Integer> userEstimates,*/ Course course){
        List<PlannedTask> plannedTasks = new ArrayList<>();

        try{
            Classroom service = classroomService.getClassroomService();

            if(service == null){
                System.out.println("failed to initialize Classroom service");
            }

            List<CourseWork> assignments = assignmentRetriver.fetchActiveAssignments(course.getId());

            for (CourseWork assignment: assignments) {
//                String assignmentID = assignment.getId();
//                Integer userEstimate = userEstimates.get(assignmentID);

                String title = assignment.getTitle();
                String description = assignment.getDescription();
                String alternateLink = assignment.getAlternateLink();

                LocalDate today = LocalDate.now();
                LocalDate dueDate = assignmentRetriver.getDueDate(assignment);
                if (dueDate == null) continue;


                long daysUntilDueDate = ChronoUnit.DAYS.between(today, dueDate);
                if (daysUntilDueDate <= 0) daysUntilDueDate = 1;

                String formattedDueDate = assignmentRetriver.getFormattedDueDate(dueDate);
                TimeEstimatorService timeEstimatorService = new TimeEstimatorService();

                int totalEstimatedMinutes;
                totalEstimatedMinutes = timeEstimatorService.estimateFromKeyWord(title, description);

                List<Long> chunks = timeEstimatorService.divideAcrossDays(totalEstimatedMinutes, daysUntilDueDate);



                PlannedTask plannedTask = new PlannedTask(
                        title,
                        description,
                        dueDate,
                        formattedDueDate,
                        totalEstimatedMinutes,
                        chunks,
                        alternateLink
                );
                plannedTasks.add(plannedTask);
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        plannedTasks.sort(Comparator.comparing(PlannedTask::getDueDate));

//        for(int i = 0; i < plannedTasks.size(); i++){
//            if(plannedTasks.get(i).getDueDate().isAfter(plannedTasks.get(i+1).getDueDate())){
//               PlannedTask temp = plannedTasks.get(i);
//               plannedTasks.set(i,plannedTasks.get(i+1));
//               plannedTasks.set(i+1,temp);
//            }
//        }

        return plannedTasks;

    }
}
