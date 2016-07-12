<?php

if($_SERVER['REQUEST_METHOD']=='GET'){
	$sender = $_GET['sender'];
	$receiver = $_GET['receiver'];
	require_once('connection.php');
	$sql = "SELECT * FROM chats WHERE (sender='".$sender."' OR sender='".$receiver."') AND (receiver='".$receiver."' OR receiver='".$sender."')";
	$r = mysqli_query($connect,$sql);
	$number_of_rows = mysqli_num_rows($r);
	$result = array();
	if($number_of_rows>0) {
		while($row = mysqli_fetch_assoc($r)) {
			$result[] = $row;
		}
	}
	
	echo json_encode(array("result"=>$result));
	mysqli_close($connect);
}