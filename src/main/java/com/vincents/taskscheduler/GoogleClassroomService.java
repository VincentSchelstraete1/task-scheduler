package com.vincents.taskscheduler;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;

import com.google.api.services.classroom.Classroom;
import com.google.api.services.classroom.model.Course;
import com.google.api.services.classroom.model.ListCoursesResponse;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class GoogleClassroomService {

    public String listCourses() {
        try {
            List<Course> courses = getCourseList();

            if (courses == null || courses.isEmpty()) {
                return "No courses found.";
            }

            StringBuilder result = new StringBuilder("<h2>Courses:</h2><ul>");
            for (Course course : courses) {
                result.append(String.format("<li>%s (%s)</li>", course.getName(), course.getId()));
            }
            result.append("</ul>");
            return result.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "Error listing courses: " + e.getMessage();
        }
    }

    public List<Course> getCourseList(){
        try {
            Classroom service = GoogleAuthUtil.getClassroomService();
            ListCoursesResponse response = service.courses().list().setPageSize(10).execute();
            return response.getCourses();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public Classroom getClassroomService(){
        return GoogleAuthUtil.getClassroomService();
    }
}