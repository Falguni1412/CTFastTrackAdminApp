<?php
require_once("dbConfig.php");
$bus_from=$_POST['bus_from'];
$bus_to=$_POST['bus_to'];
$bus_number=$_POST['bus_number'];
$total_seats=$_POST['total_seats'];
$date=$_POST['date'];
$time=$_POST['time'];


$sql="insert into get_bus_by_station(from_station,to_station,number,seats,bus_date,bus_time) values('$bus_from','$bus_to','$bus_number','$total_seats','$date','$time')";
$result=mysqli_query($conn,$sql);
if($result>0)
{
$response['success']=1;
$response['lastinsertedid']= mysqli_insert_id($conn);
}

else
{
	$response['success']=0;
}
echo json_encode($response);

?>