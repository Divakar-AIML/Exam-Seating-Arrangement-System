// Main JavaScript file for Exam Seating Management System

// Global variables
let currentUser = null;
let sessionTimeout = null;

// Initialize when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    initializeApp();
    setupGlobalEventListeners();
    checkAuthStatus();
});

/**
 * Initialize the application
 */
function initializeApp() {
    console.log('Initializing Exam Seating Management System...');
    
    // Set up CSRF protection
    setupCSRFProtection();
    
    // Initialize session management
    initializeSessionManagement();
    
    // Set up loading overlay
    setupLoadingOverlay();
    
    console.log('Application initialized successfully');
}

/**
 * Setup global event listeners
 */
function setupGlobalEventListeners() {
    // Handle back button
    window.addEventListener('popstate', function(event) {
        if (event.state && event.state.page) {
            loadPage(event.state.page, false);
        }
    });
    
    // Handle online/offline status
    window.addEventListener('online', function() {
        showNotification('Connection restored', 'success');
    });
    
    window.addEventListener('offline', function() {
        showNotification('Connection lost. Some features may not work.', 'warning');
    });
    
    // Handle page visibility changes
    document.addEventListener('visibilitychange', function() {
        if (!document.hidden) {
            refreshAuthStatus();
        }
    });
}

/**
 * Check authentication status
 */
function checkAuthStatus() {
    fetch('/auth/status', {
        method: 'GET',
        credentials: 'include'
    })
    .then(response => response.json())
    .then(data => {
        if (data.authenticated) {
            currentUser = data.user;
            startSessionTimeout();
        } else {
            currentUser = null;
        }
    })
    .catch(error => {
        console.error('Error checking auth status:', error);
    });
}

/**
 * Refresh authentication status
 */
function refreshAuthStatus() {
    if (currentUser) {
        fetch('/auth/refresh', {
            method: 'POST',
            credentials: 'include'
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                resetSessionTimeout();
            } else {
                handleSessionExpiry();
            }
        })
        .catch(error => {
            console.error('Error refreshing session:', error);
        });
    }
}

/**
 * Setup CSRF protection
 */
function setupCSRFProtection() {
    // Get CSRF token from meta tag or cookie
    let csrfToken = document.querySelector('meta[name="csrf-token"]');
    if (csrfToken) {
        // Add CSRF token to all AJAX requests
        const originalFetch = window.fetch;
        window.fetch = function(url, options = {}) {
            if (options.method && options.method.toUpperCase() !== 'GET') {
                options.headers = options.headers || {};
                options.headers['X-CSRF-Token'] = csrfToken.getAttribute('content');
            }
            return originalFetch(url, options);
        };
    }
}

/**
 * Initialize session management
 */
function initializeSessionManagement() {
    // Session timeout warning (25 minutes)
    const SESSION_WARNING_TIME = 25 * 60 * 1000;
    // Session timeout (30 minutes)
    const SESSION_TIMEOUT = 30 * 60 * 1000;
    
    let warningShown = false;
    
    function resetActivity() {
        if (sessionTimeout) {
            clearTimeout(sessionTimeout);
        }
        warningShown = false;
        
        if (currentUser) {
            sessionTimeout = setTimeout(() => {
                if (!warningShown) {
                    showSessionWarning();
                    warningShown = true;
                    
                    // Final timeout
                    setTimeout(() => {
                        handleSessionExpiry();
                    }, 5 * 60 * 1000); // 5 minutes after warning
                }
            }, SESSION_WARNING_TIME);
        }
    }
    
    // Reset activity on user interaction
    ['mousedown', 'mousemove', 'keypress', 'scroll', 'touchstart', 'click'].forEach(event => {
        document.addEventListener(event, resetActivity, true);
    });
    
    window.resetSessionTimeout = resetActivity;
}

/**
 * Start session timeout
 */
function startSessionTimeout() {
    if (window.resetSessionTimeout) {
        window.resetSessionTimeout();
    }
}

/**
 * Show session warning
 */
function showSessionWarning() {
    if (confirm('Your session will expire in 5 minutes. Do you want to continue your session?')) {
        refreshAuthStatus();
    }
}

/**
 * Handle session expiry
 */
function handleSessionExpiry() {
    currentUser = null;
    showNotification('Your session has expired. Please login again.', 'warning');
    
    // Redirect to appropriate login page after delay
    setTimeout(() => {
        const currentPath = window.location.pathname;
        if (currentPath.includes('student') || currentPath.includes('Student')) {
            window.location.href = '/student-login.html';
        } else if (currentPath.includes('teacher') || currentPath.includes('Teacher')) {
            window.location.href = '/teacher-login.html';
        } else {
            window.location.href = '/index.html';
        }
    }, 2000);
}

/**
 * Setup loading overlay
 */
function setupLoadingOverlay() {
    if (!document.getElementById('global-loading')) {
        const loadingOverlay = document.createElement('div');
        loadingOverlay.id = 'global-loading';
        loadingOverlay.className = 'loading-overlay';
        loadingOverlay.innerHTML = `
            <div class="loading-spinner">
                <i class="fas fa-spinner fa-spin"></i>
                <p>Loading...</p>
            </div>
        `;
        document.body.appendChild(loadingOverlay);
    }
}

/**
 * Show global loading
 */
function showLoading(message = 'Loading...') {
    const loadingOverlay = document.getElementById('global-loading') || 
                          document.getElementById('loading-overlay');
    
    if (loadingOverlay) {
        const messageElement = loadingOverlay.querySelector('p');
        if (messageElement) {
            messageElement.textContent = message;
        }
        loadingOverlay.classList.add('show');
    }
}

/**
 * Hide global loading
 */
function hideLoading() {
    const loadingOverlay = document.getElementById('global-loading') || 
                          document.getElementById('loading-overlay');
    
    if (loadingOverlay) {
        loadingOverlay.classList.remove('show');
    }
}

/**
 * Show notification/toast
 */
function showNotification(message, type = 'info', duration = 3000) {
    // Remove existing notifications
    const existingToasts = document.querySelectorAll('.notification-toast');
    existingToasts.forEach(toast => toast.remove());
    
    // Create new toast
    const toast = document.createElement('div');
    toast.className = `notification-toast ${type}`;
    
    const icon = getIconForType(type);
    
    toast.innerHTML = `
        <div class="toast-content">
            <i class="${icon}"></i>
            <span>${message}</span>
        </div>
        <button class="toast-close" onclick="this.parentElement.remove()">
            <i class="fas fa-times"></i>
        </button>
    `;
    
    // Add styles if not already added
    if (!document.getElementById('toast-styles')) {
        const styles = document.createElement('style');
        styles.id = 'toast-styles';
        styles.textContent = `
            .notification-toast {
                position: fixed;
                top: 20px;
                right: 20px;
                background: white;
                border-radius: 8px;
                padding: 15px 20px;
                box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
                z-index: 10000;
                transform: translateX(400px);
                transition: transform 0.3s ease;
                max-width: 400px;
                border-left: 4px solid;
            }
            .notification-toast.show { transform: translateX(0); }
            .notification-toast.info { border-left-color: #17a2b8; }
            .notification-toast.success { border-left-color: #28a745; }
            .notification-toast.warning { border-left-color: #ffc107; }
            .notification-toast.error { border-left-color: #dc3545; }
            .notification-toast .toast-content {
                display: flex;
                align-items: center;
            }
            .notification-toast .toast-content i {
                margin-right: 10px;
                font-size: 1.1rem;
            }
            .notification-toast.info .toast-content i { color: #17a2b8; }
            .notification-toast.success .toast-content i { color: #28a745; }
            .notification-toast.warning .toast-content i { color: #ffc107; }
            .notification-toast.error .toast-content i { color: #dc3545; }
            .notification-toast .toast-close {
                position: absolute;
                top: 10px;
                right: 10px;
                background: none;
                border: none;
                color: #999;
                cursor: pointer;
                font-size: 0.8rem;
            }
            @media (max-width: 480px) {
                .notification-toast {
                    left: 20px;
                    right: 20px;
                    max-width: none;
                    transform: translateY(-100px);
                }
                .notification-toast.show { transform: translateY(0); }
            }
        `;
        document.head.appendChild(styles);
    }
    
    document.body.appendChild(toast);
    
    // Show toast
    setTimeout(() => {
        toast.classList.add('show');
    }, 100);
    
    // Auto remove
    setTimeout(() => {
        toast.remove();
    }, duration);
}

/**
 * Get icon for notification type
 */
function getIconForType(type) {
    switch (type) {
        case 'success':
            return 'fas fa-check-circle';
        case 'error':
            return 'fas fa-exclamation-circle';
        case 'warning':
            return 'fas fa-exclamation-triangle';
        default:
            return 'fas fa-info-circle';
    }
}

/**
 * Make API request with error handling
 */
async function apiRequest(url, options = {}) {
    showLoading();
    
    try {
        const response = await fetch(url, {
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
                ...options.headers
            },
            ...options
        });
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const data = await response.json();
        hideLoading();
        return data;
        
    } catch (error) {
        hideLoading();
        console.error('API Request failed:', error);
        showNotification('Request failed. Please try again.', 'error');
        throw error;
    }
}

/**
 * Format date for display
 */
function formatDate(dateString, includeTime = false) {
    const date = new Date(dateString);
    const options = {
        year: 'numeric',
        month: 'short',
        day: 'numeric'
    };
    
    if (includeTime) {
        options.hour = '2-digit';
        options.minute = '2-digit';
    }
    
    return date.toLocaleDateString('en-US', options);
}

/**
 * Format time for display
 */
function formatTime(timeString) {
    const [hours, minutes] = timeString.split(':');
    const time = new Date();
    time.setHours(parseInt(hours), parseInt(minutes));
    
    return time.toLocaleTimeString('en-US', {
        hour: '2-digit',
        minute: '2-digit',
        hour12: true
    });
}

/**
 * Validate email format
 */
function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

/**
 * Sanitize HTML input
 */
function sanitizeHtml(html) {
    const div = document.createElement('div');
    div.textContent = html;
    return div.innerHTML;
}

/**
 * Debounce function calls
 */
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

/**
 * Generate random ID
 */
function generateId() {
    return Date.now().toString(36) + Math.random().toString(36).substr(2);
}

/**
 * Export functions for use in other files
 */
window.ExamSeatingApp = {
    showLoading,
    hideLoading,
    showNotification,
    apiRequest,
    formatDate,
    formatTime,
    isValidEmail,
    sanitizeHtml,
    debounce,
    generateId,
    currentUser: () => currentUser,
    refreshAuthStatus
};

// Console welcome message
console.log('%c🎓 Exam Seating Management System', 'color: #2c5aa0; font-size: 18px; font-weight: bold;');
console.log('%cSystem initialized successfully', 'color: #28a745;');