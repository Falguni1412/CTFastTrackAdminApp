<?php
require_once('config.php');

// Example query to fetch data from a table called `register_admin`
$sql = "SELECT * FROM register_admin";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    // Output data of each row
    while($row = $result->fetch_assoc()) {
        echo "ID: " . $row["id"]. " - Username: " . $row["username"]. " - Password: " . $row["password"]. "<br>";
    }
} else {
    echo "0 results";
}
$conn->close();
?>
