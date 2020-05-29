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
    margin: 50px auto;
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

#videos {
	display: flex;
	flex-wrap: wrap;
}

#videos > div {
	width: 50%;
}

video {
	width: 320px;
	height: 240px;
	
	/* 화면 좌우반전 */
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

	<div class="row">
		
		<!-- START :: SIDEBAR FORM -->
		<div class="col-lg-3">
			<%@ include file="form/sidebar.jsp"%>
		</div>
		<!-- END :: SIDEBAR FORM -->
		
		
		<!-- START :: page - content -->
		<div class="col-lg-9">
			
	    	<!-- START :: 화상채팅 -->
	    	<div class="container">
				<h1>${sessionScope.roomInfo.room_name }</h1>
		
		        <div>
		            <video id="myVideo" width="400" height="300" autoplay></video>
		        </div>
		
				<div id="videos"></div>
		        
			</div>
	    	<!-- END :: 화상채팅 -->
	    	
		</div>
		<!-- END :: page - content -->
		
	</div>
	
	
	<script src='/resources/js/main.js'></script>
	
	<script type="text/javascript">
	
		$(function(){
		    console.log("${loginMember.memberid}");
		    connect("${loginMember.memberid}");
		 });
	
	</script>

</body>
</html>