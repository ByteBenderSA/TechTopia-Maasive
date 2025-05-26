<?php
// Simple database connection
$host = "127.0.0.1";
$db = "d268";  // Fixed database name to match login.php
$user = "s268";
$pass = "s268";

$conn = new mysqli($host, $user, $pass, $db);

// Check connection
if ($conn->connect_error) {
    echo json_encode(["success" => false, "message" => "Database connection failed"]);
    exit;
}

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Get form data
    $student_number = isset($_POST['STUDENT_NUMBER']) ? $_POST['STUDENT_NUMBER'] : '';
    $postHead = isset($_POST['POST_HEAD']) ? $_POST['POST_HEAD'] : '';
    $postText = isset($_POST['POST_TEXT']) ? $_POST['POST_TEXT'] : '';
    
    // Simple validation
    if (empty($student_number) || empty($postHead) || empty($postText)) {
        echo "Please fill all required fields";
        exit;
    }
    
    $currentDateTime = date('Y-m-d H:i:s');

    // Insert post into database
    $stmt = $conn->prepare("INSERT INTO POSTS (STUDENT_NUMBER, TITLE, POST_QUESTION, POST_DATE, STATUS) VALUES (?,?,?,?,1)");
    if ($stmt) {
        $stmt->bind_param("ssss", $student_number, $postHead, $postText, $currentDateTime);
        if ($stmt->execute()) {
            echo "Post successfully created.";  // Fixed response message to match PostFragment
        } else {
            echo "Error creating post: " . $stmt->error;
        }
        $stmt->close();
    } else {
        echo "Database error: " . $conn->error;
    }
}

$conn->close();
?>
