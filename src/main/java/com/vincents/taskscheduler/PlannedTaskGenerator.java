package com.vincents.taskscheduler;

import com.google.api.services.classroom.Classroom;
import com.google.api.services.classroom.model.Course;
import com.google.api.services.classroom.model.CourseWork;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.time.temporal.ChronoUnit;
import java.util.Map;


public class PlannedTaskGenerator{
    private final GoogleClassroomService classroomService;
    private final AssignmentRetriver assignmentRetriver;

    public PlannedTaskGenerator(GoogleClassroomService classroomService, AssignmentRetriver assignmentRetriver) {
        this.classroomService = classroomService;
        this.assignmentRetriver = assignmentRetriver;
    }

    public List<PlannedTask> generateTasks(/*Map<String, Integer> userEstimates,*/ Course course){
        List<PlannedTask> plannedTasks = new ArrayList<>();

        try{
            Classroom service = classroomService.getClassroomService();

            if(service == null){
                System.out.println("failed to initialize Classroom service");
            }

            List<CourseWork> assignments = assignmentRetriver.fetchActiveAssignments(course);

            for (CourseWork assignment: assignments) {
//                String assignmentID = assignment.getId();
//                Integer userEstimate = userEstimates.get(assignmentID);

                String title = assignment.getTitle();
                String description = assignment.getDescription();

                LocalDate today = LocalDate.now();
                LocalDate dueDate = assignmentRetriver.getDueDate(assignment);
                if (dueDate == null) continue;

                long daysUntilDueDate = ChronoUnit.DAYS.between(today, dueDate);
                if (daysUntilDueDate <= 0) daysUntilDueDate = 1;

                String formattedDueDate = assignmentRetriver.getFormattedDueDate(dueDate);
                TimeEstimatorService timeEstimatorService = new TimeEstimatorService();
                int totalEstimatedMinutes;
//                if(userEstimate != null || userEstimate > 0){
//                    totalEstimatedMinutes = userEstimate;
//                } else{
                    totalEstimatedMinutes = timeEstimatorService.estimateFromKeyWord(title, description);
                //}
                List<Long> chunks = timeEstimatorService.divideAcrossDays(totalEstimatedMinutes, daysUntilDueDate);

                PlannedTask plannedTask = new PlannedTask(
                        assignment,
                        title,
                        description,
                        dueDate,
                        totalEstimatedMinutes,
                        chunks
                );
                plannedTasks.add(plannedTask);
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        return plannedTasks;

    }
}
