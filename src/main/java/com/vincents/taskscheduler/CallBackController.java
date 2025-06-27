package com.vincents.taskscheduler;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class CallBackController {


    @GetMapping("/oauth2callback")
    public String handleCallback(@RequestParam("code") String code) {
        try {
            GoogleAuthorizationCodeFlow flow = GoogleAuthUtil.getFlow();
            String redirectUri = "http://localhost:8080/oauth2callback";
            GoogleTokenResponse tokenResponse = flow.newTokenRequest(code).setRedirectUri(redirectUri).execute();
            Credential credential = flow.createAndStoreCredential(tokenResponse, "user");

            System.out.println("Access token: " + credential.getAccessToken());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "redirect:/index.html";
    }
}