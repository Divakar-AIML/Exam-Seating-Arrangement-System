package com.examseating.model;

import java.sql.Timestamp;

/**
 * SeatingArrangement model class
 */
public class SeatingArrangement {
    private int id;
    private int examId;
    private int studentId;
    private int hallId;
    private int seatNumber;
    private int seatRow;
    private int seatColumn;
    private SeatingStatus status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Related entity information (for joins)
    private String studentName;
    private String studentRollNo;
    private String hallName;
    private String examSubject;
    private String examCode;
    
    // Enum for seating status
    public enum SeatingStatus {
        ASSIGNED, PRESENT, ABSENT
    }
    
    // Constructors
    public SeatingArrangement() {
        this.status = SeatingStatus.ASSIGNED;
    }
    
    public SeatingArrangement(int examId, int studentId, int hallId, 
                             int seatNumber, int seatRow, int seatColumn) {
        this();
        this.examId = examId;
        this.studentId = studentId;
        this.hallId = hallId;
        this.seatNumber = seatNumber;
        this.seatRow = seatRow;
        this.seatColumn = seatColumn;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getExamId() {
        return examId;
    }
    
    public void setExamId(int examId) {
        this.examId = examId;
    }
    
    public int getStudentId() {
        return studentId;
    }
    
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
    
    public int getHallId() {
        return hallId;
    }
    
    public void setHallId(int hallId) {
        this.hallId = hallId;
    }
    
    public int getSeatNumber() {
        return seatNumber;
    }
    
    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }
    
    public int getSeatRow() {
        return seatRow;
    }
    
    public void setSeatRow(int seatRow) {
        this.seatRow = seatRow;
    }
    
    public int getSeatColumn() {
        return seatColumn;
    }
    
    public void setSeatColumn(int seatColumn) {
        this.seatColumn = seatColumn;
    }
    
    public SeatingStatus getStatus() {
        return status;
    }
    
    public void setStatus(SeatingStatus status) {
        this.status = status;
    }
    
    public String getStudentName() {
        return studentName;
    }
    
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    
    public String getStudentRollNo() {
        return studentRollNo;
    }
    
    public void setStudentRollNo(String studentRollNo) {
        this.studentRollNo = studentRollNo;
    }
    
    public String getHallName() {
        return hallName;
    }
    
    public void setHallName(String hallName) {
        this.hallName = hallName;
    }
    
    public String getExamSubject() {
        return examSubject;
    }
    
    public void setExamSubject(String examSubject) {
        this.examSubject = examSubject;
    }
    
    public String getExamCode() {
        return examCode;
    }
    
    public void setExamCode(String examCode) {
        this.examCode = examCode;
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
    public String getSeatPosition() {
        return "Row " + seatRow + ", Seat " + seatColumn;
    }
    
    public String getSeatDisplayText() {
        return "Seat " + seatNumber + " (" + getSeatPosition() + ")";
    }
    
    public String getStatusDisplayText() {
        switch (status) {
            case ASSIGNED:
                return "Assigned";
            case PRESENT:
                return "Present";
            case ABSENT:
                return "Absent";
            default:
                return "Unknown";
        }
    }
    
    public String getStatusCssClass() {
        switch (status) {
            case ASSIGNED:
                return "status-assigned";
            case PRESENT:
                return "status-present";
            case ABSENT:
                return "status-absent";
            default:
                return "status-unknown";
        }
    }
    
    public boolean isStudentPresent() {
        return status == SeatingStatus.PRESENT;
    }
    
    public boolean isStudentAbsent() {
        return status == SeatingStatus.ABSENT;
    }
    
    public boolean canMarkAttendance() {
        return status == SeatingStatus.ASSIGNED;
    }
    
    public String getStudentDisplay() {
        if (studentRollNo != null && studentName != null) {
            return studentRollNo + " - " + studentName;
        } else if (studentName != null) {
            return studentName;
        } else {
            return "Student ID: " + studentId;
        }
    }
    
    public String getExamDisplay() {
        if (examCode != null && examSubject != null) {
            return examCode + " - " + examSubject;
        } else if (examSubject != null) {
            return examSubject;
        } else {
            return "Exam ID: " + examId;
        }
    }
    
    @Override
    public String toString() {
        return "SeatingArrangement{" +
                "id=" + id +
                ", examId=" + examId +
                ", studentId=" + studentId +
                ", hallId=" + hallId +
                ", seatNumber=" + seatNumber +
                ", seatRow=" + seatRow +
                ", seatColumn=" + seatColumn +
                ", status=" + status +
                ", studentRollNo='" + studentRollNo + '\'' +
                ", hallName='" + hallName + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SeatingArrangement that = (SeatingArrangement) obj;
        return id == that.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}