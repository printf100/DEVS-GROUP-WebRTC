<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<!-- START :: HEADER FORM -->
	<%@ include file="form/header.jsp"%>
<!-- END :: HEADER FORM -->

<!-- START :: css -->
	<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.css" type="text/css" rel="stylesheet"/>
	<link href="/resources/css/chat.css" rel="stylesheet" type="text/css">
	<style type="text/css">
	</style>
<!-- END :: css -->
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
			
	    	<!-- START :: 채팅 -->
	    		<section class="container w-75 h-700">		
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
						                	<input type="text" class="write_msg" id="messageinput" placeholder="메시지 입력..." />
			
						                	<button class="msg_send_btn" id="sendMessage" type="button" onclick="send();"><i class="fa fa-paper-plane-o" aria-hidden="true"></i></button>
						              	</div>
					            	</div>
					          		
					          	</div>
					          	<!-- END :: 채팅 메세지 리스트 -->		
					          		
							</div>			
						</div>		
					</div>
					
					
				</section>
	    	<!-- END :: 채팅 -->
	    	
		</div>
		<!-- END :: page - content -->
		
	</div>
	
</body>
<!-- START :: 채팅방 입장 -->
	<script type="text/javascript">
		$(function(){
			openChatRoom('${room_code}')
		})
	</script>
<!-- END :: 채팅방 입장 -->

<!-- END :: 채팅방 퇴장 -->
	<script type="text/javascript">
		$(window).on('beforeunload', function(){
			alert("bye")	
			sendOutChatRoom('${room_code}');
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
	            	
	            	// 해당 채팅방을 열어놓은 상태라면 메시지 출력
		            writeChatMessage(message)
	            
					findMyChatRoomList() // 채팅방 리스트 최신화
					
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
			
			if(${loginMember.membercode} === mdata.member_code){
				console.log("내가쓴글!!")
				chat_container.addClass("outgoing_msg");
				
				msg_container
					.append($("<div>").attr({"class":"sent_msg"})
						.append($("<span>").text(mdata.message_date))
						.append($("<p>").text(mdata.message))
						.append($("<span>").attr({"class":"unread", "data-unreadlist":mdata.unread_member_code_list}))
					)
					
				chat_container.append(msg_container);
			} else {
				console.log("남이쓴글!!")
				chat_container.addClass("incoming_msg")
				msg_container.addClass("received_msg")
				
				img_container
					.append($("<img>").attr({"src":"/resources/images/profileupload/" + writer_img}))
				
					
				msg_container
					.append($("<div>").attr({"class":"received_withd_msg"})
						.append($("<span>").text(mdata.message_date))
						.append($("<p>").text(mdata.message))
						.append($("<span>").attr({"class":"unread", "data-unreadlist":mdata.unread_member_code_list}))
						)
						
				chat_container.append(img_container).append(msg_container)
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
	    		
				if(unread == ""){
					unread_length = '읽음'
				} else {
					unread_length = unread.split(',').length;
				}
				
	    		$(this).find(".unread").text(unread_length)	    		
	    	})
	    }
	    
	    
	    function notifyUnreadChange(reader){
	    	var message = $("#messages").children();
	    	
	    	console.log(reader + " 번 멤버 입장!")
	    	
	    	$.each(message, function(index, msg){
	    		var unread = $(this).find(".unread").attr("data-unreadlist");    		
	    		
	    		var unread_list = unread.split(',');
	    		
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
		
	// 채팅방열기
		function openChatRoom(room_code){
			
			// 소켓에 방에 입장했음을 알리는 메시지를 보냄
			sendEnterChatRoom();
			
			// 채팅 리스트 불러오기
			selectChatList(room_code);
			
		}
		
		// 채팅 입력
		$(function(){
			$("#messageinput").keyup(function(e){
				e.preventDefault();
				
				var messageinput = $("#messageinput").val();
				
				var code = e.keyCode ? e.keyCode : e.which;

				if(code == 13){// 엔터키
					
					if(e.shiftKey == true){// shift키 눌린 상태에서는 다음 라인으로
						
					} else {// 메세지전송
						sendChatMessage();
					}
				
					return false;
				}
			})
		})
		
		// main.js 에서 최종적으로 메시지를 보낸다.
		function sendEnterChatRoom(){
			sendPayloadMessage({
				type : "chat_enter",
				data : {
					'room_code' : '${room_code}'
				}
			});
		}
		function sendOutChatRoom(){
			sendPayloadMessage({
				type : "chat_out",
				data : {
					'room_code' : '${room_code}'
				}
			});
		}
	    function sendChatMessage(){
	    	sendPayloadMessage({
				type : "chat_send",
				data : {
					'room_code' : '${room_code}',
					'message' : $("#messageinput").val()
				}
			});
			$("#messageinput").val("");
			findGroupChanelChatRoomList() // 채팅방 리스트 최신화
	    }
	</script>
<!-- END :: 소켓으로 보내는 메시지 -->

<!-- START :: 채팅 리스트 가져오기 -->
	<script type="text/javascript">
		function selectChatList(room_code){
			$.ajax({
				type: "post",
				url: "/chat/selectChatList",
				data: JSON.stringify({
					room_code : room_code				
				}),
				contentType: "application/json",
				dataType: "json",
				
				success: function(data){
					fillChatList(data);
				},
				
				error: function(){
					alert("통신실패");
				}
			})
		}
	</script>
<!-- END :: 채팅 리스트 가져오기 -->

<!-- START :: 채팅 리스트 뿌리기-->
	<script type="text/javascript">
		function fillChatList(data){
			$("#messages").empty();
			
			$.each(data, function(index, item){
				console.log("chat_message : ", item)

				var chat_container = $("<div>");
				var img_container = $("<div>").attr({"class":"incoming_msg_img"});
				var msg_container = $("<div>");
				
				$.each(item.member_list, function(idx, member){					
					
					if(item.member_code === member.membercode){
					
						if(${loginMember.membercode} === item.member_code){
							chat_container.addClass("outgoing_msg");
							
							msg_container
								.append($("<div>").attr({"class":"sent_msg"})
									.append($("<span>").text(item.message_date))
									.append($("<p>").text(item.message))
									.append($("<span>").attr({"class":"unread", "data-unreadlist":item.unread_member_code_list}))
								)
								
							chat_container.append(msg_container);
							
						} else {
							chat_container.addClass("incoming_msg")
							msg_container.addClass("received_msg")
							
							img_container
								.append($("<img>").attr({"class":"rounded-circle", "src":"/resources/images/profileupload/" + member.member_img_server_name}))
							
							var unread_length = item.unread_member_code_list.length;
							if(unread_length == 0){
								unread_length = '읽음'
							}
							
							msg_container
								.append($("<div>").attr({"class":"received_withd_msg"})
									.append($("<span>").text(item.message_date))
									.append($("<p>").text(item.message))
									.append($("<span>").attr({"class":"unread", "data-unreadlist":item.unread_member_code_list}))
									)
									
							chat_container.append(img_container).append(msg_container)
						}
						
					}
				})
				
				$("#messages").append(chat_container);
			})
			
			setUnreadData();
			$('.scroll_fix_bottom').scrollTop($('.scroll_fix_bottom').prop('scrollHeight'));
		}
	</script>
<!-- END :: 채팅 리스트 뿌리기-->

</html>