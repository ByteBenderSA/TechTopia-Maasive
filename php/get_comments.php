<?php
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: POST');
header('Access-Control-Allow-Headers: Content-Type');

// Database connection - FIXED to match working login.php
$host = "127.0.0.1";
$db = "d2688828";
$user = "s2688828";
$pass = "s2688828";

$conn = new mysqli($host, $user, $pass, $db);

if ($conn->connect_error) {
    die(json_encode([
        'success' => false,
        'message' => 'Connection failed: ' . $conn->connect_error
    ]));
}

// Get POST data
$post_id = $_POST['POST_ID'] ?? null;
$student_number = $_POST['STUDENT_NUMBER'] ?? null;

// Validate required fields
if (!$post_id) {
    die(json_encode([
        'success' => false,
        'message' => 'Post ID is required'
    ]));
}

// Get comments for the post - FIXED to match working login.php pattern
$query = "SELECT c.*, CONCAT(u.STUDENT_FNAME, ' ', u.STUDENT_LNAME) as AUTHOR_NAME 
          FROM COMMENTS c 
          JOIN USERS u ON c.STUDENT_NUMBER = u.STUDENT_NUMBER 
          WHERE c.POST_ID = ? AND c.STATUS = 1
          ORDER BY c.COMMENT_DATE DESC";

$stmt = $conn->prepare($query);
$stmt->bind_param("i", $post_id);
$stmt->execute();
$result = $stmt->get_result();

$comments = [];
while ($row = $result->fetch_assoc()) {
    // Get vote status for this comment if student number provided
    $vote_status = 0; // Default to no vote
    if ($student_number) {
        $vote_query = "SELECT VOTE_TYPE FROM VOTES 
                      WHERE STUDENT_NUMBER = ? AND COMMENT_ID = ?";
        $vote_stmt = $conn->prepare($vote_query);
        $vote_stmt->bind_param("si", $student_number, $row['COMMENT_ID']);
        $vote_stmt->execute();
        $vote_result = $vote_stmt->get_result();
        if ($vote_row = $vote_result->fetch_assoc()) {
            $vote_status = $vote_row['VOTE_TYPE'];
        }
        $vote_stmt->close();
    }

    $comments[] = [
        'COMMENT_ID' => $row['COMMENT_ID'],
        'STUDENT_NUMBER' => $row['STUDENT_NUMBER'],
        'AUTHOR_NAME' => $row['AUTHOR_NAME'],
        'COMMENT_DATE' => $row['COMMENT_DATE'],
        'STUDENT_COMMENT' => $row['STUDENT_COMMENT'],
        'VOTE_COUNT' => $row['VOTE_COUNT'],
        'POST_ID' => $row['POST_ID'],
        'PARENT_COMMENT_ID' => $row['PARENT_COMMENT_ID'],
        'VOTE_STATUS' => $vote_status
    ];
}

$stmt->close();
$conn->close();

// Return success response with comments
echo json_encode([
    'success' => true,
    'comments' => $comments
]);
?> 