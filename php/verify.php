<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);

$host = "127.0.0.1";
$db = "d268";
$user = "s268";
$pass = "s268;

$conn = new mysqli($host, $user, $pass, $db);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Check if token is provided
if (isset($_GET['token'])) {
    $token = $_GET['token'];

    // Find user by token
    $stmt = $conn->prepare("SELECT STUDENT_NUMBER FROM AUTHENTICATION WHERE VERIFICATION_TOKEN = ?");
    $stmt->bind_param("s", $token);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($result->num_rows > 0) {
        // Token found, proceed to verify user
        $update = $conn->prepare("UPDATE AUTHENTICATION SET VERIFIED = 1, VERIFICATION_TOKEN = NULL WHERE VERIFICATION_TOKEN = ?");
        $update->bind_param("s", $token);

        if ($update->execute()) {
            echo "✅ Your email has been successfully verified. You can now log in.";
        } else {
            echo "⚠️ Error verifying account: " . $update->error;
        }

        $update->close();
    } else {
        echo "❌ Invalid or expired verification token.";
    }

    $stmt->close();
} else {
    echo "❗ No verification token provided.";
}

$conn->close();
?>
