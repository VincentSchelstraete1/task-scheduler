package com.vincents.taskscheduler;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {
    @GetMapping("/")
    public String redirectToAuthorize() {
        return "redirect:/authorize";
    }
}