<?php
/**
 * Simple Database Configuration for MobileMind Voting System
 * Uses basic mysqli connection like login.php
 */

// Database connection settings
$host = "127.0.0.1";
$db = "d268";           // Change this to your database name
$user = "s268";         // Change this to your MySQL username  
$pass = "s268";         // Change this to your MySQL password

// Create connection
function getConnection() {
    global $host, $user, $pass, $db;
    
    $conn = new mysqli($host, $user, $pass, $db);
    
    // Check connection
    if ($conn->connect_error) {
        echo json_encode(["success" => false, "message" => "Database connection failed"]);
        exit;
    }
    
    return $conn;
}
?> 