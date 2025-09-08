package com.examseating.dao;

import com.examseating.model.Teacher;
import com.examseating.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for Teacher operations
 */
public class TeacherDAO {
    private static final Logger logger = Logger.getLogger(TeacherDAO.class.getName());
    
    // SQL Queries
    private static final String INSERT_TEACHER = 
        "INSERT INTO teachers (name, email, password, department, phone, is_admin) VALUES (?, ?, ?, ?, ?, ?)";
    
    private static final String SELECT_TEACHER_BY_ID = 
        "SELECT * FROM teachers WHERE id = ?";
    
    private static final String SELECT_TEACHER_BY_EMAIL = 
        "SELECT * FROM teachers WHERE email = ?";
    
    private static final String SELECT_ALL_TEACHERS = 
        "SELECT * FROM teachers ORDER BY name";
    
    private static final String SELECT_TEACHERS_BY_DEPARTMENT = 
        "SELECT * FROM teachers WHERE department = ? ORDER BY name";
    
    private static final String UPDATE_TEACHER = 
        "UPDATE teachers SET name = ?, email = ?, department = ?, phone = ?, " +
        "updated_at = CURRENT_TIMESTAMP WHERE id = ?";
    
    private static final String UPDATE_TEACHER_PASSWORD = 
        "UPDATE teachers SET password = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
    
    private static final String DELETE_TEACHER = 
        "DELETE FROM teachers WHERE id = ?";
    
    private static final String COUNT_TEACHERS = 
        "SELECT COUNT(*) FROM teachers";
    
    private static final String SEARCH_TEACHERS = 
        "SELECT * FROM teachers WHERE (name LIKE ? OR email LIKE ? OR department LIKE ?) " +
        "ORDER BY name LIMIT ? OFFSET ?";
    
    /**
     * Create a new teacher
     */
    public boolean create(Teacher teacher) {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_TEACHER, Statement.RETURN_GENERATED_KEYS)) {
            
            statement.setString(1, teacher.getName());
            statement.setString(2, teacher.getEmail());
            statement.setString(3, teacher.getPassword());
            statement.setString(4, teacher.getDepartment());
            statement.setString(5, teacher.getPhone());
            statement.setBoolean(6, teacher.isAdmin());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        teacher.setId(generatedKeys.getInt(1));
                        logger.info("Teacher created successfully: " + teacher.getEmail());
                        return true;
                    }
                }
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error creating teacher: " + teacher.getEmail(), e);
            return false;
        }
    }
    
    /**
     * Get teacher by ID
     */
    public Teacher getById(int id) {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_TEACHER_BY_ID)) {
            
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return mapResultSetToTeacher(resultSet);
            }
            
            return null;
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting teacher by ID: " + id, e);
            return null;
        }
    }
    
    /**
     * Get teacher by email
     */
    public Teacher getByEmail(String email) {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_TEACHER_BY_EMAIL)) {
            
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return mapResultSetToTeacher(resultSet);
            }
            
            return null;
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting teacher by email: " + email, e);
            return null;
        }
    }
    
    /**
     * Get all teachers
     */
    public List<Teacher> getAll() {
        List<Teacher> teachers = new ArrayList<>();
        
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_TEACHERS);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                teachers.add(mapResultSetToTeacher(resultSet));
            }
            
            logger.info("Retrieved " + teachers.size() + " teachers");
            return teachers;
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting all teachers", e);
            return teachers;
        }
    }
    
    /**
     * Get teachers by department
     */
    public List<Teacher> getByDepartment(String department) {
        List<Teacher> teachers = new ArrayList<>();
        
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_TEACHERS_BY_DEPARTMENT)) {
            
            statement.setString(1, department);
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                teachers.add(mapResultSetToTeacher(resultSet));
            }
            
            return teachers;
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting teachers by department: " + department, e);
            return teachers;
        }
    }
    
    /**
     * Update teacher
     */
    public boolean update(Teacher teacher) {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_TEACHER)) {
            
            statement.setString(1, teacher.getName());
            statement.setString(2, teacher.getEmail());
            statement.setString(3, teacher.getDepartment());
            statement.setString(4, teacher.getPhone());
            statement.setInt(5, teacher.getId());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows > 0) {
                logger.info("Teacher updated successfully: " + teacher.getEmail());
                return true;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating teacher: " + teacher.getEmail(), e);
            return false;
        }
    }
    
    /**
     * Update teacher password
     */
    public boolean updatePassword(int teacherId, String newPassword) {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_TEACHER_PASSWORD)) {
            
            statement.setString(1, newPassword);
            statement.setInt(2, teacherId);
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows > 0) {
                logger.info("Teacher password updated successfully: " + teacherId);
                return true;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating teacher password: " + teacherId, e);
            return false;
        }
    }
    
    /**
     * Delete teacher
     */
    public boolean delete(int teacherId) {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_TEACHER)) {
            
            statement.setInt(1, teacherId);
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows > 0) {
                logger.info("Teacher deleted successfully: " + teacherId);
                return true;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting teacher: " + teacherId, e);
            return false;
        }
    }
    
    /**
     * Get total count of teachers
     */
    public int getCount() {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(COUNT_TEACHERS);
             ResultSet resultSet = statement.executeQuery()) {
            
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            
            return 0;
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting teacher count", e);
            return 0;
        }
    }
    
    /**
     * Search teachers
     */
    public List<Teacher> search(String searchTerm, int limit, int offset) {
        List<Teacher> teachers = new ArrayList<>();
        
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(SEARCH_TEACHERS)) {
            
            String searchPattern = "%" + searchTerm + "%";
            statement.setString(1, searchPattern);
            statement.setString(2, searchPattern);
            statement.setString(3, searchPattern);
            statement.setInt(4, limit);
            statement.setInt(5, offset);
            
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                teachers.add(mapResultSetToTeacher(resultSet));
            }
            
            return teachers;
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error searching teachers: " + searchTerm, e);
            return teachers;
        }
    }
    
    /**
     * Check if email exists
     */
    public boolean emailExists(String email) {
        return getByEmail(email) != null;
    }
    
    /**
     * Get all admin users
     */
    public List<Teacher> getAdmins() {
        List<Teacher> admins = new ArrayList<>();
        
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                 "SELECT * FROM teachers WHERE is_admin = TRUE ORDER BY name")) {
            
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                admins.add(mapResultSetToTeacher(resultSet));
            }
            
            return admins;
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting admin users", e);
            return admins;
        }
    }
    
    /**
     * Map ResultSet to Teacher object
     */
    private Teacher mapResultSetToTeacher(ResultSet resultSet) throws SQLException {
        Teacher teacher = new Teacher();
        teacher.setId(resultSet.getInt("id"));
        teacher.setName(resultSet.getString("name"));
        teacher.setEmail(resultSet.getString("email"));
        teacher.setPassword(resultSet.getString("password"));
        teacher.setDepartment(resultSet.getString("department"));
        teacher.setPhone(resultSet.getString("phone"));
        teacher.setAdmin(resultSet.getBoolean("is_admin"));
        teacher.setCreatedAt(resultSet.getTimestamp("created_at"));
        teacher.setUpdatedAt(resultSet.getTimestamp("updated_at"));
        return teacher;
    }
}