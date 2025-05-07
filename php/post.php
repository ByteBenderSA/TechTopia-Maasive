<?php
$host = "127.0.0.1";
$db = "d2688828";
$user = "s2688828";
$pass = "s2688828";

$conn = new mysqli($host, $user, $pass, $db);

if ($_SERVER['REQUEST_METHOD'] == 'POST'){
    $student_number = isset($_POST['STUDENT_NUMBER']) ? $_POST['STUDENT_NUMBER'] : '';
    $postHead = isset($_POST['POST_HEAD']) ? $_POST['POST_HEAD'] : '';
    $postText = isset($_POST['POST_TEXT']) ? $_POST['POST_TEXT'] : '';
    $currentDateTime = date('Y-m-d H:i:s');

    $stmt = $conn->prepare("INSERT INTO POSTS (STUDENT_NUMBER, TITLE, POST_QUESTION, POST_DATE) VALUES (?,?,?,?)");
    if($stmt){
        $stmt->bind_param("ssss", $student_number, $postHead, $postText, $currentDateTime);
        $stmt->execute();
        $stmt->close();
    }
    else{
        echo "Prepare failed: " . $conn->error;
    }


}
?>