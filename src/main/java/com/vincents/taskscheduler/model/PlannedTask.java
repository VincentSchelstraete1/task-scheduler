package com.vincents.taskscheduler;

import com.google.api.services.classroom.model.CourseWork;

import java.time.LocalDate;
import java.util.List;

public class PlannedTask {
    private CourseWork assignment;
    private String title;
    private String description;
    private LocalDate dueDate;
    private int estimatedMinutes;
    private List<Long> chunks;

    public PlannedTask(CourseWork assignment, String title, String description, LocalDate dueDate, int estimatedMinutes, List<Long> chunks) {
        this.assignment = assignment;
        this.title = title;
        this.dueDate = dueDate;
        this.estimatedMinutes = estimatedMinutes;
        this.chunks = chunks;
        this.description = description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public CourseWork getAssignment() {
        return assignment;
    }

    public int getEstimatedMinutes() {
        return estimatedMinutes;
    }

    public List<Long> getChunks() {
        return chunks;
    }

    public void setChunks(List<Long> chunks) {
        this.chunks = chunks;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setEstimatedMinutes(int estimatedMinutes) {
        this.estimatedMinutes = estimatedMinutes;
    }
}
