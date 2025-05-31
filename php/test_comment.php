<?php
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: POST');
header('Access-Control-Allow-Headers: Content-Type');

// Simple test for comment posting
echo json_encode([
    'success' => true,
    'message' => 'Comment test endpoint working!',
    'received_data' => $_POST,
    'timestamp' => date('Y-m-d H:i:s'),
    'method' => $_SERVER['REQUEST_METHOD'],
    'comment' => [
        'COMMENT_ID' => 999,
        'STUDENT_NUMBER' => $_POST['STUDENT_NUMBER'] ?? 'test_user',
        'AUTHOR_NAME' => 'Test User',
        'STUDENT_COMMENT' => $_POST['COMMENT_TEXT'] ?? 'Test comment',
        'COMMENT_DATE' => date('Y-m-d H:i:s'),
        'VOTE_COUNT' => 0,
        'POST_ID' => $_POST['POST_ID'] ?? 1,
        'PARENT_COMMENT_ID' => $_POST['PARENT_COMMENT_ID'] ?? null
    ]
]);
?> 