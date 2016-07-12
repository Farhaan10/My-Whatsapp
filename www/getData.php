<?php

if($_SERVER['REQUEST_METHOD']=='GET'){
	$sender = $_GET['sender'];
	require_once('connection.php');
	$sql = "select sender, receiver from chats WHERE sender='".$sender."' or receiver='".$sender."' ORDER BY id DESC";
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