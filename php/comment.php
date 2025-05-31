<?php
/**
 * Simple Comment API for MobileMind Forum
 * Student-friendly version using basic mysqli
 */

// Enable error reporting for debugging
error_reporting(E_ALL);
ini_set('display_errors', 1);

// Log the start of the script
error_log("=== Starting comment.php script ===");
error_log("Request method: " . $_SERVER['REQUEST_METHOD']);
error_log("Raw POST data: " . file_get_contents('php://input'));
error_log("POST array: " . print_r($_POST, true));

// Simple headers
header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type");

// Handle preflight OPTIONS request
if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    error_log("Handling OPTIONS request");
    http_response_code(200);
    exit;
}

// Simple database connection (same as login.php)
$host = "127.0.0.1";
$db = "d2688828";
$user = "s2688828";
$pass = "s2688828";

try {
    error_log("Attempting database connection...");
    $conn = new mysqli($host, $user, $pass, $db);

    // Check connection
    if ($conn->connect_error) {
        throw new Exception("Database connection failed: " . $conn->connect_error);
    }
    error_log("Database connection successful");

    // Get form data from POST
    $student_number = isset($_POST['STUDENT_NUMBER']) ? trim($_POST['STUDENT_NUMBER']) : null;
    $post_id = isset($_POST['POST_ID']) ? trim($_POST['POST_ID']) : null;
    $comment_text = isset($_POST['COMMENT_TEXT']) ? trim($_POST['COMMENT_TEXT']) : null;

    // Log received data
    error_log("Received data - Student Number: $student_number, Post ID: $post_id, Comment: $comment_text");

    // Simple validation
    if (empty($student_number)) {
        throw new Exception("Student number is required");
    }
    if (empty($post_id)) {
        throw new Exception("Post ID is required");
    }
    if (empty($comment_text)) {
        throw new Exception("Comment text is required");
    }

    error_log("Basic validation passed");

    // Verify student exists
    error_log("Checking if student exists...");
    $checkStudentSql = "SELECT STUDENT_NUMBER FROM USERS WHERE STUDENT_NUMBER = ?";
    $checkStudentStmt = $conn->prepare($checkStudentSql);
    if (!$checkStudentStmt) {
        throw new Exception("Prepare failed for student check: " . $conn->error);
    }
    $checkStudentStmt->bind_param("s", $student_number);
    $checkStudentStmt->execute();
    $studentResult = $checkStudentStmt->get_result();
    if ($studentResult->num_rows === 0) {
        throw new Exception("Student not found");
    }
    error_log("Student verification successful");
    $checkStudentStmt->close();

    // Verify post exists
    error_log("Checking if post exists...");
    $checkPostSql = "SELECT POST_ID FROM POSTS WHERE POST_ID = ?";
    $checkPostStmt = $conn->prepare($checkPostSql);
    if (!$checkPostStmt) {
        throw new Exception("Prepare failed for post check: " . $conn->error);
    }
    $checkPostStmt->bind_param("i", $post_id);
    $checkPostStmt->execute();
    $postResult = $checkPostStmt->get_result();
    if ($postResult->num_rows === 0) {
        throw new Exception("Post not found");
    }
    error_log("Post verification successful");
    $checkPostStmt->close();

    // Get current timestamp
    $current_time = date('Y-m-d H:i:s');
    error_log("Current timestamp: " . $current_time);

    // Insert new comment using prepared statement for security
    error_log("Preparing to insert comment...");
    $insertSql = "INSERT INTO COMMENTS (STUDENT_NUMBER, POST_ID, STUDENT_COMMENT, COMMENT_DATE, VOTE_COUNT, STATUS) 
                  VALUES (?, ?, ?, ?, 0, 1)";

    $stmt = $conn->prepare($insertSql);
    if (!$stmt) {
        throw new Exception("Prepare failed for comment insert: " . $conn->error);
    }

    $stmt->bind_param("ssss", $student_number, $post_id, $comment_text, $current_time);

    if (!$stmt->execute()) {
        throw new Exception("Execute failed for comment insert: " . $stmt->error);
    }
    error_log("Comment inserted successfully");

    // Get the new comment ID
    $comment_id = $conn->insert_id;
    error_log("New comment ID: " . $comment_id);
    
    // Get author name
    error_log("Getting author name...");
    $authorSql = "SELECT CONCAT(STUDENT_FNAME, ' ', STUDENT_LNAME) as AUTHOR_NAME 
                  FROM USERS WHERE STUDENT_NUMBER = ?";
    $authorStmt = $conn->prepare($authorSql);
    if (!$authorStmt) {
        throw new Exception("Author prepare failed: " . $conn->error);
    }

    $authorStmt->bind_param("s", $student_number);
    if (!$authorStmt->execute()) {
        throw new Exception("Author execute failed: " . $authorStmt->error);
    }

    $authorResult = $authorStmt->get_result();
    $authorRow = $authorResult->fetch_assoc();
    $author_name = $authorRow['AUTHOR_NAME'] ?? 'Unknown User';
    error_log("Author name retrieved: " . $author_name);
    $authorStmt->close();
    
    // Return success response
    $response = [
        "success" => true,
        "message" => "Comment added successfully",
        "comment" => [
            "COMMENT_ID" => $comment_id,
            "STUDENT_NUMBER" => $student_number,
            "AUTHOR_NAME" => $author_name,
            "STUDENT_COMMENT" => $comment_text,
            "COMMENT_DATE" => $current_time,
            "VOTE_COUNT" => 0,
            "POST_ID" => $post_id
        ]
    ];
    error_log("Sending success response: " . json_encode($response));
    echo json_encode($response);

} catch (Exception $e) {
    error_log("Error in comment.php: " . $e->getMessage());
    error_log("Stack trace: " . $e->getTraceAsString());
    http_response_code(500);
    $error_response = [
        "success" => false,
        "message" => "Server error: " . $e->getMessage()
    ];
    error_log("Sending error response: " . json_encode($error_response));
    echo json_encode($error_response);
} finally {
    if (isset($stmt)) $stmt->close();
    if (isset($authorStmt)) $authorStmt->close();
    if (isset($checkStudentStmt)) $checkStudentStmt->close();
    if (isset($checkPostStmt)) $checkPostStmt->close();
    if (isset($conn)) $conn->close();
    error_log("=== End of comment.php script ===");
}
?> 