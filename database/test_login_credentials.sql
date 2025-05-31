-- Test Login Credentials for MobileMind App
-- Use these credentials to test your login functionality

-- All users have the same password: "password123"
-- Password hash: $2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi

-- Test User 1: John Doe
-- Student Number: 2688828
-- Password: password123

-- Test User 2: Sarah Smith  
-- Student Number: 2666605
-- Password: password123

-- Test User 3: Mike Johnson
-- Student Number: 2548941
-- Password: password123

-- Test User 4: Emma Wilson
-- Student Number: 2729013
-- Password: password123

-- Quick verification query to see all users:
SELECT 
    u.STUDENT_NUMBER,
    CONCAT(u.STUDENT_FNAME, ' ', u.STUDENT_LNAME) as FULL_NAME,
    u.STUDENT_EMAIL,
    u.USER_ROLE
FROM USERS u 
JOIN AUTHENTICATION a ON u.STUDENT_NUMBER = a.STUDENT_NUMBER; 