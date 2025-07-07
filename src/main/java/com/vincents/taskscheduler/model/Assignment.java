package com.vincents.taskscheduler.model;

import java.time.LocalDate;

public class Assignment {
    private String id;
    private String title;
    private String courseName;
    private String description;
    private LocalDate dueDate;
    private String alternateLink;

    public Assignment(String id, String title, String courseName, String description, LocalDate dueDate, String alternateLink) {
        this.id = id;
        this.title = title;
        this.courseName = courseName;
        this.description = description;
        this.dueDate = dueDate;
        this.alternateLink = alternateLink;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getCourseName() {
        return courseName;
    }
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public LocalDate getDueDate() {
        return dueDate;
    }
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
    public String getAlternateLink() {
        return alternateLink;
    }
    public void setAlternateLink(String alternateLink) {
        this.alternateLink = alternateLink;
    }

}
