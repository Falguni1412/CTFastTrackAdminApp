<?php
require_once("dbConfig.php");
$sql="select bmb.id,bmb.bus_id,bmb.bus_number,bmb.bus_from,bmb.bus_to,bmb.date,bmb.time,r.name,r.contact,r.address from book_my_bus bmb inner join registration r on bmb.user_id = r.id";

$result=array();

$data=mysqli_query($conn,$sql);

while($row=mysqli_fetch_array($data))
{
	array_push($result,array('id'=>$row[0],'bus_id' => $row[1],'bus_number' => $row[2],'bus_from'=>$row[3],'bus_to' => $row[4],'date' => $row[5],'time' => $row[6],'name' => $row[7],'contact' => $row[8],'address' => $row[9]));
}
echo json_encode(array('getAllBooking'=>$result));

mysqli_close($con);


?>
