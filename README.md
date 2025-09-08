# 🎓 Exam Seating Management System

A comprehensive full-stack web application for managing exam seating arrangements in educational institutions. Built with Java Servlets, JSP, MySQL, and modern frontend technologies.

[![Java](https://img.shields.io/badge/Java-11+-orange.svg)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.6+-blue.svg)](https://maven.apache.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

## 📋 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Prerequisites](#-prerequisites)
- [Installation](#-installation)
- [Configuration](#-configuration)
- [Running the Application](#-running-the-application)
- [Usage](#-usage)
- [API Documentation](#-api-documentation)
- [Database Schema](#-database-schema)
- [Security Features](#-security-features)
- [Screenshots](#-screenshots)
- [Contributing](#-contributing)
- [Troubleshooting](#-troubleshooting)
- [License](#-license)
- [Support](#-support)

## 🚀 Features

### Core Functionality
- **User Authentication**: Secure login/registration for Students and Teachers/Admins
- **Role-based Access Control**: Separate dashboards and permissions for different user types
- **Student Management**: CRUD operations for student records with advanced search
- **Exam Management**: Create, edit, and manage exams with scheduling
- **Seating Arrangement**: Automated generation with spacing and distribution algorithms
- **Real-time Updates**: Dynamic content updates using AJAX/Fetch API

### Student Features
- ✅ View upcoming exams and schedules
- ✅ Check assigned seating arrangements
- ✅ Update personal profile information
- ✅ Change password securely
- ✅ Real-time seating updates
- ✅ Exam history tracking

### Teacher/Admin Features
- ✅ Comprehensive student management
- ✅ Exam creation and scheduling
- ✅ Automated seating plan generation
- ✅ Seating arrangement validation
- ✅ Export seating plans (Future: PDF/Excel)
- ✅ Manage exam halls and capacities
- ✅ Dashboard with analytics

### Technical Features
- **Responsive Design**: Modern, mobile-friendly UI
- **Session Management**: Secure user sessions with timeout
- **Form Validation**: Client and server-side validation
- **Error Handling**: Custom error pages and user-friendly messages
- **Search & Filter**: Advanced search and filtering capabilities
- **Print Support**: Print-friendly seating arrangements

## 🛠️ Tech Stack

### Backend
- **Java 11+**: Core application logic
- **Servlets & JSP**: Web framework
- **JDBC**: Database connectivity with connection pooling
- **MySQL**: Relational database management
- **Apache Tomcat**: Application server
- **Maven**: Build and dependency management

### Frontend
- **HTML5**: Semantic markup
- **CSS3**: Modern styling with Grid and Flexbox
- **JavaScript (ES6+)**: Dynamic functionality
- **AJAX/Fetch API**: Asynchronous data loading
- **Font Awesome**: Icon library

### Libraries & Dependencies
- **Gson**: JSON processing
- **MySQL Connector**: Database driver
- **SLF4J + Logback**: Logging framework
- **JUnit 5**: Testing framework

## 📁 Project Structure

```
Exam-Seating-Management-System/
├── 📁 src/
│   ├── 📁 main/
│   │   ├── 📁 java/com/examseating/
│   │   │   ├── 📁 model/              # Data models (User, Student, Teacher, Exam, etc.)
│   │   │   ├── 📁 dao/                # Data Access Objects
│   │   │   ├── 📁 servlet/            # HTTP request handlers
│   │   │   ├── 📁 util/               # Utility classes (Database, Password, Validation)
│   │   │   └── 📁 filter/             # Authentication and security filters
│   │   ├── 📁 resources/
│   │   │   ├── db.properties          # Database configuration
│   │   │   └── logback.xml            # Logging configuration
│   │   └── 📁 webapp/
│   │       ├── 📁 WEB-INF/
│   │       │   └── web.xml            # Web application configuration
│   │       ├── 📁 css/
│   │       │   ├── style.css          # Main stylesheet
│   │       │   ├── login.css          # Login page styles
│   │       │   └── dashboard.css      # Dashboard styles
│   │       ├── 📁 js/
│   │       │   ├── main.js            # Core JavaScript utilities
│   │       │   └── student-dashboard.js # Dashboard functionality
│   │       ├── 📁 images/             # Static images
│   │       ├── index.html             # Landing page
│   │       ├── student-login.html     # Student authentication
│   │       ├── teacher-login.html     # Teacher authentication
│   │       ├── student-dashboard.html # Student interface
│   │       └── teacher-dashboard.html # Teacher interface
│   └── 📁 test/                       # Unit and integration tests
├── 📁 database/
│   ├── schema.sql                     # Complete database schema
│   └── sample-data.sql                # Sample data for testing
├── 📁 scripts/
│   ├── setup.sh                       # Unix/Linux setup script
│   ├── setup.bat                      # Windows setup script
│   └── setup_database.py             # Database setup utility
├── pom.xml                            # Maven configuration
├── README.md                          # This file
└── LICENSE                            # MIT License
```

## 🔧 Prerequisites

Before you begin, ensure you have the following installed:

- **Java Development Kit (JDK) 11 or higher**
  - Download: [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://adoptium.net/)
- **Apache Maven 3.6 or higher**
  - Download: [Apache Maven](https://maven.apache.org/download.cgi)
- **MySQL 8.0 or higher**
  - Download: [MySQL Community Server](https://dev.mysql.com/downloads/mysql/)
- **Git** (for cloning the repository)
  - Download: [Git](https://git-scm.com/downloads)

### Optional but Recommended
- **Apache Tomcat 9.0+** (or use Maven Tomcat plugin)
- **MySQL Workbench** (for database management)
- **IntelliJ IDEA** or **Eclipse** (for development)

## 💻 Installation

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/exam-seating-management-system.git
cd exam-seating-management-system
```

### 2. Automated Setup (Recommended)

#### For Unix/Linux/macOS:
```bash
chmod +x setup.sh
./setup.sh
```

#### For Windows:
```cmd
setup.bat
```

#### Using Python Script:
```bash
python3 setup_database.py
```

### 3. Manual Installation

If automated setup fails, follow these manual steps:

#### Step 1: Database Setup
```bash
# Start MySQL service
sudo systemctl start mysql  # Linux
brew services start mysql   # macOS

# Connect to MySQL
mysql -u root -p

# Create database and run schema
CREATE DATABASE exam_seating_db;
USE exam_seating_db;
source database/schema.sql;
exit;
```

#### Step 2: Configure Database Connection
Edit `src/main/resources/db.properties`:

```properties
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/exam_seating_db?useSSL=false&serverTimezone=UTC
db.username=root
db.password=your_mysql_password
```

#### Step 3: Build the Application
```bash
mvn clean package
```

#### Step 4: Deploy and Run
```bash
# Option A: Using Maven Tomcat Plugin (Recommended for development)
mvn tomcat7:run

# Option B: Deploy to external Tomcat
cp target/exam-seating-management-1.0.0.war $TOMCAT_HOME/webapps/
$TOMCAT_HOME/bin/startup.sh
```

## ⚙️ Configuration

### Database Configuration
The application uses `db.properties` for database configuration:

```properties
# Database Connection
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/exam_seating_db
db.username=your_username
db.password=your_password

# Connection Pool Settings
db.initialSize=5
db.maxActive=20
db.maxIdle=10
db.minIdle=5
db.maxWait=60000

# Application Settings
app.session.timeout=1800
security.password.min.length=8
```

### Environment Variables (Optional)
For production deployment, you can use environment variables:

```bash
export DB_USERNAME=your_username
export DB_PASSWORD=your_password
export DB_URL=jdbc:mysql://your-host:3306/exam_seating_db
```

## 🏃‍♂️ Running the Application

### Development Mode
```bash
mvn tomcat7:run
```
Access: `http://localhost:8080`

### Production Mode
1. Build WAR file: `mvn clean package`
2. Deploy to Tomcat: `cp target/*.war $TOMCAT_HOME/webapps/`
3. Start Tomcat: `$TOMCAT_HOME/bin/startup.sh`
4. Access: `http://your-domain:8080/exam-seating-management-1.0.0/`

### Docker (Optional)
```bash
# Build image
docker build -t exam-seating-app .

# Run container
docker run -p 8080:8080 exam-seating-app
```

## 📖 Usage

### Demo Accounts

The system comes with pre-configured demo accounts:

#### Student Account
- **Email**: `alice.johnson@student.edu`
- **Password**: `student123`
- **Features**: View exams, check seating, update profile

#### Teacher Account
- **Email**: `john.smith@university.edu`
- **Password**: `password123`
- **Features**: Manage students, create exams, generate seating

#### Admin Account
- **Email**: `admin@university.edu`
- **Password**: `password123`
- **Features**: Full system access, user management, system settings

### User Workflows

#### For Students:
1. **Login** → Navigate to Student Dashboard
2. **View Exams** → Check upcoming examinations
3. **Check Seating** → Find your assigned seat
4. **Update Profile** → Manage personal information

#### For Teachers:
1. **Login** → Access Teacher Dashboard
2. **Manage Students** → Add/edit student records
3. **Create Exams** → Schedule new examinations
4. **Generate Seating** → Auto-create seating arrangements
5. **View Reports** → Monitor exam statistics

## 📡 API Documentation

### Authentication Endpoints

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| POST | `/auth/login` | User login | `{email, password, userType, rememberMe}` |
| POST | `/auth/logout` | User logout | - |
| GET | `/auth/status` | Check auth status | - |

### Student Endpoints

| Method | Endpoint | Description | Response |
|--------|----------|-------------|----------|
| GET | `/student/profile` | Get student profile | Student details |
| PUT | `/student/profile` | Update profile | Success/Error |
| GET | `/student/exams` | Get student exams | List of exams |
| GET | `/student/seating` | Get seating arrangements | Seating details |
| GET | `/student/stats` | Get dashboard stats | Statistics object |

### Teacher Endpoints

| Method | Endpoint | Description | Parameters |
|--------|----------|-------------|------------|
| GET | `/teacher/students` | Get all students | `page`, `limit`, `search` |
| POST | `/teacher/students` | Add new student | Student object |
| PUT | `/teacher/students/{id}` | Update student | Student object |
| DELETE | `/teacher/students/{id}` | Delete student | - |
| POST | `/teacher/exams` | Create exam | Exam object |
| POST | `/teacher/seating/{examId}` | Generate seating | Configuration |

## 🗄️ Database Schema

### Core Tables

#### Students Table
```sql
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
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

#### Teachers Table
```sql
CREATE TABLE teachers (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    department VARCHAR(100),
    phone VARCHAR(15),
    is_admin BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

#### Exams Table
```sql
CREATE TABLE exams (
    id INT PRIMARY KEY AUTO_INCREMENT,
    subject VARCHAR(100) NOT NULL,
    exam_code VARCHAR(20) UNIQUE NOT NULL,
    exam_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    duration INT NOT NULL,
    total_marks INT NOT NULL,
    status ENUM('SCHEDULED', 'ONGOING', 'COMPLETED', 'CANCELLED') DEFAULT 'SCHEDULED',
    created_by INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES teachers(id)
);
```

### Relationships
- **Students** ↔ **Seating Arrangements** (One-to-Many)
- **Exams** ↔ **Seating Arrangements** (One-to-Many)
- **Exam Halls** ↔ **Seating Arrangements** (One-to-Many)
- **Teachers** ↔ **Exams** (One-to-Many)

## 🔒 Security Features

### Authentication & Authorization
- **Password Hashing**: SHA-256 with salt
- **Session Management**: Configurable timeout (30 minutes default)
- **Role-based Access**: URL-level security constraints
- **Remember Me**: Secure cookie implementation

### Data Protection
- **SQL Injection Prevention**: Prepared statements
- **XSS Protection**: Input sanitization and encoding
- **CSRF Protection**: Token-based validation
- **Input Validation**: Server-side validation for all inputs

### Security Headers
```java
// Example security configuration
response.setHeader("X-Content-Type-Options", "nosniff");
response.setHeader("X-Frame-Options", "DENY");
response.setHeader("X-XSS-Protection", "1; mode=block");
```

## 📸 Screenshots

### Landing Page
![Landing Page](docs/images/landing-page.png)
*Modern, responsive landing page with feature showcase*

### Student Login
![Student Login](docs/images/student-login.png)
*Clean, user-friendly login interface*

### Student Dashboard
![Student Dashboard](docs/images/student-dashboard.png)
*Comprehensive dashboard with statistics and quick access*

### Seating Arrangement
![Seating View](docs/images/seating-arrangement.png)
*Visual seating arrangement display*

## 🤝 Contributing

We welcome contributions from the community! Here's how you can help:

### Getting Started
1. Fork the repository
2. Create a feature branch: `git checkout -b feature/amazing-feature`
3. Make your changes
4. Add tests for new functionality
5. Commit changes: `git commit -m 'Add amazing feature'`
6. Push to branch: `git push origin feature/amazing-feature`
7. Submit a Pull Request

### Development Guidelines
- Follow Java coding conventions
- Write comprehensive tests
- Update documentation for new features
- Ensure responsive design principles
- Maintain backward compatibility

### Code Style
```bash
# Format code before committing
mvn spotless:apply
```

## 🐛 Troubleshooting

### Common Issues

#### Database Connection Failed
```bash
# Check MySQL service
sudo systemctl status mysql

# Verify credentials
mysql -u root -p -e "SELECT 1;"

# Check database exists
mysql -u root -p -e "SHOW DATABASES;"
```

#### Port Already in Use
```bash
# Find process using port 8080
lsof -i :8080

# Kill the process
kill -9 <PID>

# Or use different port
mvn tomcat7:run -Dmaven.tomcat.port=8081
```

#### Maven Build Fails
```bash
# Clean and rebuild
mvn clean install -U

# Skip tests if necessary
mvn clean package -DskipTests

# Check Java version
java -version
mvn -version
```

#### Application Startup Issues
```bash
# Check logs
tail -f logs/application.log

# Verify Tomcat deployment
ls $TOMCAT_HOME/webapps/

# Check application context
curl http://localhost:8080/manager/text/list
```

### Log Files
- **Application Logs**: `logs/application.log`
- **Tomcat Logs**: `$TOMCAT_HOME/logs/catalina.out`
- **MySQL Logs**: `/var/log/mysql/error.log`

### Performance Tuning
```bash
# JVM options for production
export JAVA_OPTS="-Xms512m -Xmx2g -XX:+UseG1GC"

# Database connection pool tuning
# Edit db.properties:
db.maxActive=50
db.maxIdle=25
```

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

```
MIT License

Copyright (c) 2024 Exam Seating Management System

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
```



## 🚧 Roadmap

### Version 2.0 (Planned Features)
- [ ] **Advanced Seating Algorithm**: AI-powered optimal seating
- [ ] **PDF Export**: Generate printable seating charts
- [ ] **Email Notifications**: Automated exam reminders
- [ ] **Mobile App**: Native Android/iOS applications
- [ ] **API Integration**: REST API for external systems
- [ ] **Multi-language**: Internationalization support
- [ ] **Advanced Analytics**: Comprehensive reporting dashboard
- [ ] **Backup System**: Automated backup and recovery

### Version 2.1 (Future Enhancements)
- [ ] **Real-time Chat**: Communication between students and teachers
- [ ] **Calendar Integration**: Sync with Google Calendar/Outlook
- [ ] **Blockchain Verification**: Secure exam integrity
- [ ] **Machine Learning**: Predictive analytics for seating optimization

## 👥 Team

### Core Contributors
- **Divakar Mungamuru** - *Project Lead & Full-stack Developer*
- **Heerehal Dheeraj Kishore** - *Frontend Developer*
- **Esukupalli Vinesh Reddy** - *Backend Developer*
- **Onteru Venkateswarlu** - *Database Administrator*

### Acknowledgments
- Thanks to all contributors who have helped shape this project
- Special thanks to the open-source community for tools and libraries
- Educational institutions that provided feedback and requirements

</div>
