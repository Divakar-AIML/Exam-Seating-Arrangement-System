package com.examseating.model;

/**
 * Student model class
 */
public class Student extends User {
    private String rollNo;
    private String department;
    private int year;
    private int semester;
    private String address;
    private boolean isActive;
    
    // Constructors
    public Student() {
        super();
        this.isActive = true;
    }
    
    public Student(String rollNo, String name, String email, String password, 
                   String department, int year, int semester) {
        super(name, email, password);
        this.rollNo = rollNo;
        this.department = department;
        this.year = year;
        this.semester = semester;
        this.isActive = true;
    }
    
    public Student(int id, String rollNo, String name, String email, String password,
                   String department, int year, int semester) {
        super(id, name, email, password);
        this.rollNo = rollNo;
        this.department = department;
        this.year = year;
        this.semester = semester;
        this.isActive = true;
    }
    
    // Getters and Setters
    public String getRollNo() {
        return rollNo;
    }
    
    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public int getYear() {
        return year;
    }
    
    public void setYear(int year) {
        this.year = year;
    }
    
    public int getSemester() {
        return semester;
    }
    
    public void setSemester(int semester) {
        this.semester = semester;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    @Override
    public String getRole() {
        return "STUDENT";
    }
    
    // Business methods
    public String getFullDisplayName() {
        return rollNo + " - " + name;
    }
    
    public String getYearSemesterDisplay() {
        return "Year " + year + ", Sem " + semester;
    }
    
    public boolean isEligibleForExam() {
        return isActive;
    }
    
    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", rollNo='" + rollNo + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", department='" + department + '\'' +
                ", year=" + year +
                ", semester=" + semester +
                ", isActive=" + isActive +
                '}';
    }
}