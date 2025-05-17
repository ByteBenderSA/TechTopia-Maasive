<?php
// Database connection details
$host = "127.0.0.1";
$db = "d268";
$user = "s268";
$pass = "s268";

// Error reporting for debugging (remove in production)
ini_set('display_errors', 1);
error_reporting(E_ALL);

// Create database connection
$conn = new mysqli($host, $user, $pass, $db);

// Check connection
if ($conn->connect_error) {
    die(json_encode([
        "success" => false,
        "message" => "Connection failed: " . $conn->connect_error
    ]));
}

// Handle POST request for authentication
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Get parameters from request
    $username = isset($_POST['STUDENT_NUMBER']) ? trim($_POST['STUDENT_NUMBER']) : '';
    $password = isset($_POST['PASSWORD_HASH']) ? $_POST['PASSWORD_HASH'] : '';
    
    // Validate inputs
    if (empty($username) || empty($password)) {
        echo json_encode([
            "success" => false,
            "message" => "Username and password are required"
        ]);
        exit;
    }
    
    try {
        // Check password
        $result = $conn->prepare("SELECT PASSWORD_HASH FROM AUTHENTICATION WHERE STUDENT_NUMBER = ?");
        if (!$result) {
            throw new Exception("Prepare failed: " . $conn->error);
        }
        
        $result->bind_param("s", $username);
        $result->execute();
        $stmt = $result->get_result();
        
        if ($stmt->num_rows > 0) {
            $check_row = $stmt->fetch_assoc();
            $storedHash = $check_row['PASSWORD_HASH'];
            
            if (password_verify($password, $storedHash)) {
                // Password is correct, fetch user data
                $data = $conn->prepare("SELECT STUDENT_NUMBER, STUDENT_FNAME, STUDENT_LNAME, STUDENT_CONTACT_NO, STUDENT_EMAIL, USER_ROLE FROM STUDENTS WHERE STUDENT_NUMBER = ?");
                if (!$data) {
                    throw new Exception("Prepare failed: " . $conn->error);
                }
                
                $data->bind_param("s", $username);
                $data->execute();
                $return = $data->get_result();
                
                if ($return->num_rows > 0) {
                    $userData = $return->fetch_assoc();
                    
                    // Start session and set session variables
                    session_start();
                    $_SESSION['user_id'] = $username;
                    $_SESSION['first_name'] = $userData['STUDENT_FNAME'];
                    $_SESSION['authenticated'] = true;
                    
                    // Return user data as JSON
                    echo json_encode([
                        "success" => true,
                        "message" => "Authentication successful",
                        "user" => $userData
                    ]);
                } else {
                    echo json_encode([
                        "success" => false, 
                        "message" => "User data not found"
                    ]);
                }
            } else {
                echo json_encode([
                    "success" => false,
                    "message" => "Invalid username or password"
                ]);
            }
        } else {
            echo json_encode([
                "success" => false,
                "message" => "Invalid username or password"
            ]);
        }
        
    } catch (Exception $e) {
        echo json_encode([
            "success" => false,
            "message" => "An error occurred: " . $e->getMessage()
        ]);
    } finally {
        // Close all prepared statements
        if (isset($result)) $result->close();
        if (isset($data)) $data->close();
        $conn->close();
    }
} else {
    // Handle non-POST requests
    echo json_encode([
        "success" => false,
        "message" => "Invalid request method"
    ]);
}
?>