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

.videos {
	display: flex;
	flex-wrap: wrap;
}

.videos > div {
	width: 50%;
}

video {
	width: 320px;
	height: 240px;
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
	
<!-- 		<div id="myView"> -->
<!-- 		  <video id="myVideo" width="320" height="240" autoplay style="display: inline;"></video> -->
<!-- 		</div> -->

<!-- 		<div id="remoteViews"> -->
<!-- 		   <video id="remoteView" width="320" height="240" autoplay="autoplay" style="display: inline;"></video> -->
<!-- 		</div> -->

		<div class="videos">
            <div>
                <video id="myVideo" autoplay muted playsinline></video>
            </div>
        </div>
        
        <div id="connections"></div>

<!-- 		<div id="divStartRTC"> -->
<!-- 			<button id="startRTC" onclick="startRTC();">화상채팅 시작하기</button> -->
<!-- 			<input id="participant" type="text"><button onclick="getOffer();">초대하기</button> -->
<!-- 		</div> -->
	
	</div>
	
	<script src='/resources/js/adapter-latest.js'></script>
	<script src='/resources/js/main2.js'></script>
	
	<script type="text/javascript">
	
		var HEAD = "";
		
		$(function(){
		    console.log("${loginMember.memberid}");
		    connect("${loginMember.memberid}");
		    
		 	$("#startRTC").click(function() {
		 		HEAD = MEMBER_ID;	// 방장 아아디 셋팅
		 	});
		 	
// 		 	if(HEAD == "" || HEAD != MEMBER_ID) {
// 		 		$("#divStartRTC").hide();
// 		 	}
		 });
		 
		 function getOffer(){
		    var participant = $("#participant").val();
		    
		    offer(participant);
		 }
		 
		 function addMyView(stream) {
			 console.log("내 화면 입력");
			 
			 var myVideo = $("video").attr({
					"id" : "myVideo",
					"width" : "320",
					"height" : "240",
					"autoplay" : "autoplay",
					"style" : "display: inline;"
			})
				
			$("#myView").append(myVideo);
			 
			 myVideo.srcObject = stream;
		 }
		 
		 var newVideoId = 0;
		 function addNewPeer(stream) {
			 console.log("상대방 화면 입력");

			 ++newVideoId;
			 var newVideo = $("video").attr({
					"id" : newVideoId,
					"width" : "320",
					"height" : "240",
					"autoplay" : "autoplay",
					"style" : "display: inline;"
				})
				
				$("#remoteViews").append(newVideo);
			 
				newVideoId.srcObject = stream;
		 }
	
	</script>

</body>
</html>