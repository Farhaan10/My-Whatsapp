<?php

if($_SERVER['REQUEST_METHOD']=='GET'){
	$sender = $_GET['sender'];
	$receiver = $_GET['receiver'];
	$message = $_GET['message'];
	require_once('connection.php');
	$sql = "INSERT INTO chats (id, sender, receiver, message) values (NULL, '$sender', '$receiver', '$message');";
	$result = mysqli_query($connect, $sql);
	if($result>0){
		$response["success"] = 1;
	}
	else {
		$response["success"] = 0;
	}
	echo json_encode($response);
	mysqli_close($connect);
	
}
?>