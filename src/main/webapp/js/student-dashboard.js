// Student Dashboard JavaScript

let currentStudent = null;
let currentSection = 'dashboard';

// Initialize dashboard when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    initializeDashboard();
    setupEventListeners();
    checkAuthentication();
});

/**
 * Initialize the dashboard
 */
function initializeDashboard() {
    console.log('Initializing Student Dashboard...');
    
    // Set up navigation
    setupNavigation();
    
    // Set up modals
    setupModals();
    
    // Load initial data
    loadDashboardData();
    
    console.log('Student Dashboard initialized');
}

/**
 * Setup event listeners
 */
function setupEventListeners() {
    // Navigation clicks
    document.querySelectorAll('.nav-link').forEach(link => {
        link.addEventListener('click', handleNavigation);
    });
    
    // User menu toggle
    const userMenuBtn = document.getElementById('user-menu-btn');
    const userDropdown = document.getElementById('user-dropdown');
    
    userMenuBtn?.addEventListener('click', function(e) {
        e.stopPropagation();
        userDropdown.classList.toggle('show');
    });
    
    // Close dropdown when clicking outside
    document.addEventListener('click', function() {
        userDropdown?.classList.remove('show');
    });
    
    // Logout functionality
    document.getElementById('logout-btn')?.addEventListener('click', handleLogout);
    
    // Profile form submission
    document.getElementById('profile-form')?.addEventListener('submit', handleProfileUpdate);
    
    // Change password
    document.getElementById('change-password-btn')?.addEventListener('click', showChangePasswordModal);
    document.getElementById('change-password-form')?.addEventListener('submit', handlePasswordChange);
    
    // Search and filter
    document.getElementById('exam-search')?.addEventListener('input', debounce(handleExamSearch, 300));
    document.getElementById('exam-status-filter')?.addEventListener('change', handleExamFilter);
    
    // View all buttons
    document.querySelectorAll('.btn-view-all').forEach(btn => {
        btn.addEventListener('click', function() {
            const section = this.getAttribute('data-section');
            if (section) {
                showSection(section);
            }
        });
    });
}

/**
 * Setup navigation
 */
function setupNavigation() {
    // Handle back/forward browser navigation
    window.addEventListener('popstate', function(event) {
        if (event.state && event.state.section) {
            showSection(event.state.section, false);
        }
    });
}

/**
 * Setup modal functionality
 */
function setupModals() {
    const changePasswordModal = document.getElementById('change-password-modal');
    const closeModalBtns = document.querySelectorAll('.modal-close, #password-cancel-btn');
    
    closeModalBtns.forEach(btn => {
        btn.addEventListener('click', function() {
            hideModal(changePasswordModal);
        });
    });
    
    // Close modal when clicking outside
    changePasswordModal?.addEventListener('click', function(e) {
        if (e.target === this) {
            hideModal(this);
        }
    });
}

/**
 * Check user authentication
 */
function checkAuthentication() {
    ExamSeatingApp.showLoading('Checking authentication...');
    
    fetch('/auth/status')
        .then(response => response.json())
        .then(data => {
            ExamSeatingApp.hideLoading();
            
            if (!data.authenticated || data.userType !== 'student') {
                ExamSeatingApp.showNotification('Please login to access the dashboard', 'error');
                setTimeout(() => {
                    window.location.href = '/student-login.html';
                }, 2000);
                return;
            }
            
            // Set current user
            currentStudent = data;
            updateUserInfo(data);
            
            // Load dashboard data
            loadDashboardData();
            
        })
        .catch(error => {
            ExamSeatingApp.hideLoading();
            console.error('Authentication check failed:', error);
            ExamSeatingApp.showNotification('Authentication failed', 'error');
            
            setTimeout(() => {
                window.location.href = '/student-login.html';
            }, 2000);
        });
}

/**
 * Update user information in the UI
 */
function updateUserInfo(userData) {
    const userNameElement = document.getElementById('user-name');
    if (userNameElement) {
        userNameElement.textContent = userData.userName || 'Student';
    }
    
    // Update profile information
    if (userData.student) {
        const student = userData.student;
        
        document.getElementById('profile-name').textContent = student.name || '';
        document.getElementById('profile-email').textContent = student.email || '';
        document.getElementById('student-name').value = student.name || '';
        document.getElementById('student-email').value = student.email || '';
        document.getElementById('student-rollno').value = student.rollNo || '';
        document.getElementById('student-phone').value = student.phone || '';
        document.getElementById('student-department').value = student.department || '';
        document.getElementById('student-year-semester').value = 
            `Year ${student.year}, Semester ${student.semester}`;
        document.getElementById('student-address').value = student.address || '';
    }
}

/**
 * Handle navigation clicks
 */
function handleNavigation(e) {
    e.preventDefault();
    const section = this.getAttribute('data-section');
    if (section) {
        showSection(section);
    }
}

/**
 * Show specific section
 */
function showSection(sectionName, pushState = true) {
    // Hide all sections
    document.querySelectorAll('.content-section').forEach(section => {
        section.classList.remove('active');
    });
    
    // Show target section
    const targetSection = document.getElementById(sectionName + '-section');
    if (targetSection) {
        targetSection.classList.add('active');
    }
    
    // Update navigation
    document.querySelectorAll('.nav-link').forEach(link => {
        link.classList.remove('active');
    });
    
    const activeLink = document.querySelector(`[data-section="${sectionName}"]`);
    if (activeLink) {
        activeLink.classList.add('active');
    }
    
    // Update browser history
    if (pushState) {
        history.pushState({section: sectionName}, '', `#${sectionName}`);
    }
    
    currentSection = sectionName;
    
    // Load section-specific data
    loadSectionData(sectionName);
}

/**
 * Load dashboard data
 */
function loadDashboardData() {
    loadStudentStats();
    loadRecentActivity();
    loadUpcomingExams();
}

/**
 * Load section-specific data
 */
function loadSectionData(section) {
    switch (section) {
        case 'exams':
            loadAllExams();
            break;
        case 'seating':
            loadSeatingArrangements();
            break;
        case 'profile':
            loadProfileData();
            break;
        default:
            loadDashboardData();
            break;
    }
}

/**
 * Load student statistics
 */
function loadStudentStats() {
    fetch('/student/stats')
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                updateStatsDisplay(data.stats);
            }
        })
        .catch(error => {
            console.error('Error loading stats:', error);
            // Set default values
            updateStatsDisplay({
                upcomingExams: 0,
                completedExams: 0,
                seatingArrangements: 0,
                profileCompletion: 75
            });
        });
}

/**
 * Update stats display
 */
function updateStatsDisplay(stats) {
    animateCounter('upcoming-exams-count', stats.upcomingExams || 0);
    animateCounter('completed-exams-count', stats.completedExams || 0);
    animateCounter('seating-arrangements-count', stats.seatingArrangements || 0);
    
    const profileCompletion = stats.profileCompletion || 75;
    document.getElementById('profile-completion').textContent = profileCompletion + '%';
}

/**
 * Animate counter numbers
 */
function animateCounter(elementId, targetValue) {
    const element = document.getElementById(elementId);
    if (!element) return;
    
    const startValue = 0;
    const duration = 1000;
    const increment = targetValue / (duration / 16);
    
    let currentValue = startValue;
    const timer = setInterval(() => {
        currentValue += increment;
        if (currentValue >= targetValue) {
            currentValue = targetValue;
            clearInterval(timer);
        }
        element.textContent = Math.floor(currentValue);
    }, 16);
}

/**
 * Load recent activity
 */
function loadRecentActivity() {
    fetch('/student/activity')
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                displayRecentActivity(data.activities);
            }
        })
        .catch(error => {
            console.error('Error loading recent activity:', error);
            displayRecentActivity([]);
        });
}

/**
 * Display recent activity
 */
function displayRecentActivity(activities) {
    const container = document.getElementById('recent-activity');
    if (!container) return;
    
    if (activities.length === 0) {
        container.innerHTML = `
            <div class="activity-item">
                <div class="activity-icon">
                    <i class="fas fa-info-circle text-info"></i>
                </div>
                <div class="activity-content">
                    <p>No recent activity</p>
                    <small>Check back later for updates</small>
                </div>
            </div>
        `;
        return;
    }
    
    container.innerHTML = activities.map(activity => `
        <div class="activity-item">
            <div class="activity-icon">
                <i class="${getActivityIcon(activity.type)} ${getActivityColor(activity.type)}"></i>
            </div>
            <div class="activity-content">
                <p>${activity.description}</p>
                <small>${formatTimeAgo(activity.timestamp)}</small>
            </div>
        </div>
    `).join('');
}

/**
 * Get activity icon based on type
 */
function getActivityIcon(type) {
    switch (type) {
        case 'seating_assigned':
            return 'fas fa-chair';
        case 'exam_scheduled':
            return 'fas fa-calendar-plus';
        case 'profile_updated':
            return 'fas fa-user-edit';
        default:
            return 'fas fa-info-circle';
    }
}

/**
 * Get activity color based on type
 */
function getActivityColor(type) {
    switch (type) {
        case 'seating_assigned':
            return 'text-info';
        case 'exam_scheduled':
            return 'text-success';
        case 'profile_updated':
            return 'text-warning';
        default:
            return 'text-info';
    }
}

/**
 * Format timestamp to "time ago"
 */
function formatTimeAgo(timestamp) {
    const now = new Date();
    const past = new Date(timestamp);
    const diffInSeconds = Math.floor((now - past) / 1000);
    
    if (diffInSeconds < 60) return 'Just now';
    if (diffInSeconds < 3600) return Math.floor(diffInSeconds / 60) + ' minutes ago';
    if (diffInSeconds < 86400) return Math.floor(diffInSeconds / 3600) + ' hours ago';
    if (diffInSeconds < 2592000) return Math.floor(diffInSeconds / 86400) + ' days ago';
    
    return past.toLocaleDateString();
}

/**
 * Load upcoming exams
 */
function loadUpcomingExams() {
    fetch('/student/exams?status=upcoming&limit=5')
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                displayUpcomingExamsList(data.exams);
            }
        })
        .catch(error => {
            console.error('Error loading upcoming exams:', error);
            displayUpcomingExamsList([]);
        });
}

/**
 * Display upcoming exams list
 */
function displayUpcomingExamsList(exams) {
    const container = document.getElementById('upcoming-exams-list');
    if (!container) return;
    
    if (exams.length === 0) {
        container.innerHTML = `
            <div class="exam-item">
                <p>No upcoming exams scheduled</p>
                <small>You're all caught up!</small>
            </div>
        `;
        return;
    }
    
    container.innerHTML = exams.map(exam => `
        <div class="exam-item">
            <div class="exam-info">
                <h4>${exam.subject}</h4>
                <p>${exam.examCode}</p>
                <small>
                    <i class="fas fa-calendar"></i> ${ExamSeatingApp.formatDate(exam.examDate)} 
                    <i class="fas fa-clock"></i> ${ExamSeatingApp.formatTime(exam.startTime)}
                </small>
            </div>
            <div class="exam-status">
                <span class="status-badge ${exam.status}">${exam.statusText}</span>
            </div>
        </div>
    `).join('');
}

/**
 * Load all exams
 */
function loadAllExams() {
    ExamSeatingApp.showLoading('Loading exams...');
    
    const status = document.getElementById('exam-status-filter')?.value || 'all';
    const search = document.getElementById('exam-search')?.value || '';
    
    let url = '/student/exams';
    const params = new URLSearchParams();
    
    if (status && status !== 'all') {
        params.append('status', status);
    }
    
    if (search) {
        params.append('search', search);
    }
    
    if (params.toString()) {
        url += '?' + params.toString();
    }
    
    fetch(url)
        .then(response => response.json())
        .then(data => {
            ExamSeatingApp.hideLoading();
            if (data.success) {
                displayExamsGrid(data.exams);
            } else {
                ExamSeatingApp.showNotification('Failed to load exams', 'error');
            }
        })
        .catch(error => {
            ExamSeatingApp.hideLoading();
            console.error('Error loading exams:', error);
            ExamSeatingApp.showNotification('Error loading exams', 'error');
        });
}

/**
 * Display exams grid
 */
function displayExamsGrid(exams) {
    const container = document.getElementById('exams-grid');
    if (!container) return;
    
    if (exams.length === 0) {
        container.innerHTML = `
            <div class="no-data">
                <i class="fas fa-file-alt"></i>
                <h3>No exams found</h3>
                <p>No exams match your current filter criteria.</p>
            </div>
        `;
        return;
    }
    
    container.innerHTML = exams.map(exam => `
        <div class="exam-card">
            <div class="exam-header">
                <h3 class="exam-title">${exam.subject}</h3>
                <p class="exam-code">${exam.examCode}</p>
            </div>
            <div class="exam-details">
                <div class="exam-detail">
                    <i class="fas fa-calendar"></i>
                    <span>${ExamSeatingApp.formatDate(exam.examDate)}</span>
                </div>
                <div class="exam-detail">
                    <i class="fas fa-clock"></i>
                    <span>${ExamSeatingApp.formatTime(exam.startTime)} - ${ExamSeatingApp.formatTime(exam.endTime)}</span>
                </div>
                <div class="exam-detail">
                    <i class="fas fa-stopwatch"></i>
                    <span>${exam.duration} minutes</span>
                </div>
                <div class="exam-detail">
                    <i class="fas fa-star"></i>
                    <span>${exam.totalMarks} marks</span>
                </div>
                <div class="exam-detail">
                    <i class="fas fa-info-circle"></i>
                    <span class="exam-status ${exam.status.toLowerCase()}">${exam.statusText}</span>
                </div>
            </div>
        </div>
    `).join('');
}

/**
 * Load seating arrangements
 */
function loadSeatingArrangements() {
    ExamSeatingApp.showLoading('Loading seating arrangements...');
    
    fetch('/student/seating')
        .then(response => response.json())
        .then(data => {
            ExamSeatingApp.hideLoading();
            if (data.success) {
                displaySeatingArrangements(data.arrangements);
            } else {
                ExamSeatingApp.showNotification('Failed to load seating arrangements', 'error');
            }
        })
        .catch(error => {
            ExamSeatingApp.hideLoading();
            console.error('Error loading seating arrangements:', error);
            ExamSeatingApp.showNotification('Error loading seating arrangements', 'error');
        });
}

/**
 * Display seating arrangements
 */
function displaySeatingArrangements(arrangements) {
    const container = document.getElementById('seating-arrangements');
    if (!container) return;
    
    if (arrangements.length === 0) {
        container.innerHTML = `
            <div class="no-data">
                <i class="fas fa-chair"></i>
                <h3>No seating arrangements</h3>
                <p>Your seating arrangements will appear here once they are assigned.</p>
            </div>
        `;
        return;
    }
    
    container.innerHTML = arrangements.map(arrangement => `
        <div class="seating-card">
            <div class="seating-header">
                <div class="seating-exam">
                    <h3>${arrangement.examSubject}</h3>
                    <p class="exam-date">${ExamSeatingApp.formatDate(arrangement.examDate)} at ${ExamSeatingApp.formatTime(arrangement.startTime)}</p>
                </div>
                <span class="exam-status ${arrangement.examStatus}">${arrangement.examStatusText}</span>
            </div>
            <div class="seating-info">
                <div class="seating-details">
                    <div class="seating-detail">
                        <i class="fas fa-building"></i>
                        <span>${arrangement.hallName}</span>
                    </div>
                    <div class="seating-detail">
                        <i class="fas fa-chair"></i>
                        <span>Seat ${arrangement.seatNumber}</span>
                    </div>
                    <div class="seating-detail">
                        <i class="fas fa-map-marker-alt"></i>
                        <span>Row ${arrangement.seatRow}, Column ${arrangement.seatColumn}</span>
                    </div>
                    <div class="seating-detail">
                        <i class="fas fa-info-circle"></i>
                        <span class="status-${arrangement.status.toLowerCase()}">${arrangement.statusText}</span>
                    </div>
                </div>
            </div>
        </div>
    `).join('');
}

/**
 * Load profile data
 */
function loadProfileData() {
    fetch('/student/profile')
        .then(response => response.json())
        .then(data => {
            if (data.success && data.student) {
                updateProfileForm(data.student);
            }
        })
        .catch(error => {
            console.error('Error loading profile data:', error);
        });
}

/**
 * Update profile form with data
 */
function updateProfileForm(student) {
    document.getElementById('student-name').value = student.name || '';
    document.getElementById('student-email').value = student.email || '';
    document.getElementById('student-rollno').value = student.rollNo || '';
    document.getElementById('student-phone').value = student.phone || '';
    document.getElementById('student-department').value = student.department || '';
    document.getElementById('student-year-semester').value = 
        `Year ${student.year}, Semester ${student.semester}`;
    document.getElementById('student-address').value = student.address || '';
    
    // Update header info
    document.getElementById('profile-name').textContent = student.name || '';
    document.getElementById('profile-email').textContent = student.email || '';
}

/**
 * Handle profile update
 */
function handleProfileUpdate(e) {
    e.preventDefault();
    
    const formData = new FormData(e.target);
    const profileData = {
        name: formData.get('name'),
        email: formData.get('email'),
        phone: formData.get('phone'),
        address: formData.get('address')
    };
    
    // Validate data
    if (!profileData.name || !profileData.email) {
        ExamSeatingApp.showNotification('Name and email are required', 'error');
        return;
    }
    
    if (!ExamSeatingApp.isValidEmail(profileData.email)) {
        ExamSeatingApp.showNotification('Please enter a valid email address', 'error');
        return;
    }
    
    ExamSeatingApp.showLoading('Updating profile...');
    
    fetch('/student/profile', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(profileData)
    })
    .then(response => response.json())
    .then(data => {
        ExamSeatingApp.hideLoading();
        
        if (data.success) {
            ExamSeatingApp.showNotification('Profile updated successfully', 'success');
            
            // Update user name in header
            document.getElementById('user-name').textContent = profileData.name;
            document.getElementById('profile-name').textContent = profileData.name;
            document.getElementById('profile-email').textContent = profileData.email;
            
        } else {
            ExamSeatingApp.showNotification(data.message || 'Failed to update profile', 'error');
        }
    })
    .catch(error => {
        ExamSeatingApp.hideLoading();
        console.error('Error updating profile:', error);
        ExamSeatingApp.showNotification('Error updating profile', 'error');
    });
}

/**
 * Show change password modal
 */
function showChangePasswordModal() {
    const modal = document.getElementById('change-password-modal');
    if (modal) {
        modal.classList.add('show');
        document.getElementById('current-password').focus();
    }
}

/**
 * Hide modal
 */
function hideModal(modal) {
    if (modal) {
        modal.classList.remove('show');
        
        // Clear form
        const form = modal.querySelector('form');
        if (form) {
            form.reset();
        }
    }
}

/**
 * Handle password change
 */
function handlePasswordChange(e) {
    e.preventDefault();
    
    const formData = new FormData(e.target);
    const currentPassword = formData.get('currentPassword');
    const newPassword = formData.get('newPassword');
    const confirmPassword = formData.get('confirmPassword');
    
    // Validate passwords
    if (!currentPassword || !newPassword || !confirmPassword) {
        ExamSeatingApp.showNotification('All password fields are required', 'error');
        return;
    }
    
    if (newPassword !== confirmPassword) {
        ExamSeatingApp.showNotification('New passwords do not match', 'error');
        return;
    }
    
    if (newPassword.length < 8) {
        ExamSeatingApp.showNotification('New password must be at least 8 characters long', 'error');
        return;
    }
    
    ExamSeatingApp.showLoading('Changing password...');
    
    fetch('/student/password', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            currentPassword: currentPassword,
            newPassword: newPassword
        })
    })
    .then(response => response.json())
    .then(data => {
        ExamSeatingApp.hideLoading();
        
        if (data.success) {
            ExamSeatingApp.showNotification('Password changed successfully', 'success');
            hideModal(document.getElementById('change-password-modal'));
        } else {
            ExamSeatingApp.showNotification(data.message || 'Failed to change password', 'error');
        }
    })
    .catch(error => {
        ExamSeatingApp.hideLoading();
        console.error('Error changing password:', error);
        ExamSeatingApp.showNotification('Error changing password', 'error');
    });
}

/**
 * Handle exam search
 */
function handleExamSearch() {
    if (currentSection === 'exams') {
        loadAllExams();
    }
}

/**
 * Handle exam filter
 */
function handleExamFilter() {
    if (currentSection === 'exams') {
        loadAllExams();
    }
}

/**
 * Handle logout
 */
function handleLogout(e) {
    e.preventDefault();
    
    if (confirm('Are you sure you want to logout?')) {
        ExamSeatingApp.showLoading('Logging out...');
        
        fetch('/auth/logout', {
            method: 'POST'
        })
        .then(response => response.json())
        .then(data => {
            ExamSeatingApp.hideLoading();
            ExamSeatingApp.showNotification('Logged out successfully', 'success');
            
            // Clear user data
            currentStudent = null;
            
            // Redirect to login page
            setTimeout(() => {
                window.location.href = '/student-login.html';
            }, 1000);
        })
        .catch(error => {
            ExamSeatingApp.hideLoading();
            console.error('Logout error:', error);
            
            // Even if logout fails, redirect to login
            setTimeout(() => {
                window.location.href = '/student-login.html';
            }, 1000);
        });
    }
}

/**
 * Debounce function for search
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
 * Handle initial page load with hash
 */
window.addEventListener('load', function() {
    const hash = window.location.hash.replace('#', '');
    if (hash && ['dashboard', 'exams', 'seating', 'profile'].includes(hash)) {
        showSection(hash, false);
    } else {
        showSection('dashboard', false);
    }
});

// Export functions for debugging (development only)
if (typeof window !== 'undefined' && window.location.hostname === 'localhost') {
    window.StudentDashboard = {
        loadDashboardData,
        loadAllExams,
        loadSeatingArrangements,
        showSection,
        currentStudent: () => currentStudent
    };
}