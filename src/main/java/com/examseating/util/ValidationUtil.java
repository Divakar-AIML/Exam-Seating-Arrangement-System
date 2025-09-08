package com.examseating.util;

import java.util.regex.Pattern;

/**
 * Utility class for input validation
 */
public class ValidationUtil {
    
    // Email validation pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );
    
    // Phone number pattern (supports various formats)
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^[+]?[1-9]\\d{1,14}$|^[0-9]{10}$|^\\+[1-9]\\d{1,3}[\\s-]?\\d{3,4}[\\s-]?\\d{6,7}$"
    );
    
    // Roll number pattern (alphanumeric with possible special characters)
    private static final Pattern ROLL_NO_PATTERN = Pattern.compile(
        "^[A-Z]{2,4}\\d{4,6}$|^\\d{2}[A-Z]{2,4}\\d{3,4}$"
    );
    
    // Exam code pattern
    private static final Pattern EXAM_CODE_PATTERN = Pattern.compile(
        "^[A-Z]{2,4}\\d{3}-(MID|FINAL|QUIZ)$"
    );
    
    /**
     * Validate email address
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }
    
    /**
     * Validate phone number
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return true; // Phone is optional
        }
        return PHONE_PATTERN.matcher(phone.trim().replaceAll("[\\s-()]", "")).matches();
    }
    
    /**
     * Validate roll number
     */
    public static boolean isValidRollNo(String rollNo) {
        return rollNo != null && ROLL_NO_PATTERN.matcher(rollNo.trim().toUpperCase()).matches();
    }
    
    /**
     * Validate exam code
     */
    public static boolean isValidExamCode(String examCode) {
        return examCode != null && EXAM_CODE_PATTERN.matcher(examCode.trim().toUpperCase()).matches();
    }
    
    /**
     * Validate name (letters, spaces, and some special characters)
     */
    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        String trimmed = name.trim();
        return trimmed.length() >= 2 && 
               trimmed.length() <= 100 && 
               trimmed.matches("^[a-zA-Z\\s.',-]+$");
    }
    
    /**
     * Validate department name
     */
    public static boolean isValidDepartment(String department) {
        if (department == null || department.trim().isEmpty()) {
            return false;
        }
        String trimmed = department.trim();
        return trimmed.length() >= 2 && 
               trimmed.length() <= 100 && 
               trimmed.matches("^[a-zA-Z\\s&-]+$");
    }
    
    /**
     * Validate year (1-6 for undergraduate/graduate programs)
     */
    public static boolean isValidYear(int year) {
        return year >= 1 && year <= 6;
    }
    
    /**
     * Validate semester (1-8)
     */
    public static boolean isValidSemester(int semester) {
        return semester >= 1 && semester <= 8;
    }
    
    /**
     * Validate marks (0-1000)
     */
    public static boolean isValidMarks(int marks) {
        return marks >= 0 && marks <= 1000;
    }
    
    /**
     * Validate duration in minutes (5 minutes to 8 hours)
     */
    public static boolean isValidDuration(int duration) {
        return duration >= 5 && duration <= 480;
    }
    
    /**
     * Validate capacity (1-1000)
     */
    public static boolean isValidCapacity(int capacity) {
        return capacity >= 1 && capacity <= 1000;
    }
    
    /**
     * Validate rows and columns (1-50)
     */
    public static boolean isValidRowsColumns(int value) {
        return value >= 1 && value <= 50;
    }
    
    /**
     * Validate floor number (-5 to 50)
     */
    public static boolean isValidFloor(int floor) {
        return floor >= -5 && floor <= 50;
    }
    
    /**
     * Validate subject name
     */
    public static boolean isValidSubject(String subject) {
        if (subject == null || subject.trim().isEmpty()) {
            return false;
        }
        String trimmed = subject.trim();
        return trimmed.length() >= 3 && 
               trimmed.length() <= 100 && 
               trimmed.matches("^[a-zA-Z0-9\\s&-().,]+$");
    }
    
    /**
     * Validate hall name
     */
    public static boolean isValidHallName(String hallName) {
        if (hallName == null || hallName.trim().isEmpty()) {
            return false;
        }
        String trimmed = hallName.trim();
        return trimmed.length() >= 2 && 
               trimmed.length() <= 100 && 
               trimmed.matches("^[a-zA-Z0-9\\s-]+$");
    }
    
    /**
     * Validate building name
     */
    public static boolean isValidBuildingName(String building) {
        if (building == null || building.trim().isEmpty()) {
            return true; // Building is optional
        }
        String trimmed = building.trim();
        return trimmed.length() >= 2 && 
               trimmed.length() <= 100 && 
               trimmed.matches("^[a-zA-Z0-9\\s-]+$");
    }
    
    /**
     * Sanitize string input
     */
    public static String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }
        return input.trim()
                   .replaceAll("<", "&lt;")
                   .replaceAll(">", "&gt;")
                   .replaceAll("\"", "&quot;")
                   .replaceAll("'", "&#x27;")
                   .replaceAll("/", "&#x2F;");
    }
    
    /**
     * Check if string is null or empty
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    /**
     * Check if string is not null and not empty
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
    
    /**
     * Validate string length
     */
    public static boolean isValidLength(String str, int minLength, int maxLength) {
        if (str == null) {
            return minLength == 0;
        }
        int length = str.trim().length();
        return length >= minLength && length <= maxLength;
    }
    
    /**
     * Validate positive integer
     */
    public static boolean isPositiveInteger(String str) {
        try {
            int value = Integer.parseInt(str);
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Validate non-negative integer
     */
    public static boolean isNonNegativeInteger(String str) {
        try {
            int value = Integer.parseInt(str);
            return value >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Format roll number to standard format
     */
    public static String formatRollNo(String rollNo) {
        if (rollNo == null) {
            return null;
        }
        return rollNo.trim().toUpperCase();
    }
    
    /**
     * Format exam code to standard format
     */
    public static String formatExamCode(String examCode) {
        if (examCode == null) {
            return null;
        }
        return examCode.trim().toUpperCase();
    }
    
    /**
     * Format name to proper case
     */
    public static String formatName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return name;
        }
        
        String[] words = name.trim().toLowerCase().split("\\s+");
        StringBuilder formatted = new StringBuilder();
        
        for (String word : words) {
            if (word.length() > 0) {
                formatted.append(Character.toUpperCase(word.charAt(0)));
                if (word.length() > 1) {
                    formatted.append(word.substring(1));
                }
                formatted.append(" ");
            }
        }
        
        return formatted.toString().trim();
    }
}