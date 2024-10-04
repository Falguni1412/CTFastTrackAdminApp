<?php
header("Content-Type: application/json; charset=UTF-8");

// Retrieve POST data from the request
$username = $_POST['username'];
$password = $_POST['password'];

// Database configuration
$servername = "localhost";
$dbname = "ctbustrack"; // Replace with your actual database name
$dbusername = "root"; // Default username for MySQL in XAMPP
$dbpassword = ""; // Default password for MySQL in XAMPP (empty by default)

// Create a connection to the database
$conn = new mysqli($servername, $dbusername, $dbpassword, $dbname);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Prepare SQL statement to prevent SQL injection
$stmt = $conn->prepare("SELECT * FROM register_admin WHERE username=? AND password=?");
$stmt->bind_param("ss", $username, $password); // "ss" means two strings

// Execute the statement
$stmt->execute();

// Get the result
$result = $stmt->get_result();

// Check if any rows were returned
if ($result->num_rows > 0) {
    // User found
    echo json_encode(array("success" => "1"));
} else {
    // User not found
    echo json_encode(array("success" => "0"));
}

// Close the connection
$stmt->close();
$conn->close();
?>
