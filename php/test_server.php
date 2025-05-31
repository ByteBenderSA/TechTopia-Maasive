<?php
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: POST, GET');
header('Access-Control-Allow-Headers: Content-Type');

// Simple test response
echo json_encode([
    'success' => true,
    'message' => 'Server is working!',
    'timestamp' => date('Y-m-d H:i:s'),
    'post_data' => $_POST,
    'method' => $_SERVER['REQUEST_METHOD']
]);
?> 