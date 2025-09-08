package com.examseating.dao;

import com.examseating.model.Student;
import com.examseating.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for Student operations
 */
public class StudentDAO {
    private static final Logger logger = Logger.getLogger(StudentDAO.class.getName());
    
    // SQL Queries
    private static final String INSERT_STUDENT = 
        "INSERT INTO students (roll_no, name, email, password, department, year, semester, phone, address) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String SELECT_STUDENT_BY_ID = 
        "SELECT * FROM students WHERE id = ?";
    
    private static final String SELECT_STUDENT_BY_EMAIL = 
        "SELECT * FROM students WHERE email = ?";
    
    private static final String SELECT_STUDENT_BY_ROLL_NO = 
        "SELECT * FROM students WHERE roll_no = ?";
    
    private static final String SELECT_ALL_STUDENTS = 
        "SELECT * FROM students WHERE is_active = TRUE ORDER BY roll_no";
    
    private static final String SELECT_STUDENTS_BY_DEPARTMENT = 
        "SELECT * FROM students WHERE department = ? AND is_active = TRUE ORDER BY roll_no";
    
    private static final String SELECT_STUDENTS_BY_YEAR_SEMESTER = 
        "SELECT * FROM students WHERE year = ? AND semester = ? AND is_active = TRUE ORDER BY roll_no";
    
    private static final String UPDATE_STUDENT = 
        "UPDATE students SET name = ?, email = ?, department = ?, year = ?, semester = ?, " +
        "phone = ?, address = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
    
    private static final String UPDATE_STUDENT_PASSWORD = 
        "UPDATE students SET password = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
    
    private static final String DELETE_STUDENT = 
        "UPDATE students SET is_active = FALSE, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
    
    private static final String COUNT_STUDENTS = 
        "SELECT COUNT(*) FROM students WHERE is_active = TRUE";
    
    private static final String SEARCH_STUDENTS = 
        "SELECT * FROM students WHERE is_active = TRUE AND " +
        "(name LIKE ? OR email LIKE ? OR roll_no LIKE ? OR department LIKE ?) " +
        "ORDER BY roll_no LIMIT ? OFFSET ?";
    
    /**
     * Create a new student
     */
    public boolean create(Student student) {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_STUDENT, Statement.RETURN_GENERATED_KEYS)) {
            
            statement.setString(1, student.getRollNo());
            statement.setString(2, student.getName());
            statement.setString(3, student.getEmail());
            statement.setString(4, student.getPassword());
            statement.setString(5, student.getDepartment());
            statement.setInt(6, student.getYear());
            statement.setInt(7, student.getSemester());
            statement.setString(8, student.getPhone());
            statement.setString(9, student.getAddress());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        student.setId(generatedKeys.getInt(1));
                        logger.info("Student created successfully: " + student.getRollNo());
                        return true;
                    }
                }
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error creating student: " + student.getRollNo(), e);
            return false;
        }
    }
    
    /**
     * Get student by ID
     */
    public Student getById(int id) {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_STUDENT_BY_ID)) {
            
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return mapResultSetToStudent(resultSet);
            }
            
            return null;
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting student by ID: " + id, e);
            return null;
        }
    }
    
    /**
     * Get student by email
     */
    public Student getByEmail(String email) {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_STUDENT_BY_EMAIL)) {
            
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return mapResultSetToStudent(resultSet);
            }
            
            return null;
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting student by email: " + email, e);
            return null;
        }
    }
    
    /**
     * Get student by roll number
     */
    public Student getByRollNo(String rollNo) {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_STUDENT_BY_ROLL_NO)) {
            
            statement.setString(1, rollNo);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return mapResultSetToStudent(resultSet);
            }
            
            return null;
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting student by roll number: " + rollNo, e);
            return null;
        }
    }
    
    /**
     * Get all active students
     */
    public List<Student> getAll() {
        List<Student> students = new ArrayList<>();
        
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_STUDENTS);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                students.add(mapResultSetToStudent(resultSet));
            }
            
            logger.info("Retrieved " + students.size() + " students");
            return students;
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting all students", e);
            return students;
        }
    }
    
    /**
     * Get students by department
     */
    public List<Student> getByDepartment(String department) {
        List<Student> students = new ArrayList<>();
        
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_STUDENTS_BY_DEPARTMENT)) {
            
            statement.setString(1, department);
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                students.add(mapResultSetToStudent(resultSet));
            }
            
            return students;
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting students by department: " + department, e);
            return students;
        }
    }
    
    /**
     * Get students by year and semester
     */
    public List<Student> getByYearAndSemester(int year, int semester) {
        List<Student> students = new ArrayList<>();
        
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_STUDENTS_BY_YEAR_SEMESTER)) {
            
            statement.setInt(1, year);
            statement.setInt(2, semester);
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                students.add(mapResultSetToStudent(resultSet));
            }
            
            return students;
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting students by year/semester: " + year + "/" + semester, e);
            return students;
        }
    }
    
    /**
     * Update student
     */
    public boolean update(Student student) {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_STUDENT)) {
            
            statement.setString(1, student.getName());
            statement.setString(2, student.getEmail());
            statement.setString(3, student.getDepartment());
            statement.setInt(4, student.getYear());
            statement.setInt(5, student.getSemester());
            statement.setString(6, student.getPhone());
            statement.setString(7, student.getAddress());
            statement.setInt(8, student.getId());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows > 0) {
                logger.info("Student updated successfully: " + student.getRollNo());
                return true;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating student: " + student.getRollNo(), e);
            return false;
        }
    }
    
    /**
     * Update student password
     */
    public boolean updatePassword(int studentId, String newPassword) {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_STUDENT_PASSWORD)) {
            
            statement.setString(1, newPassword);
            statement.setInt(2, studentId);
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows > 0) {
                logger.info("Student password updated successfully: " + studentId);
                return true;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating student password: " + studentId, e);
            return false;
        }
    }
    
    /**
     * Delete student (soft delete)
     */
    public boolean delete(int studentId) {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_STUDENT)) {
            
            statement.setInt(1, studentId);
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows > 0) {
                logger.info("Student deleted successfully: " + studentId);
                return true;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting student: " + studentId, e);
            return false;
        }
    }
    
    /**
     * Get total count of students
     */
    public int getCount() {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(COUNT_STUDENTS);
             ResultSet resultSet = statement.executeQuery()) {
            
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            
            return 0;
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting student count", e);
            return 0;
        }
    }
    
    /**
     * Search students
     */
    public List<Student> search(String searchTerm, int limit, int offset) {
        List<Student> students = new ArrayList<>();
        
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(SEARCH_STUDENTS)) {
            
            String searchPattern = "%" + searchTerm + "%";
            statement.setString(1, searchPattern);
            statement.setString(2, searchPattern);
            statement.setString(3, searchPattern);
            statement.setString(4, searchPattern);
            statement.setInt(5, limit);
            statement.setInt(6, offset);
            
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                students.add(mapResultSetToStudent(resultSet));
            }
            
            return students;
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error searching students: " + searchTerm, e);
            return students;
        }
    }
    
    /**
     * Check if email exists
     */
    public boolean emailExists(String email) {
        return getByEmail(email) != null;
    }
    
    /**
     * Check if roll number exists
     */
    public boolean rollNoExists(String rollNo) {
        return getByRollNo(rollNo) != null;
    }
    
    /**
     * Map ResultSet to Student object
     */
    private Student mapResultSetToStudent(ResultSet resultSet) throws SQLException {
        Student student = new Student();
        student.setId(resultSet.getInt("id"));
        student.setRollNo(resultSet.getString("roll_no"));
        student.setName(resultSet.getString("name"));
        student.setEmail(resultSet.getString("email"));
        student.setPassword(resultSet.getString("password"));
        student.setDepartment(resultSet.getString("department"));
        student.setYear(resultSet.getInt("year"));
        student.setSemester(resultSet.getInt("semester"));
        student.setPhone(resultSet.getString("phone"));
        student.setAddress(resultSet.getString("address"));
        student.setActive(resultSet.getBoolean("is_active"));
        student.setCreatedAt(resultSet.getTimestamp("created_at"));
        student.setUpdatedAt(resultSet.getTimestamp("updated_at"));
        return student;
    }
}