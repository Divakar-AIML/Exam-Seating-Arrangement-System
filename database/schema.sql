-- Exam Seating Management System Database Schema
-- This script creates all necessary tables and initial data

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS exam_seating_db;
USE exam_seating_db;

-- Set character set and collation
ALTER DATABASE exam_seating_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Drop tables if they exist (for clean setup)
DROP TABLE IF EXISTS seating_arrangements;
DROP TABLE IF EXISTS exam_hall_assignments;
DROP TABLE IF EXISTS exams;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS teachers;
DROP TABLE IF EXISTS exam_halls;

-- Teachers table
CREATE TABLE teachers (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    department VARCHAR(100),
    phone VARCHAR(15),
    is_admin BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_email (email),
    INDEX idx_department (department)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Students table
CREATE TABLE students (
    id INT PRIMARY KEY AUTO_INCREMENT,
    roll_no VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    department VARCHAR(100) NOT NULL,
    year INT NOT NULL,
    semester INT NOT NULL,
    phone VARCHAR(15),
    address TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_roll_no (roll_no),
    INDEX idx_email (email),
    INDEX idx_department (department),
    INDEX idx_year_semester (year, semester)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Exam halls table
CREATE TABLE exam_halls (
    id INT PRIMARY KEY AUTO_INCREMENT,
    hall_name VARCHAR(100) NOT NULL,
    capacity INT NOT NULL,
    rows INT NOT NULL,
    columns INT NOT NULL,
    building VARCHAR(100),
    floor INT,
    facilities TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_hall_name (hall_name),
    INDEX idx_capacity (capacity)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Exams table
CREATE TABLE exams (
    id INT PRIMARY KEY AUTO_INCREMENT,
    subject VARCHAR(100) NOT NULL,
    exam_code VARCHAR(20) UNIQUE NOT NULL,
    exam_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    duration INT NOT NULL, -- in minutes
    total_marks INT NOT NULL,
    min_marks INT DEFAULT 35,
    instructions TEXT,
    status ENUM('SCHEDULED', 'ONGOING', 'COMPLETED', 'CANCELLED') DEFAULT 'SCHEDULED',
    created_by INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (created_by) REFERENCES teachers(id) ON DELETE CASCADE,
    INDEX idx_exam_date (exam_date),
    INDEX idx_subject (subject),
    INDEX idx_status (status),
    INDEX idx_exam_code (exam_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Exam hall assignments table
CREATE TABLE exam_hall_assignments (
    id INT PRIMARY KEY AUTO_INCREMENT,
    exam_id INT NOT NULL,
    hall_id INT NOT NULL,
    assigned_capacity INT NOT NULL,
    supervisor_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (exam_id) REFERENCES exams(id) ON DELETE CASCADE,
    FOREIGN KEY (hall_id) REFERENCES exam_halls(id) ON DELETE CASCADE,
    FOREIGN KEY (supervisor_id) REFERENCES teachers(id) ON DELETE SET NULL,
    UNIQUE KEY unique_exam_hall (exam_id, hall_id),
    INDEX idx_exam_id (exam_id),
    INDEX idx_hall_id (hall_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Seating arrangements table
CREATE TABLE seating_arrangements (
    id INT PRIMARY KEY AUTO_INCREMENT,
    exam_id INT NOT NULL,
    student_id INT NOT NULL,
    hall_id INT NOT NULL,
    seat_number INT NOT NULL,
    seat_row INT NOT NULL,
    seat_column INT NOT NULL,
    status ENUM('ASSIGNED', 'PRESENT', 'ABSENT') DEFAULT 'ASSIGNED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (exam_id) REFERENCES exams(id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (hall_id) REFERENCES exam_halls(id) ON DELETE CASCADE,
    UNIQUE KEY unique_seat (exam_id, hall_id, seat_number),
    UNIQUE KEY unique_student_exam (exam_id, student_id),
    INDEX idx_exam_student (exam_id, student_id),
    INDEX idx_hall_seat (hall_id, seat_row, seat_column)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert sample data

-- Sample teachers (password is 'password123' hashed)
INSERT INTO teachers (name, email, password, department, phone, is_admin) VALUES
('Admin User', 'admin@university.edu', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Administration', '+1234567890', TRUE),
('Dr. John Smith', 'john.smith@university.edu', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Computer Science', '+1234567891', FALSE),
('Dr. Jane Doe', 'jane.doe@university.edu', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Mathematics', '+1234567892', FALSE),
('Prof. Michael Johnson', 'michael.johnson@university.edu', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Physics', '+1234567893', FALSE);

-- Sample students (password is 'student123' hashed)
INSERT INTO students (roll_no, name, email, password, department, year, semester, phone) VALUES
('CS2021001', 'Alice Johnson', 'alice.johnson@student.edu', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'Computer Science', 3, 5, '+1234567901'),
('CS2021002', 'Bob Wilson', 'bob.wilson@student.edu', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'Computer Science', 3, 5, '+1234567902'),
('CS2021003', 'Carol Brown', 'carol.brown@student.edu', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'Computer Science', 3, 5, '+1234567903'),
('CS2021004', 'David Davis', 'david.davis@student.edu', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'Computer Science', 3, 5, '+1234567904'),
('CS2021005', 'Eva Martinez', 'eva.martinez@student.edu', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'Computer Science', 3, 5, '+1234567905'),
('MT2021001', 'Frank Miller', 'frank.miller@student.edu', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'Mathematics', 2, 3, '+1234567906'),
('MT2021002', 'Grace Lee', 'grace.lee@student.edu', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'Mathematics', 2, 3, '+1234567907'),
('PH2021001', 'Henry Taylor', 'henry.taylor@student.edu', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'Physics', 1, 1, '+1234567908');

-- Sample exam halls
INSERT INTO exam_halls (hall_name, capacity, rows, columns, building, floor, facilities) VALUES
('Main Hall A', 60, 10, 6, 'Academic Block A', 1, 'Air Conditioning, Projector, CCTV'),
('Main Hall B', 50, 10, 5, 'Academic Block A', 1, 'Air Conditioning, CCTV'),
('Computer Lab 1', 40, 8, 5, 'IT Block', 2, 'Computers, Air Conditioning, CCTV'),
('Physics Lab', 30, 6, 5, 'Science Block', 1, 'Lab Equipment, Air Conditioning'),
('Auditorium', 100, 20, 5, 'Main Building', 0, 'Sound System, Projector, Air Conditioning, CCTV');

-- Sample exams
INSERT INTO exams (subject, exam_code, exam_date, start_time, end_time, duration, total_marks, created_by, instructions) VALUES
('Data Structures and Algorithms', 'CS301-MID', '2024-03-15', '09:00:00', '12:00:00', 180, 100, 2, 'Bring calculator and pen. No mobile phones allowed.'),
('Calculus II', 'MT201-MID', '2024-03-16', '14:00:00', '17:00:00', 180, 100, 3, 'Scientific calculator allowed. Show all working.'),
('Quantum Physics', 'PH101-FINAL', '2024-03-17', '09:00:00', '11:00:00', 120, 80, 4, 'Formula sheet provided. No calculator needed.');

-- Sample hall assignments
INSERT INTO exam_hall_assignments (exam_id, hall_id, assigned_capacity, supervisor_id) VALUES
(1, 1, 50, 2),
(1, 2, 40, 3),
(2, 3, 35, 3),
(3, 4, 25, 4);

-- Sample seating arrangements
INSERT INTO seating_arrangements (exam_id, student_id, hall_id, seat_number, seat_row, seat_column) VALUES
-- Data Structures exam seating
(1, 1, 1, 1, 1, 1),
(1, 2, 1, 3, 1, 3),
(1, 3, 1, 5, 1, 5),
(1, 4, 2, 1, 1, 1),
(1, 5, 2, 3, 1, 3),
-- Calculus exam seating
(2, 6, 3, 1, 1, 1),
(2, 7, 3, 3, 1, 3),
-- Physics exam seating
(3, 8, 4, 1, 1, 1);

-- Create views for common queries

-- View for student exam details
CREATE VIEW student_exam_view AS
SELECT 
    s.id as student_id,
    s.roll_no,
    s.name as student_name,
    s.department,
    e.id as exam_id,
    e.subject,
    e.exam_code,
    e.exam_date,
    e.start_time,
    e.end_time,
    sa.hall_id,
    eh.hall_name,
    sa.seat_number,
    sa.seat_row,
    sa.seat_column,
    sa.status
FROM students s
JOIN seating_arrangements sa ON s.id = sa.student_id
JOIN exams e ON sa.exam_id = e.id
JOIN exam_halls eh ON sa.hall_id = eh.id
WHERE s.is_active = TRUE AND e.status != 'CANCELLED';

-- View for hall occupancy
CREATE VIEW hall_occupancy_view AS
SELECT 
    eh.id as hall_id,
    eh.hall_name,
    eh.capacity,
    COUNT(sa.id) as occupied_seats,
    (eh.capacity - COUNT(sa.id)) as available_seats,
    e.id as exam_id,
    e.subject,
    e.exam_date
FROM exam_halls eh
LEFT JOIN seating_arrangements sa ON eh.id = sa.hall_id
LEFT JOIN exams e ON sa.exam_id = e.id
GROUP BY eh.id, eh.hall_name, eh.capacity, e.id, e.subject, e.exam_date;

-- Create indexes for better performance
CREATE INDEX idx_students_dept_year ON students(department, year);
CREATE INDEX idx_exams_date_status ON exams(exam_date, status);
CREATE INDEX idx_seating_exam_hall ON seating_arrangements(exam_id, hall_id);

-- Create stored procedures

DELIMITER //

-- Procedure to generate seating arrangement
CREATE PROCEDURE GenerateSeatingArrangement(
    IN exam_id INT,
    IN hall_id INT,
    OUT result VARCHAR(255)
)
BEGIN
    DECLARE student_count INT;
    DECLARE hall_capacity INT;
    DECLARE existing_count INT;
    
    -- Check if seating already exists
    SELECT COUNT(*) INTO existing_count
    FROM seating_arrangements 
    WHERE exam_id = exam_id AND hall_id = hall_id;
    
    IF existing_count > 0 THEN
        SET result = 'Seating already exists for this exam and hall';
    ELSE
        -- Get hall capacity
        SELECT capacity INTO hall_capacity
        FROM exam_halls 
        WHERE id = hall_id;
        
        -- This is a simplified version - actual seating logic would be more complex
        SET result = CONCAT('Seating arrangement generated for hall capacity: ', hall_capacity);
    END IF;
END//

DELIMITER ;

-- Create triggers for audit logging

DELIMITER //

-- Trigger to update exam status
CREATE TRIGGER update_exam_status
    BEFORE UPDATE ON exams
    FOR EACH ROW
BEGIN
    -- Auto-update status based on date and time
    IF NEW.exam_date < CURDATE() THEN
        SET NEW.status = 'COMPLETED';
    ELSEIF NEW.exam_date = CURDATE() AND NEW.start_time <= CURTIME() THEN
        SET NEW.status = 'ONGOING';
    END IF;
END//

DELIMITER ;

-- Grant permissions (adjust as needed for your setup)
-- GRANT ALL PRIVILEGES ON exam_seating_db.* TO 'exam_user'@'localhost' IDENTIFIED BY 'exam_password';
-- FLUSH PRIVILEGES;

-- Display summary
SELECT 'Database setup completed successfully!' as Status;
SELECT COUNT(*) as Teachers FROM teachers;
SELECT COUNT(*) as Students FROM students;
SELECT COUNT(*) as Exam_Halls FROM exam_halls;
SELECT COUNT(*) as Exams FROM exams;
SELECT COUNT(*) as Seating_Arrangements FROM seating_arrangements;