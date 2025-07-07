package com.vincents.taskscheduler.util;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.classroom.Classroom;
import com.vincents.taskscheduler.controller.AuthController;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class GoogleAuthUtil {
    private static final List<String> SCOPES = List.of(
            "https://www.googleapis.com/auth/calendar",
            "https://www.googleapis.com/auth/classroom.coursework.students",
            "https://www.googleapis.com/auth/classroom.courses.readonly",
            "https://www.googleapis.com/auth/classroom.coursework.me.readonly"

    );

    private static final JsonFactory JSON_FACTORY = new GsonFactory().getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    public static JsonFactory getJsonFactory(){
        return JSON_FACTORY;
    }

    public static GoogleAuthorizationCodeFlow getFlow(){
        try{
            InputStream in = AuthController.class.getClassLoader().getResourceAsStream("credentials.json");
            if (in == null) {
                throw new RuntimeException("Unable to load credentials.json");
            }

            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

            return new GoogleAuthorizationCodeFlow.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JSON_FACTORY,
                    clientSecrets,
                    SCOPES)
                    .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                    .setAccessType("offline")
                    .build();

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static Classroom getClassroomService(){
       try {
           GoogleAuthorizationCodeFlow flow = GoogleAuthUtil.getFlow();
           Credential credential = flow.loadCredential("user");
           if (credential == null) {
               throw new IllegalStateException("User not authenticated yet. Visit /authorize first.");
           }

           return new Classroom.Builder(
                   GoogleNetHttpTransport.newTrustedTransport(),
                   GoogleAuthUtil.getJsonFactory(),
                   credential
           ).setApplicationName("Task Scheduler").build();
       }catch (Exception e){
           throw new RuntimeException(e);
       }

    }

    public static Calendar getCalendarService(){
        try {
            GoogleAuthorizationCodeFlow flow = GoogleAuthUtil.getFlow();
            Credential credential = flow.loadCredential("user");
            if (credential == null) {
                throw new IllegalStateException("User not authenticated yet. Visit /authorize first.");
            }

            return new Calendar.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GoogleAuthUtil.getJsonFactory(),
                    credential)
                    .setApplicationName("Task Scheduler").build();

        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

}
