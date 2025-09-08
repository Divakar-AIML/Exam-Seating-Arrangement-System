package com.examseating.servlet;

import com.examseating.dao.StudentDAO;
import com.examseating.dao.TeacherDAO;
import com.examseating.model.Student;
import com.examseating.model.Teacher;
import com.examseating.util.PasswordUtil;
import com.examseating.util.ValidationUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet for handling user authentication
 */
public class LoginServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(LoginServlet.class.getName());
    private StudentDAO studentDAO;
    private TeacherDAO teacherDAO;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        super.init();
        studentDAO = new StudentDAO();
        teacherDAO = new TeacherDAO();
        gson = new Gson();
        logger.info("LoginServlet initialized");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            // Parse request body
            JsonObject requestData = gson.fromJson(request.getReader(), JsonObject.class);
            
            String email = requestData.get("email").getAsString().trim();
            String password = requestData.get("password").getAsString();
            String userType = requestData.get("userType").getAsString();
            boolean rememberMe = requestData.has("rememberMe") && 
                               requestData.get("rememberMe").getAsBoolean();
            
            // Validate input
            JsonObject responseData = new JsonObject();
            
            if (!ValidationUtil.isValidEmail(email)) {
                responseData.addProperty("success", false);
                responseData.addProperty("message", "Invalid email format");
                sendResponse(response, responseData);
                return;
            }
            
            if (ValidationUtil.isEmpty(password)) {
                responseData.addProperty("success", false);
                responseData.addProperty("message", "Password is required");
                sendResponse(response, responseData);
                return;
            }
            
            // Authenticate user based on type
            boolean loginSuccess = false;
            String userName = "";
            String userRole = "";
            int userId = 0;
            
            if ("student".equals(userType)) {
                Student student = studentDAO.getByEmail(email);
                if (student != null && student.isActive()) {
                    if (PasswordUtil.verifyPassword(password, student.getPassword())) {
                        loginSuccess = true;
                        userName = student.getName();
                        userRole = "STUDENT";
                        userId = student.getId();
                        
                        // Create session
                        HttpSession session = request.getSession(true);
                        session.setAttribute("userId", userId);
                        session.setAttribute("userType", "student");
                        session.setAttribute("userName", userName);
                        session.setAttribute("userEmail", email);
                        session.setAttribute("student", student);
                        
                        // Set session timeout (30 minutes)
                        session.setMaxInactiveInterval(30 * 60);
                        
                        logger.info("Student login successful: " + email);
                    }
                }
            } else if ("teacher".equals(userType)) {
                Teacher teacher = teacherDAO.getByEmail(email);
                if (teacher != null) {
                    if (PasswordUtil.verifyPassword(password, teacher.getPassword())) {
                        loginSuccess = true;
                        userName = teacher.getName();
                        userRole = teacher.isAdmin() ? "ADMIN" : "TEACHER";
                        userId = teacher.getId();
                        
                        // Create session
                        HttpSession session = request.getSession(true);
                        session.setAttribute("userId", userId);
                        session.setAttribute("userType", "teacher");
                        session.setAttribute("userName", userName);
                        session.setAttribute("userEmail", email);
                        session.setAttribute("isAdmin", teacher.isAdmin());
                        session.setAttribute("teacher", teacher);
                        
                        // Set session timeout (30 minutes)
                        session.setMaxInactiveInterval(30 * 60);
                        
                        logger.info("Teacher login successful: " + email);
                    }
                }
            }
            
            if (loginSuccess) {
                responseData.addProperty("success", true);
                responseData.addProperty("message", "Login successful");
                responseData.addProperty("userType", userType);
                responseData.addProperty("userName", userName);
                responseData.addProperty("userRole", userRole);
                
                // Handle remember me functionality
                if (rememberMe) {
                    // Set remember me cookie (valid for 7 days)
                    javax.servlet.http.Cookie rememberCookie = 
                        new javax.servlet.http.Cookie("rememberedEmail", email);
                    rememberCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
                    rememberCookie.setHttpOnly(true);
                    rememberCookie.setPath("/");
                    response.addCookie(rememberCookie);
                }
                
            } else {
                responseData.addProperty("success", false);
                responseData.addProperty("message", "Invalid email or password");
                
                // Log failed login attempt
                logger.warning("Login failed for email: " + email + ", userType: " + userType);
            }
            
            sendResponse(response, responseData);
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error during login", e);
            
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("success", false);
            errorResponse.addProperty("message", "Login failed due to server error");
            
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            sendResponse(response, errorResponse);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check authentication status
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        JsonObject responseData = new JsonObject();
        HttpSession session = request.getSession(false);
        
        if (session != null && session.getAttribute("userId") != null) {
            responseData.addProperty("authenticated", true);
            responseData.addProperty("userType", (String) session.getAttribute("userType"));
            responseData.addProperty("userName", (String) session.getAttribute("userName"));
            responseData.addProperty("userEmail", (String) session.getAttribute("userEmail"));
            
            if ("teacher".equals(session.getAttribute("userType"))) {
                responseData.addProperty("isAdmin", 
                    session.getAttribute("isAdmin") != null && 
                    (Boolean) session.getAttribute("isAdmin"));
            }
        } else {
            responseData.addProperty("authenticated", false);
        }
        
        sendResponse(response, responseData);
    }
    
    private void sendResponse(HttpServletResponse response, JsonObject data) 
            throws IOException {
        PrintWriter out = response.getWriter();
        out.print(data.toString());
        out.flush();
    }
}