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

</style>
<!-- END :: CSS -->

<!-- START :: JAVASCRIPT -->
<script type="text/javascript">
</script>
<!-- END :: JAVASCRIPT -->

</head>
<body>

	<div class="container">
		<h1>A Demo for messaging in WebRTC</h1>

		<h3>
		 Run two instances of this webpage along with the server to test this
		 application.<br> Create an offer, and then send the message. <br>Check
		 the browser console to see the output.
		</h3>
		
		<!--WebRTC related code-->
		<button type="button" class="btn btn-primary" onclick='createOffer()'>CreateOffer</button>
		<input id="messageInput" type="text" class="form-control" placeholder="message">
		<button type="button" class="btn btn-primary" onclick='sendMessage()'>SEND</button>
		<script src="/resources/js/socket.js"></script>
		<!--WebRTC related code-->
		
	</div>
	<div class="footer">This application is intentionally made simple
	 to avoid cluttering with non WebRTC related code.</div>

</body>
</html>