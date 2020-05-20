<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
	
<%@page isELIgnored="false" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Insert title here</title>

<!-- START :: HEADER FORM -->
<%@ include file="form/header.jsp"%>
<!-- END :: HEADER FORM -->

<!--Bootstrap only for styling-->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
<!--Bootstrap only for styling-->

<!-- START :: CSS -->
<!-- <link href="resources/css/master.css" rel="stylesheet" type="text/css"> -->
<style type="text/css">

.container {
    background: rgb(148, 144, 144);
    margin: 50px auto;
    max-width: 80%;
    text-align: center;
    padding: 2%;
}

button {
    margin: 1em;
}

input {
    margin-top: 1em;
}

.footer {
    background: rgb(148, 144, 144);
    text-align: center;
    padding: 2%;
    position: absolute;
    bottom: 0;
    width: 100%;
}

video {
    transform: rotateY(180deg);
    -webkit-transform:rotateY(180deg); /* Safari and Chrome */
    -moz-transform:rotateY(180deg); /* Firefox */
}

</style>
<!-- END :: CSS -->

<!-- START :: JAVASCRIPT -->
<script type="text/javascript">
</script>
<!-- END :: JAVASCRIPT -->

</head>
<body>

	<div class="container">
		<h1>WebRTC</h1>
	
		<div>
		   <video id="remoteView" width="640" height="480" autoplay style="display: inline;"></video>
		</div>
		
		<div>
		  <video id="selfView" width="320" height="240" autoplay style="display: inline;"></video>
		</div>

		<button onclick="startRTC();">startRTC</button>
		<input id="RTC_Manager" type="text"><button onclick="getOffer();">offer</button>
	
	</div>
	
	<script src='/resources/js/adapter.js'></script>
	<script src='/resources/js/main.js'></script>
	
	<script type="text/javascript">
	
		$(function(){
		    console.log("${loginMember.memberid}")
		    connect("${loginMember.memberid}");
		 });
		 
		 function getOffer(){
		    var rtc_manager = $("#RTC_Manager").val();
		    
		    offer(rtc_manager);
		 }
	
	</script>

</body>
</html>