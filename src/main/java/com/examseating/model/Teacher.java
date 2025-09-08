package com.examseating.model;

/**
 * Teacher model class
 */
public class Teacher extends User {
    private String department;
    private boolean isAdmin;
    
    // Constructors
    public Teacher() {
        super();
        this.isAdmin = false;
    }
    
    public Teacher(String name, String email, String password, String department) {
        super(name, email, password);
        this.department = department;
        this.isAdmin = false;
    }
    
    public Teacher(int id, String name, String email, String password, 
                   String department, boolean isAdmin) {
        super(id, name, email, password);
        this.department = department;
        this.isAdmin = isAdmin;
    }
    
    // Getters and Setters
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public boolean isAdmin() {
        return isAdmin;
    }
    
    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
    
    @Override
    public String getRole() {
        return isAdmin ? "ADMIN" : "TEACHER";
    }
    
    // Business methods
    public boolean canManageStudents() {
        return true; // All teachers can manage students
    }
    
    public boolean canCreateExams() {
        return true; // All teachers can create exams
    }
    
    public boolean canManageSystem() {
        return isAdmin; // Only admins can manage system settings
    }
    
    public String getDisplayTitle() {
        return isAdmin ? "Admin" : "Teacher";
    }
    
    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", department='" + department + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }
}