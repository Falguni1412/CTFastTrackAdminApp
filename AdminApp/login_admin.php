<?php
require_once("dbConfig.php");
$username=$_POST['username'];
$password=$_POST['password'];
$sql="select username,password from register_admin where username='$username' and password='$password'";
$result=mysqli_query($conn,$sql);
if(mysqli_fetch_array($result))
{
	$response["success"]=1;
}
else
{
	$response["success"]=0;
}
echo json_encode($response);

?>