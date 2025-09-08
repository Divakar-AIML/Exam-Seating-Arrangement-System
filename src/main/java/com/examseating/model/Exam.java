package com.examseating.model;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * Exam model class
 */
public class Exam {
    private int id;
    private String subject;
    private String examCode;
    private Date examDate;
    private Time startTime;
    private Time endTime;
    private int duration; // in minutes
    private int totalMarks;
    private int minMarks;
    private String instructions;
    private ExamStatus status;
    private int createdBy;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Teacher information (for joins)
    private String createdByName;
    
    // Enum for exam status
    public enum ExamStatus {
        SCHEDULED, ONGOING, COMPLETED, CANCELLED
    }
    
    // Constructors
    public Exam() {
        this.status = ExamStatus.SCHEDULED;
        this.minMarks = 35; // Default passing marks
    }
    
    public Exam(String subject, String examCode, Date examDate, Time startTime, 
                Time endTime, int duration, int totalMarks, int createdBy) {
        this();
        this.subject = subject;
        this.examCode = examCode;
        this.examDate = examDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.totalMarks = totalMarks;
        this.createdBy = createdBy;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public String getExamCode() {
        return examCode;
    }
    
    public void setExamCode(String examCode) {
        this.examCode = examCode;
    }
    
    public Date getExamDate() {
        return examDate;
    }
    
    public void setExamDate(Date examDate) {
        this.examDate = examDate;
    }
    
    public Time getStartTime() {
        return startTime;
    }
    
    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }
    
    public Time getEndTime() {
        return endTime;
    }
    
    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }
    
    public int getDuration() {
        return duration;
    }
    
    public void setDuration(int duration) {
        this.duration = duration;
    }
    
    public int getTotalMarks() {
        return totalMarks;
    }
    
    public void setTotalMarks(int totalMarks) {
        this.totalMarks = totalMarks;
    }
    
    public int getMinMarks() {
        return minMarks;
    }
    
    public void setMinMarks(int minMarks) {
        this.minMarks = minMarks;
    }
    
    public String getInstructions() {
        return instructions;
    }
    
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
    
    public ExamStatus getStatus() {
        return status;
    }
    
    public void setStatus(ExamStatus status) {
        this.status = status;
    }
    
    public int getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }
    
    public String getCreatedByName() {
        return createdByName;
    }
    
    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // Business methods
    public String getFormattedDuration() {
        int hours = duration / 60;
        int minutes = duration % 60;
        if (hours > 0) {
            return hours + "h " + minutes + "m";
        } else {
            return minutes + "m";
        }
    }
    
    public String getExamDisplayName() {
        return examCode + " - " + subject;
    }
    
    public boolean isCompleted() {
        return status == ExamStatus.COMPLETED;
    }
    
    public boolean isOngoing() {
        return status == ExamStatus.ONGOING;
    }
    
    public boolean isScheduled() {
        return status == ExamStatus.SCHEDULED;
    }
    
    public boolean isCancelled() {
        return status == ExamStatus.CANCELLED;
    }
    
    public boolean canBeModified() {
        return status == ExamStatus.SCHEDULED;
    }
    
    public String getStatusDisplayText() {
        switch (status) {
            case SCHEDULED:
                return "Scheduled";
            case ONGOING:
                return "In Progress";
            case COMPLETED:
                return "Completed";
            case CANCELLED:
                return "Cancelled";
            default:
                return "Unknown";
        }
    }
    
    public String getStatusCssClass() {
        switch (status) {
            case SCHEDULED:
                return "status-scheduled";
            case ONGOING:
                return "status-ongoing";
            case COMPLETED:
                return "status-completed";
            case CANCELLED:
                return "status-cancelled";
            default:
                return "status-unknown";
        }
    }
    
    @Override
    public String toString() {
        return "Exam{" +
                "id=" + id +
                ", subject='" + subject + '\'' +
                ", examCode='" + examCode + '\'' +
                ", examDate=" + examDate +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", duration=" + duration +
                ", totalMarks=" + totalMarks +
                ", status=" + status +
                ", createdBy=" + createdBy +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Exam exam = (Exam) obj;
        return id == exam.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}