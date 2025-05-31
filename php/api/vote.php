<?php
/**
 * Simple Voting API for MobileMind Forum
 * Student-friendly version using basic mysqli
 */

// Simple headers
header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST");

// Simple database connection (same as login.php)
$host = "127.0.0.1";
$db = "d2688828";
$user = "s2688828";
$pass = "s2688828";

$conn = new mysqli($host, $user, $pass, $db);

// Check connection
if ($conn->connect_error) {
    echo json_encode(["success" => false, "message" => "Database connection failed"]);
    exit;
}

// Get form data from POST (same as login.php)
$student_number = $_POST['STUDENT_NUMBER'];
$vote_type = $_POST['VOTE_TYPE']; // 'post' or 'comment'
$post_id = isset($_POST['POST_ID']) ? $_POST['POST_ID'] : null;
$comment_id = isset($_POST['COMMENT_ID']) ? $_POST['COMMENT_ID'] : null;

// Simple validation
if (empty($student_number) || empty($vote_type)) {
    echo json_encode(["success" => false, "message" => "Missing required fields"]);
    exit;
}

if ($vote_type == 'post') {
    if (empty($post_id)) {
        echo json_encode(["success" => false, "message" => "Post ID required"]);
        exit;
    }
    
    // Check if user already voted on this post
    $checkSql = "SELECT VOTE_ID FROM VOTES WHERE STUDENT_NUMBER = '$student_number' AND POST_ID = '$post_id'";
    $checkResult = $conn->query($checkSql);
    
    if ($checkResult->num_rows > 0) {
        // User already voted - remove vote
        $deleteSql = "DELETE FROM VOTES WHERE STUDENT_NUMBER = '$student_number' AND POST_ID = '$post_id'";
        $conn->query($deleteSql);
        
        // Update post vote count (decrease)
        $updateSql = "UPDATE POSTS SET VOTE_COUNT = VOTE_COUNT - 1 WHERE POST_ID = '$post_id'";
        $conn->query($updateSql);
        
        $message = "Vote removed";
    } else {
        // User hasn't voted - add vote
        $insertSql = "INSERT INTO VOTES (STUDENT_NUMBER, POST_ID, VOTE_TYPE) VALUES ('$student_number', '$post_id', 1)";
        $conn->query($insertSql);
        
        // Update post vote count (increase)
        $updateSql = "UPDATE POSTS SET VOTE_COUNT = VOTE_COUNT + 1 WHERE POST_ID = '$post_id'";
        $conn->query($updateSql);
        
        $message = "Vote added";
    }
    
    // Get updated vote count
    $countSql = "SELECT VOTE_COUNT FROM POSTS WHERE POST_ID = '$post_id'";
    $countResult = $conn->query($countSql);
    $countRow = $countResult->fetch_assoc();
    $new_count = $countRow['VOTE_COUNT'];
    
} else if ($vote_type == 'comment') {
    if (empty($comment_id)) {
        echo json_encode(["success" => false, "message" => "Comment ID required"]);
        exit;
    }
    
    // Check if user already voted on this comment
    $checkSql = "SELECT VOTE_ID FROM VOTES WHERE STUDENT_NUMBER = '$student_number' AND COMMENT_ID = '$comment_id'";
    $checkResult = $conn->query($checkSql);
    
    if ($checkResult->num_rows > 0) {
        // User already voted - remove vote
        $deleteSql = "DELETE FROM VOTES WHERE STUDENT_NUMBER = '$student_number' AND COMMENT_ID = '$comment_id'";
        $conn->query($deleteSql);
        
        // Update comment vote count (decrease)
        $updateSql = "UPDATE COMMENTS SET VOTE_COUNT = VOTE_COUNT - 1 WHERE COMMENT_ID = '$comment_id'";
        $conn->query($updateSql);
        
        $message = "Vote removed";
    } else {
        // User hasn't voted - add vote
        $insertSql = "INSERT INTO VOTES (STUDENT_NUMBER, COMMENT_ID, VOTE_TYPE) VALUES ('$student_number', '$comment_id', 1)";
        $conn->query($insertSql);
        
        // Update comment vote count (increase)
        $updateSql = "UPDATE COMMENTS SET VOTE_COUNT = VOTE_COUNT + 1 WHERE COMMENT_ID = '$comment_id'";
        $conn->query($updateSql);
        
        $message = "Vote added";
    }
    
    // Get updated vote count
    $countSql = "SELECT VOTE_COUNT FROM COMMENTS WHERE COMMENT_ID = '$comment_id'";
    $countResult = $conn->query($countSql);
    $countRow = $countResult->fetch_assoc();
    $new_count = $countRow['VOTE_COUNT'];
    
} else {
    echo json_encode(["success" => false, "message" => "Invalid vote type"]);
    exit;
}

// Return success response (same format as login.php)
echo json_encode([
    "success" => true,
    "message" => $message,
    "new_vote_count" => $new_count
]);

$conn->close();
?> 