<?php
// Simple database connection
$host = "127.0.0.1";
$db = "d268";
$user = "s268";
$pass = "s268";

$conn = new mysqli($host, $user, $pass, $db);

// Check connection
if ($conn->connect_error) {
    echo json_encode(["success" => false, "message" => "Database connection failed"]);
    exit;
}

// Get form data
$username = $_POST['STUDENT_NUMBER'];
$password = $_POST['PASSWORD_HASH'];

// Simple validation
if (empty($username) || empty($password)) {
    echo json_encode(["success" => false, "message" => "Please fill all fields"]);
    exit;
}

// Check user credentials (basic approach)
$sql = "SELECT PASSWORD_HASH FROM AUTHENTICATION WHERE STUDENT_NUMBER = '$username'";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    $row = $result->fetch_assoc();
    
    // Simple password check
    if (password_verify($password, $row['PASSWORD_HASH'])) {
        
        // Get user data
        $userSql = "SELECT STUDENT_NUMBER, STUDENT_FNAME, STUDENT_LNAME, STUDENT_EMAIL, USER_ROLE FROM USERS WHERE STUDENT_NUMBER = '$username'";
        $userResult = $conn->query($userSql);
        $userData = $userResult->fetch_assoc();
        
        // Get all posts
        $postsSql = "SELECT p.POST_ID, p.STUDENT_NUMBER, p.TITLE, p.POST_QUESTION, p.POST_DATE, p.VOTE_COUNT, 
                     CONCAT(u.STUDENT_FNAME, ' ', u.STUDENT_LNAME) as AUTHOR_NAME 
                     FROM POSTS p 
                     JOIN USERS u ON p.STUDENT_NUMBER = u.STUDENT_NUMBER 
                     WHERE p.STATUS = 1";
        $postsResult = $conn->query($postsSql);
        $posts = [];
        while ($row = $postsResult->fetch_assoc()) {
            $posts[] = $row;
        }
        
        // Get all comments
        $commentsSql = "SELECT c.COMMENT_ID, c.POST_ID, c.STUDENT_NUMBER, c.STUDENT_COMMENT, c.COMMENT_DATE, c.VOTE_COUNT,
                        CONCAT(u.STUDENT_FNAME, ' ', u.STUDENT_LNAME) as AUTHOR_NAME 
                        FROM COMMENTS c 
                        JOIN USERS u ON c.STUDENT_NUMBER = u.STUDENT_NUMBER 
                        WHERE c.STATUS = 1";
        $commentsResult = $conn->query($commentsSql);
        $comments = [];
        while ($row = $commentsResult->fetch_assoc()) {
            $comments[] = $row;
        }
        
        // Return success response
        echo json_encode([
            "success" => true,
            "message" => "Login successful",
            "user" => $userData,
            "posts" => $posts,
            "comments" => $comments
        ]);
        
    } else {
        echo json_encode(["success" => false, "message" => "Wrong password"]);
    }
} else {
    echo json_encode(["success" => false, "message" => "User not found"]);
}

$conn->close();
?>