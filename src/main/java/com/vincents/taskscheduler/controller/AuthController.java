package com.vincents.taskscheduler;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;

import com.vincents.taskscheduler.service.scheduling.AssignmentRetriver;
import com.vincents.taskscheduler.util.GoogleAuthUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/authorize")
public class AuthController {

    private final GoogleClassroomService classroomService;
    private final AssignmentRetriver assignmentRetriver;

    public AuthController(GoogleClassroomService classroomService, AssignmentRetriver assignmentRetriver) {
        this.classroomService = classroomService;
        this.assignmentRetriver = assignmentRetriver;
    }

    @GetMapping("/listcourses")
    @ResponseBody
    public String listCourses(){
        return classroomService.listCourses();
    }

    @GetMapping("/getassignments")
    @ResponseBody
    public String getAssignments(){
        return assignmentRetriver.listCourseAssignments();
    }



    @GetMapping
    public String authorizeUser(){
        try{
            GoogleAuthorizationCodeFlow flow = GoogleAuthUtil.getFlow();
            String redirectURL = "http://localhost:8080/oauth2callback";
            String authorizeURL = flow.newAuthorizationUrl().setRedirectUri(redirectURL).build();
            return "redirect:" + authorizeURL;
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

}
