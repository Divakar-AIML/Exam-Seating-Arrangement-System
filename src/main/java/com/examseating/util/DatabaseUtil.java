package com.examseating.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Database utility class for connection management
 */
public class DatabaseUtil {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseUtil.class);
    private static Properties dbProperties;
    
    static {
        loadDatabaseProperties();
        loadDatabaseDriver();
    }
    
    /**
     * Load database properties from db.properties file
     */
    private static void loadDatabaseProperties() {
        dbProperties = new Properties();
        try (InputStream input = DatabaseUtil.class.getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (input == null) {
                logger.error("Database properties file not found");
                throw new RuntimeException("Database configuration file not found");
            }
            dbProperties.load(input);
            logger.info("Database properties loaded successfully");
        } catch (IOException e) {
            logger.error("Error loading database properties", e);
            throw new RuntimeException("Failed to load database configuration", e);
        }
    }
    
    /**
     * Load database driver
     */
    private static void loadDatabaseDriver() {
        try {
            String driver = dbProperties.getProperty("db.driver", "com.mysql.cj.jdbc.Driver");
            Class.forName(driver);
            logger.info("Database driver loaded: {}", driver);
        } catch (ClassNotFoundException e) {
            logger.error("Database driver not found", e);
            throw new RuntimeException("Database driver not found", e);
        }
    }
    
    /**
     * Get database connection
     */
    public static Connection getConnection() throws SQLException {
        try {
            String url = dbProperties.getProperty("db.url");
            String username = dbProperties.getProperty("db.username");
            String password = dbProperties.getProperty("db.password");
            
            if (url == null || username == null) {
                throw new SQLException("Database connection parameters not configured");
            }
            
            Connection connection = DriverManager.getConnection(url, username, password);
            connection.setAutoCommit(true);
            
            logger.debug("Database connection established");
            return connection;
            
        } catch (SQLException e) {
            logger.error("Failed to establish database connection", e);
            throw e;
        }
    }
    
    /**
     * Close database connection safely
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                logger.debug("Database connection closed");
            } catch (SQLException e) {
                logger.error("Error closing database connection", e);
            }
        }
    }
    
    /**
     * Test database connection
     */
    public static boolean testConnection() {
        try (Connection connection = getConnection()) {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            logger.error("Database connection test failed", e);
            return false;
        }
    }
    
    /**
     * Get database property
     */
    public static String getProperty(String key) {
        return dbProperties.getProperty(key);
    }
    
    /**
     * Get database property with default value
     */
    public static String getProperty(String key, String defaultValue) {
        return dbProperties.getProperty(key, defaultValue);
    }
    
    /**
     * Get integer property
     */
    public static int getIntProperty(String key, int defaultValue) {
        String value = dbProperties.getProperty(key);
        try {
            return value != null ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            logger.warn("Invalid integer property {}: {}, using default: {}", 
                       key, value, defaultValue);
            return defaultValue;
        }
    }
    
    /**
     * Get boolean property
     */
    public static boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = dbProperties.getProperty(key);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }
    
    /**
     * Initialize database (for testing purposes)
     */
    public static void initializeDatabase() {
        logger.info("Initializing database connection pool");
        try (Connection connection = getConnection()) {
            logger.info("Database initialized successfully");
        } catch (SQLException e) {
            logger.error("Failed to initialize database", e);
            throw new RuntimeException("Database initialization failed", e);
        }
    }
}