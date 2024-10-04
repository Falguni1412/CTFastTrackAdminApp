<?php

//$con=mysqli_connect() or die("Could not Connect");
$conn = new mysqli("localhost","root","","ctbustrack");


// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 
?>