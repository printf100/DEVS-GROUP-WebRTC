<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
	
<%@page isELIgnored="false" %>

<!-- START :: HEADER IMPORT -->
<%@ include file="form/header.jsp"%>
<!-- END :: HEADER IMPORT -->

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Insert title here</title>

<!--Bootstrap only for styling-->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
<!--Bootstrap only for styling-->

<!-- START :: CSS -->
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.css" type="text/css" rel="stylesheet"/>
<link href="/resources/css/chat.css" rel="stylesheet" type="text/css">
<!-- <link href="resources/css/master.css" rel="stylesheet" type="text/css"> -->
<style type="text/css">

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

#myVideo {	
	/* 화면 좌우반전 */
    transform: rotateY(180deg);
    -webkit-transform:rotateY(180deg); /* Safari and Chrome */
    -moz-transform:rotateY(180deg); /* Firefox */
}

#videos > video {
	width: 320px;
	height: 240px;
	
	/* 화면 좌우반전 */
    transform: rotateY(180deg);
    -webkit-transform:rotateY(180deg); /* Safari and Chrome */
    -moz-transform:rotateY(180deg); /* Firefox */
}

</style>
<!-- END :: CSS -->

</head>
<body>

	<div class="page-wrapper chiller-theme toggled">		
	
		<!-- START :: SIDEBAR FORM -->
		<%@ include file="form/sidebar.jsp"%>
		<!-- END :: SIDEBAR FORM -->
		
		<!-- START :: page - content -->
		<main class="page-content">
			<div class="container-fluid">
				<div class="page-content" id="channel_description">
					<h3 style="text-align: center;">${sessionScope.roomInfo.room_name }</h3>
					
					<div class="row">
						<!-- START :: 화상채팅 -->
						<div class="col-lg-9">
							<div>
								<video id="sharedscreen" width="800" height="600" style="display: none;" autoplay></video>
							</div>
					
					        <div id="myVideos">
					            <video id="myVideo" width="400" height="300" style="display: inline;" autoplay></video>
					            <video id="myScreenCapture" width="400" height="300" style="display: none;" autoplay></video>
					            <button type="button" id="share_screen" onclick="screenSharing();">화면공유하기</button>
					        </div>
					
				
							<div id="videos">
							</div>
							
						</div>
						<!-- END :: 화상채팅 -->
						
						<!-- START :: 채팅 -->
						<div class="col-lg-3">
							
							<section class="w-100 h-100">		
								
								<div class="messaging">
									<div class="inbox_msg">
																	
										<!-- START :: 채팅 메세지 리스트 -->
							          	<div class="mesgs">
		
							            	<div id="messages" class="msg_history scroll_fix_bottom">               
							            	</div>
							             
							            	<div class="type_msg">
								              	<div class="input_msg_write">
								              		<input type="hidden" name="room_code">				
								                	<input type="text" class="write_msg" id="messageinput" placeholder="메시지를 입력해주세요." />
					
								                	<button class="msg_send_btn" id="sendMessage" type="button"><i class="fa fa-pencil-square-o" aria-hidden="true"></i></button>
								              	</div>
							            	</div>
							          		
							          	</div>
							          	<!-- END :: 채팅 메세지 리스트 -->		
							          		
									</div>			
								</div>		
								
							</section>
						</div>
						<!-- END :: 채팅 -->
						
					</div>
					
				</div>
	    	</div>
		</main>		
		<!-- END :: page - content -->
		
	</div>
	
	<script src='/resources/js/webrtc_socket.js'></script>
	
	<script type="text/javascript">
	
		$(function() {
		    console.log("${loginMember.memberid}");
		    connect("${loginMember.memberid}");
		    
		    $("#share_screen").hide();
		});
		
		// 소켓으로부터받은 채팅 메시지 뿌리기
	    function writeChatMessage(message) {
			var chatmessage = message.data;
			var fromId = message.fromId;
			var writer_img = message.fromProfileImageName;
			
			console.log("fromId", fromId);
			console.log("chatmessage", chatmessage);
			
			var chat_container = $("<div>");
			var img_container = $("<div>").attr({"class":"incoming_msg_img"});
			var msg_container = $("<div>");
			
			if("${loginMember.memberid}" === fromId) {
				
				console.log("내가쓴글!!");

				chat_container.addClass("outgoing_msg");
				
				msg_container
					.append($("<div>").attr({"class":"sent_msg"})
						.append($("<p>").text(chatmessage))
					);
					
				chat_container.append(msg_container);
				
			} else {
				
				console.log("남이쓴글!!");
				chat_container.addClass("incoming_msg");
				msg_container.addClass("received_msg");
				
				img_container
					.append($("<img>").attr({"src":"/resources/images/profileupload/" + writer_img}))
					.append($("<span>").text(fromId));
				
					
				msg_container
					.append($("<div>").attr({"class":"received_withd_msg"})
						.append($("<p>").text(chatmessage))
						);
						
				chat_container.append(img_container).append(msg_container);
				
			}
			
			$("#messages").append(chat_container);
			
			$('.scroll_fix_bottom').scrollTop($('.scroll_fix_bottom').prop('scrollHeight'));
		}
	
	</script>
	
	<!-- START :: 소켓으로 보내는 메시지 -->	
	<script type="text/javascript">
		
		// 채팅 입력
		$(function() {
			
			$("#messageinput").keyup(function(e) {
				e.preventDefault();
				
				var messageinput = $("#messageinput").val();
				
				var code = e.keyCode ? e.keyCode : e.which;

				if(code == 13) {// 엔터키
					
					if(e.shiftKey == true) {// shift키 눌린 상태에서는 다음 라인으로
						
					} else {// 메세지전송
						
						if (messageinput != "" && messageinput != null) {
							sendChatMessage();
						}
					}
				
					return false;
				}
			});
			
			$("#sendMessage").click(function() {
				
				var messageinput = $("#messageinput").val();
				
				if (messageinput != "" && messageinput != null) {
					sendChatMessage();
				}
				
			});
			
		});
		
	    function sendChatMessage() {
	    	sendMessage({
				type : "chat",
				data : $("#messageinput").val()
			});
	    	
			$("#messageinput").val("");
	    }
	</script>
	<!-- END :: 소켓으로 보내는 메시지 -->

</body>
</html>