package com.vincents.taskscheduler.model;

import com.google.api.services.classroom.model.CourseWork;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
/*
Needs to be modified to integrate assignment

 */
public class PlannedTask {
    private String title;
    private String description;
    private LocalDate dueDate;
    private String formattedDueDate;
    private int estimatedMinutes;
    private List<Long> chunks;
    private Urgency urgency;
    private String alternateLink;

    public enum Urgency{
        NOT_URGENT,
        SOMEWHAT_URGENT,
        URGENT
    }


    public PlannedTask(String title, String description,LocalDate dueDate, String formattedDueDate, int estimatedMinutes, List<Long> chunks, String alternateLink) {
        this.title = title;
        this.dueDate = dueDate;
        this.formattedDueDate = formattedDueDate;
        this.estimatedMinutes = estimatedMinutes;
        this.chunks = chunks;
        this.description = description;
        this.urgency = calculateUrgency(dueDate);
        this.alternateLink = alternateLink;
    }

    private Urgency calculateUrgency(LocalDate dueDate){
        long daysUntilDue = ChronoUnit.DAYS.between(LocalDate.now(), dueDate);

        if (daysUntilDue <= 1) {
            return Urgency.URGENT;
        } else if (daysUntilDue <= 3) {
            return Urgency.SOMEWHAT_URGENT;
        } else {
            return Urgency.NOT_URGENT;
        }
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getFormattedDueDate() {
        return formattedDueDate;
    }
    public void setFormattedDueDate(String formattedDueDate) {
        this.formattedDueDate = formattedDueDate;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
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

    public void setDueDate(String formattedDueDate) {
        this.formattedDueDate = formattedDueDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setEstimatedMinutes(int estimatedMinutes) {
        this.estimatedMinutes = estimatedMinutes;
    }

    public Urgency getUrgency() {
        return urgency;
    }
    public void setUrgency(Urgency urgency) {
        this.urgency = urgency;
    }
    public String getAlternateLink() {
        return alternateLink;
    }
    public void setAlternateLink(String alternateLink) {
        this.alternateLink = alternateLink;
    }
}
