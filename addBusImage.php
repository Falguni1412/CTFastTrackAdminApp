<?php
require_once("dbConfig.php");
define('UPLOAD_PATH', 'Images/');
mysqli_set_charset($conn,"utf8");
if(isset($_FILES['pic']['name']) && isset($_POST['tags'])){
 
 //uploading file and storing it to database as well 
 try{
 move_uploaded_file($_FILES['pic']['tmp_name'], UPLOAD_PATH . $_FILES['pic']['name']);

 $filename=$_FILES['pic']['name'];
 
 $sql="update get_bus_by_station set image_url='".$filename."' where id=".$_POST['tags']."";
 $result=mysqli_query($conn,$sql);
 
 if(mysqli_affected_rows($conn) >0 )
 {
 $response['error'] = false;
 $response['message'] = 'File uploaded successfully';
 }else{
 throw new Exception("Could not upload file");
 }
 }catch(Exception $e){
 $response['error'] = true;
 $response['message'] = 'Could not upload file';
 }
 
 }else{
 $response['error'] = true;
 $response['message'] = "Required params not available";
 }

 echo json_encode($response);

?>