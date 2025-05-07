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
    die(json_encode(["error" => "Connection failed: " . $conn->connect_error]));
}

// Handle POST request for authentication
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Validate and sanitize inputs
    $username = isset($_POST['username']) ? trim($_POST['username']) : '';
    $password = isset($_POST['password']) ? $_POST['password'] : '';
    
    // Validate inputs
    if (empty($username) || empty($password)) {
        echo json_encode(["error" => "Username and password are required"]);
        exit;
    }
    
    try {
        // First check if user exists and is verified
        $checkUser = $conn->prepare("SELECT VERIFIED FROM AUTHENTICATION WHERE STUDENT_NUMBER = ?");
        if (!$checkUser) {
            throw new Exception("Prepare failed: " . $conn->error);
        }
        
        $checkUser->bind_param("s", $username);
        $checkUser->execute();
        $checkResult = $checkUser->get_result();
        
        if ($checkResult->num_rows > 0) {
            $row = $checkResult->fetch_assoc();
            
            if ($row['VERIFIED'] == 1) {
                // User is verified, now check password
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
                        $data = $conn->prepare("SELECT STUDENT_FNAME, STUDENT_LNAME, STUDENT_CONTACT_NO, STUDENT_EMAIL FROM STUDENTS WHERE STUDENT_NUMBER = ?");
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
                            echo json_encode(["error" => "User data not found"]);
                        }
                    } else {
                        echo json_encode(["error" => "Invalid username or password"]);
                    }
                } else {
                    echo json_encode(["error" => "Invalid username or password"]);
                }
            } else {
                echo json_encode(["error" => "Account not verified"]);
            }
        } else {
            echo json_encode(["error" => "Invalid username or password"]);
        }
        
    } catch (Exception $e) {
        echo json_encode(["error" => "An error occurred: " . $e->getMessage()]);
    } finally {
        // Close all prepared statements
        if (isset($checkUser)) $checkUser->close();
        if (isset($result)) $result->close();
        if (isset($data)) $data->close();
        $conn->close();
    }
} else {
    // Handle non-POST requests
    echo json_encode(["error" => "Invalid request method"]);
}
?>