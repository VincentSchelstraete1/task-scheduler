package com.vincents.taskscheduler.controller;

import com.google.api.services.classroom.model.CourseWork;
import com.vincents.taskscheduler.model.PlannedTask;
import com.vincents.taskscheduler.service.scheduling.AssignmentRetriver;
import com.vincents.taskscheduler.service.scheduling.PlannedTaskGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/assignments")
public class AssignmentController {
    @Autowired
    private PlannedTaskGenerator plannedTaskGenerator;

    @GetMapping
    public Map<String, List<PlannedTask>> getAssignments(){
        return plannedTaskGenerator.generateAllTasks();
    }
}
