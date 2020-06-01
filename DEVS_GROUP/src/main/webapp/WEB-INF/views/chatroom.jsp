<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!-- START :: HEADER IMPORT -->
<%@ include file="form/header.jsp"%>
<!-- END :: HEADER IMPORT -->

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<!-- START :: css -->
	<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.css" type="text/css" rel="stylesheet"/>
	<link href="/resources/css/chat.css" rel="stylesheet" type="text/css">
	<style type="text/css">
	
		#write_image {
			/* 파일 필드 숨기기 */
			display: none;
		}

	</style>
<!-- END :: css -->
</head>

<body>
	
	<div class="page-wrapper chiller-theme toggled">
		
		<!-- START :: SIDEBAR FORM -->
		<%@ include file="form/sidebar.jsp"%>
		<!-- END :: SIDEBAR FORM -->
		
		<!-- START :: page - content -->
		<main class="page-content">	
			<div class="container-fluid">

		    		<!-- START :: 채팅 -->
		    		<div class="page-content" id="channel_description">
			    		<section class="container w-100 h-700">		
				    		<h4 style="text-align: center;">${chatRoomInfo.room_name }</h4>
							<div class="container">			
								<div class="messaging">
									<div class="inbox_msg">
																	
										<!-- START :: 채팅 메세지 리스트 -->
							          	<div class="mesgs">
		
							            	<div id="messages" class="msg_history scroll_fix_bottom">               
							            	</div>
							             
							            	<div class="type_msg">
								              	<div class="input_msg_write">
								              		<input type="hidden" name="room_code">				
								              	
								              		<!-- START :: 사진 전송하기 -->
													<div class="msg_send_btn" style="right: 40px; cursor: pointer;">
							              				<i class="fa fa-upload" style="padding: 6px 0px 0px 8px;" aria-hidden="true"></i>
										            	<form id="imageForm" action="/chat/sendImage" method="POST" enctype="multipart/form-data">
															<input id="write_image" type="file" name="write_image">					
														</form>
													</div>
													<!-- END :: 사진 전송하기 -->
													
								                	<input type="text" class="write_msg" id="messageinput" placeholder="메시지를 입력해주세요." />
		
								                	<button class="msg_send_btn" id="sendMessage" type="button"><i class="fa fa-pencil-square-o" aria-hidden="true"></i></button>
								              	</div>
							            	</div>
							          		
							          	</div>
							          	<!-- END :: 채팅 메세지 리스트 -->		
							          		
									</div>			
								</div>		
							</div>
							
							
						</section>
					</div>
			    	<!-- END :: 채팅 -->

		    </div>
		</main>
		<!-- END :: page - content -->
		
	</div>
	
</body>

<!-- START :: 사진 전송하기 -->
	<script type="text/javascript">
	
		$(".fa-upload").click(function() {			
			$("#write_image").click();
		});
		
		$("#write_image").change(function() {
			
			var ext = $('#write_image').val().split('.').pop().toLowerCase();
			
		  	if($.inArray(ext, ['gif','png','jpg','jpeg']) == -1) {		// 확장자 검사
		  		
		  		alert("이미지만 전송 가능합니다.");
		  	    $("#write_image").val("");
		  	    
		  	    return;
		  	    
		 	} else {
		 		
				var form = $("#imageForm")[0];
				var formData = new FormData(form);
				
				$.ajax({
					type: "POST",
					enctype: "multipart/form-data",
					url: "/chat/sendImage",
					processData: false,
					contentType: false,
					data: formData,
					dataType: "JSON",
					
					success: function(msg) {
						
						console.log(msg);
						$("#write_image").val("");
						
						sendPayloadMessage({
							type : "chat_send",
							data : {
								'room_code' : '${chatRoomInfo.room_code}',
								'message' : msg.send_image
							}
						});
						
					},
					
					error : function() {
						alert("통신 실패");
					}
				});
		 	}
		  	
		});
	</script>
<!-- END :: 사진 전송하기 -->

<!-- START :: 채팅방 입장 -->
	<script type="text/javascript">
		$(function() {
			
		    // 웹소켓 열림
		    ws.onopen=function(event) {

		    	// 소켓에 방에 입장했음을 알리는 메시지를 보냄
				sendEnterChatRoom();
		    };
			
			// 채팅 리스트 불러오기
			selectChatList('${chatRoomInfo.room_code}');

			// 스크롤 페이징
	        $('.scroll_fix_bottom').scroll(function() {
	        	let scrollTop = $(".scroll_fix_bottom").scrollTop();
	            
	            if(scrollTop == 0) {
	            	// 채팅 리스트 불러오기
	            	selectChatList('${chatRoomInfo.room_code}');
	         	}
	            
	        });
	         
		});
	</script>
<!-- END :: 채팅방 입장 -->

<!-- END :: 채팅방 퇴장 -->
	<script type="text/javascript">
		$(window).on('beforeunload', function() {
			alert("bye");
			sendOutChatRoom('${chatRoomInfo.room_code}');
		});
	</script> 
<!-- END :: 채팅방 퇴장 -->

<!-- START :: 소켓으로부터 도착한 메시지 핸들링 -->
	<script type="text/javascript">
		$(function(){
	        // 소켓으로부터 메시지 도착 시 (메시지의 attribute 이름에 따라 이벤트를 구분)
	        ws.onmessage=function(e){
	    		console.log('message', e.data);
	    		var message = JSON.parse(e.data);
            	
	            if (message.type == 'chat_enter') { // 이 채팅방에 다른 사람이 접속
	            	
	            	// 다른 접속자가 채팅창에 접속했음을 알림	
		            notifyUnreadChange(message.data.member_code);
	            	
	            } else if (message.type == 'chat_send'){ // 접속자의 메시지가 도착함 

		            findGroupChannelChatRoomList() // 채팅방 리스트 최신화
		            
		            // 소켓에서는 채팅룸 리스트에 메시지를 표시하기 위해 방에들어오지 않아도 참여자에게는 메시지를 보낸다.
		            // 이 때, 이 방에 들어와있는 사람에게는 메시지 출력을 한다!!
	            	if('${chatRoomInfo.room_code}' == message.data.new_chat.room_code){
		            	// 해당 채팅방을 열어놓은 상태라면 메시지 출력
			            writeChatMessage(message)
	            	}
	            }
	        };
		})
		
	    // 소켓으로부터받은 채팅 메시지 뿌리기
	    function writeChatMessage(message){
			var mdata = message.data.new_chat;
			var writer_img = message.data.writer_img
			
			console.log("mdata", mdata);
			console.log("writer_img", writer_img);
			
			var chat_container = $("<div>");
			var img_container = $("<div>").attr({"class":"incoming_msg_img"});
			var msg_container = $("<div>");
			
			if(mdata.chat_type == "invite"){ // 초대 메시지일 경우
				
				chat_container.addClass("invite_msg");
			
				msg_container
					.append($("<p>").attr({"class":"invite"}).text(mdata.message))
					.append($("<span>").attr({"data-unreadlist":mdata.unread_member_code_list}));
				
				chat_container.append(msg_container);

			} else { // 채팅 메시지일 경우
				
				// 내가 보낸 글
				if(${loginMember.membercode} === mdata.member_code){
					console.log("내가쓴글!!")
					chat_container.addClass("outgoing_msg");
					
					msg_container
						.append($("<div>").attr({"class":"sent_msg"})
							.append($("<span>").text(mdata.message_date))
							.append($("<p>").html(mdata.message))
							.append($("<span>").attr({"class":"unread", "data-unreadlist":mdata.unread_member_code_list}))
						)
						
					chat_container.append(msg_container);
				} 
				
				// 남이 보낸 글
				else {
					console.log("남이쓴글!!")
					chat_container.addClass("incoming_msg")
					msg_container.addClass("received_msg")
					
					img_container
						.append($("<img>").attr({"src":"/resources/images/profileupload/" + writer_img}))
					
						
					msg_container
						.append($("<div>").attr({"class":"received_withd_msg"})
							.append($("<span>").text(mdata.message_date))
							.append($("<p>").html(mdata.message))
							.append($("<span>").attr({"class":"unread", "data-unreadlist":mdata.unread_member_code_list}))
							)
							
					chat_container.append(img_container).append(msg_container)
				}
			}
			
			
			
			$("#messages").append(chat_container);
			setUnreadData();
			
			$('.scroll_fix_bottom').scrollTop($('.scroll_fix_bottom').prop('scrollHeight'));
		}

	    // 채팅리스트 전체에서 읽음표시 set
	    function setUnreadData(){
	    	var message = $("#messages").children();

	    	$.each(message, function(index, msg){
	    		var unread = $(this).find(".unread").attr("data-unreadlist");
	    		var unread_length;
	    		
	    		console.log(">>>" + unread);
	    		
				if(unread == "" || unread == undefined || unread == null){
					unread_length = '읽음'
				} else {
					unread_length = unread.split(',').length;
				}
				
	    		$(this).find(".unread").text(unread_length);  		
	    	})
	    }
	    
	    
	    function notifyUnreadChange(reader){
	    	var message = $("#messages").children();
	    	
	    	console.log(reader + " 번 멤버 입장!")
	    	
	    	$.each(message, function(index, msg){
	    		var unread = $(this).find(".unread").attr("data-unreadlist");    		
	    		
	    		var unread_list;
				if(unread == "" || unread == undefined || unread == null){
					unread_list = [];
				} else {
					unread_list = unread.split(',');
				}
				
	    		for(var i=0; i<unread_list.length; i++){
	    			
	    			if(unread_list[i] == reader){		    			
	    				unread_list.splice(i, 1);	 // 접속한 참여자가 unread_ilst에 존재한다면 삭제   				
	    				
		    			$(this).find(".unread").attr("data-unreadlist", unread_list);
	    				break;
	    			}
	    		}
	    	})
	    	
	    	setUnreadData()	    	
	    }
	</script>
<!-- END :: 소켓으로부터 도착한 메시지 핸들링 -->

<!-- START :: 소켓으로 보내는 메시지 -->	
	<script type="text/javascript">
		
		// 채팅 입력
		$(function() {
			$("#messageinput").keyup(function(e) {
				e.preventDefault();
				
				var messageinput = $("#messageinput").val();
				
				var code = e.keyCode ? e.keyCode : e.which;

				if(code == 13){// 엔터키
					
					if(e.shiftKey == true){// shift키 눌린 상태에서는 다음 라인으로
						
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
		
		// main.js 에서 최종적으로 메시지를 보낸다.
		function sendEnterChatRoom() {
			sendPayloadMessage({
				type : "chat_enter",
				data : {
					'room_code' : '${chatRoomInfo.room_code}'
				}
			});
		}
		
		function sendOutChatRoom() {
			sendPayloadMessage({
				type : "chat_out",
				data : {
					'room_code' : '${chatRoomInfo.room_code}'
				}
			});
		}
		
	    function sendChatMessage() {
	    	sendPayloadMessage({
				type : "chat_send",
				data : {
					'room_code' : '${chatRoomInfo.room_code}',
					'message' : $("#messageinput").val()
				}
			});
	    	
			$("#messageinput").val("");
	    }
	</script>
<!-- END :: 소켓으로 보내는 메시지 -->

<!-- START :: 채팅 리스트 가져오기 -->
	<script type="text/javascript">
		
		var isEnd = false;
		var startNo = 0; // MongoChatDao에서 조건식으로 사용, 무조건 시작은 0이어야함!!, 후에 불러온 채팅 리스트중 가장 작은 chat_code 입력
		var scrollLength;
	
		function selectChatList(room_code) {
			
			if(isEnd == true) {
				return;
			}	
			
			$.ajax({
				type: "post",
				url: "/chat/selectChatList",
				data: JSON.stringify({
					room_code : room_code, 
					startNo : startNo
				}),
				contentType: "application/json",
				dataType: "json",
				
				success: function(data) {
					
					if(data.length == 0 || data.length < 20){
						isEnd = true;
						console.log("채팅 메시지 끝!!");
					}
					
					// 채팅 리스트 뿌리기
					fillChatList(data);
					
					if(startNo == 0) {
						// 처음 스크롤 위치
						$('.scroll_fix_bottom').scrollTop($('.scroll_fix_bottom').prop('scrollHeight'));
						scrollLength = $('.scroll_fix_bottom').prop('scrollHeight');
					} else {
						//
						$('.scroll_fix_bottom').scrollTop($('.scroll_fix_bottom').prop('scrollHeight') - scrollLength);
						scrollLength = $('.scroll_fix_bottom').prop('scrollHeight');
					}
					
					startNo = data[data.length - 1].chat_code;
					
					console.log("chatListLength : ", data.length);
					console.log("startNo : ", startNo);
				},
				
				error: function() {
					alert("통신실패");
				}
			});
		}
	</script>
<!-- END :: 채팅 리스트 가져오기 -->

<!-- START :: 채팅 리스트 뿌리기-->
	<script type="text/javascript">
		function fillChatList(data){
			
			$.each(data, function(index, item){
				console.log("chat_message : ", item)

				var chat_container = $("<div>");
				var img_container = $("<div>").attr({"class":"incoming_msg_img"});
				var msg_container = $("<div>");
				
				
				if(item.chat_type == "invite") { // 초대 메시지일 경우
					
					chat_container.addClass("invite_msg");
				
					msg_container
						.append($("<p>").attr({"class":"invite"}).text(item.message))
						.append($("<span>").attr({"data-unreadlist":item.unread_member_code_list}));
						
					chat_container.append(msg_container);

				} else { // 채팅 메시지일 경우
				
					$.each(item.member_list, function(idx, member) {

						if(item.member_code === member.membercode) {
						
							if(${loginMember.membercode} === item.member_code) {
								chat_container.addClass("outgoing_msg");
								
								msg_container
									.append($("<div>").attr({"class":"sent_msg"})
										.append($("<span>").text(item.message_date))
										.append($("<p>").html(item.message))
										.append($("<span>").attr({"class":"unread", "data-unreadlist":item.unread_member_code_list}))
									);
									
								chat_container.append(msg_container);
								
							} else {
								chat_container.addClass("incoming_msg")
								msg_container.addClass("received_msg")
								
								img_container
									.append($("<img>").attr({"class":"rounded-circle", "src":"/resources/images/profileupload/" + member.member_img_server_name}));
								
								var unread_length = item.unread_member_code_list.length;
								if(unread_length == 0){
									unread_length = '읽음'
								}
								
								msg_container
									.append($("<div>").attr({"class":"received_withd_msg"})
										.append($("<span>").text(item.message_date))
										.append($("<p>").html(item.message))
										.append($("<span>").attr({"class":"unread", "data-unreadlist":item.unread_member_code_list}))
										);
										
								chat_container.append(img_container).append(msg_container);
							}
							
						}
					})
				}
				
				$("#messages").prepend(chat_container);
			})
			
			setUnreadData();
		}
	</script>
<!-- END :: 채팅 리스트 뿌리기-->

</html>