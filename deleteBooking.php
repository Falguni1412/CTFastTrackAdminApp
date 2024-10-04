<?php
require_once("dbConfig.php");

$id=$_POST['id'];

$sql="delete from book_my_bus where id='$id'";
$result=mysqli_query($conn,$sql);
if($result>0)
{
$response['success']=1;
}
else
{
	$response['success']=0;
}
echo json_encode($response);

?>