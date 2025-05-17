<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);

$host = "127.0.0.1";
$db = "d2688828"
$user = "s2688828"
$pass = "s2688828;

$conn = new mysqli($host, $user, $pass, $db);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

echo "Testing server response<br>";

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $username = $_POST['STUDENT_NUMBER'];
    $password = $_POST['PASSWORD_HASH'];
    $student_fname = $_POST['STUDENT_FNAME'];
    $student_lname = $_POST['STUDENT_LNAME'];
    $student_contact_no = $_POST['STUDENT_CONTACT_NO'];
    $student_email = $username . "@students.wits.ac.za";

    // Generate a 32-character verification token
    $token = bin2hex(random_bytes(16));

    // Hash the password
    $hashed_password = password_hash($password, PASSWORD_DEFAULT);

    // INSERT INTO USERS
    $stmt1 = $conn->prepare("INSERT INTO USERS (STUDENT_NUMBER, STUDENT_FNAME, STUDENT_LNAME, STUDENT_CONTACT_NO, STUDENT_EMAIL, USER_ROLE) VALUES (?, ?, ?, ?, ?, 'STUDENT')");
    $stmt1->bind_param("sssss", $username, $student_fname, $student_lname, $student_contact_no, $student_email);

    // INSERT INTO AUTHENTICATION
    $stmt2 = $conn->prepare("INSERT INTO AUTHENTICATION (STUDENT_NUMBER, PASSWORD_HASH, VERIFICATION_TOKEN, VERIFIED) VALUES (?, ?, ?, 0)");
    $stmt2->bind_param("sss", $username, $hashed_password, $token);

    if ($stmt1->execute() && $stmt2->execute()) {
        $verificationLink = "https://lamp.ms.wits.ac.za/home/s2688828/verify.php?token=$token";

            use PHPMailer\PHPMailer\PHPMailer;
            use PHPMailer\PHPMailer\Exception;
            
            require 'vendor/autoload.php'; // If using Composer
            // OR if manually including files:
            // require 'path/to/PHPMailer/src/Exception.php';
            // require 'path/to/PHPMailer/src/PHPMailer.php';
            // require 'path/to/PHPMailer/src/SMTP.php';
            
            $mail = new PHPMailer(true);
            
            try {
                //Server settings
                $mail->isSMTP();
                $mail->Host       = 'smtp.gmail.com'; // Use your SMTP provider
                $mail->SMTPAuth   = true;
                $mail->Username   = 'tshilidzisibara4@gmail.com'; // Your SMTP username
                $mail->Password   = 'ffhr bfcm regp kuix'; // Your SMTP password or app-specific password
                $mail->SMTPSecure = PHPMailer::ENCRYPTION_STARTTLS; // or PHPMailer::ENCRYPTION_SMTPS
                $mail->Port       = 587;
            
                //Recipients
                $mail->setFrom('noreply@students.wits.ac.za', 'Wits Student System');
                $mail->addAddress($student_email);
            
                // Content
                $mail->isHTML(false);
                $mail->Subject = 'Verify your email';
                $mail->Body    = "Click here to verify: $verificationLink";
            
                $mail->send();
                echo "User successfully created. Please check your email to verify your account.";
            } catch (Exception $e) {
                echo "Message could not be sent. Mailer Error: {$mail->ErrorInfo}";
            }
            

    $stmt1->close();
    $stmt2->close();
}

$conn->close();
?>
