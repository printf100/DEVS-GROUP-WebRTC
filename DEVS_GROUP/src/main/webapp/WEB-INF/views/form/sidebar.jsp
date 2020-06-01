<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
	request.setCharacterEncoding("UTF-8");
%>
<%
	response.setContentType("text/html; charset=UTF-8");
%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="Responsive sidebar template with sliding effect and dropdown menu based on bootstrap 3">

	<title>Insert title here</title>
	
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link href="https://use.fontawesome.com/releases/v5.0.6/css/all.css" rel="stylesheet">
    
<!-- START :: css -->	
	<link href="/resources/css/sidebar.css" rel="stylesheet" type="text/css">
	<link href="/resources/css/tempstyle.css" rel="stylesheet" type="text/css">
	<style type="text/css">
	
	</style>
<!-- END :: css -->

<!-- START :: set JSTL variable -->
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!-- END :: set JSTL variable -->

<!-- START :: JAVASCRIPT -->
	<script type="text/javascript" src="/resources/js/sidebar.js"></script>
	
	<!-- START :: 그룹채널 프로필 이미지 수정 -->
	<script type="text/javascript">
		$(function(){
			
			$("#profile_image").click(function() {
				
				// 채널주인만 수정 가능
				if('${channel.membercode}' != '${loginMember.membercode}') {
					return false;
				}
				
				$("#memberImgOriginalName").click();
			});
			
			$("#memberImgOriginalName").change(function(e) {
				var form = $("#profileImageForm")[0];
				var formData = new FormData(form);
				
				$.ajax({
					type: "POST",
					enctype: "multipart/form-data",
					url: "/group/updatememberprofileimage",
					processData: false,
					contentType: false,
					data: formData,
					dataType: "JSON",
					success: function(msg) {
						$("#profile_image").attr("src","/resources/images/profileupload/" + msg.img);
					},
					error : function() {
						alert("통신 실패");
					}
				});
			});
			
		});
	</script>
	<!-- END :: 그룹채널 프로필 이미지 수정 -->
	
<!-- END :: JAVASCRIPT -->

</head>
<body>
	<div class="page-wrapper chiller-theme toggled">
	    
	    <a id="show-sidebar" class="btn btn-sm btn-dark" href="#">
	      <i class="fas fa-bars"></i>
	    </a>
	    
	    <nav id="sidebar" class="sidebar-wrapper row">	
	    		
    		<div class="col-3">
    			
    			<div id="my_group_channel_container">
    			</div>
    			
    			<div id="follow_group_channel_container">
    			</div>
    			
    			<div id="addChannel" onclick="goMainPage()">
    				<h1 style="color: white; background-color: gray; width: 100px; height: 100px;">
    					+
    				</h1>
    			</div>
    		</div>
    		
    		<div class="col-9">
    			
    			<div class="sidebar-content">
      
		      		<div class="sidebar-brand">
			        	<a href="/group/channel?channelcode=${channel.channelcode}">채널이름 : ${channel.channelname}</a>
			        	<div id="close-sidebar">
			            	<i class="fas fa-times"></i>
			        	</div>
			        </div>
		        
		        
			        <div class="sidebar-header">
			        
						<!-- START :: 멤버 프로필 이미지 -->
						<div class="user-pic">
			               <form id="profileImageForm" action="/DEVCA/member/privacyprofileimageupdate.do" method="POST" enctype="multipart/form-data">
			                  <input type="hidden" name="MEMBER_CODE" value="${member.MEMBER_CODE }">
			                  
			                  <div style="text-align: center;">            
			                     <img id="profile_image" src="
			                                       <c:choose>
			                                          <c:when test="${not empty loginProfile.memberImgServerName}">
			                                          	/resources/images/profileupload/${loginProfile.memberImgServerName }
			                                          </c:when>
			                                          <c:otherwise>
			                                          	/resources/images/profileupload/default.png
			                                          </c:otherwise>
			                                       </c:choose>   
			                                       ">
								<input id="memberImgOriginalName" type="file" name="memberImgOriginalName" value="${loginProfile.memberImgOriginalName }">					
			                     
			                  </div>
			                  
			               </form>
							  
						</div>
						<!-- END :: 멤버 프로필 이미지 -->
						
						<div class="user-info">
							<span class="user-name">${loginMember.memberid}</span>
							<span class="user-role">${loginMember.memberemail }</span>
							<span class="user-status">
								<i class="fa fa-circle"></i>
								<span>Online</span>
							</span>
						</div>
			        </div>		        
	
					<div class="sidebar-menu">
						<ul>
							<li class="header-menu">
								<span>Communication</span>
							</li>
							
							<li class="sidebar-dropdown">
								<a href="#">
									<i class="fa fa-tachometer-alt"></i>
									<span>Chatting</span>
									<span id="chatting_list_size" class="badge badge-pill badge-danger"></span>
								</a>
								<div class="sidebar-submenu">
									<ul id="chat_room_ul">
									</ul>
									
									<c:if test="${loginMember.membercode eq channel.membercode || follow.followerrole eq 'E'}">
										<ul>
											<li>
												<a data-toggle="modal" data-target="#makeNewChatRoom">+ Add a Chatting Room</a>
											</li>
										</ul>
									</c:if>
								</div>
								

								
							</li>
							
							<li class="sidebar-dropdown">
								<a href="#">
									<i class="fa fa-shopping-cart"></i>
									<span>WebRTC</span>
									<span id="rtc_list_size" class="badge badge-pill badge-danger"></span>
								</a>
								<div class="sidebar-submenu">
									<ul id="rtc_room_ul">
									</ul>
									
									<c:if test="${loginMember.membercode eq channel.membercode || follow.followerrole eq 'E'}">
										<ul>
											<li>
												<a data-toggle="modal" data-target="#makeNewRtcRoom">+ Add a Conference Room</a>
											</li>
										</ul>
									</c:if>
								</div>
								
								
								
							</li>
							
							<li class="header-menu">
								<span>People</span>
							</li>
							<li class="sidebar-dropdown">
								<a href="#">
									<i class="fa fa-shopping-cart"></i>
									<span>Editor</span>
									<span id="editor_list_size" class="badge badge-pill badge-danger"></span>
								</a>
								<div class="sidebar-submenu">
									<ul id="editor_list_container">
									</ul>
								</div>
							</li>
							<li class="sidebar-dropdown">
								<a href="#">
									<i class="fa fa-shopping-cart"></i>
									<span>Follower</span>
									<span id="follower_list_size" class="badge badge-pill badge-danger"></span>
								</a>
								<div class="sidebar-submenu">
									<ul id="follower_list_container">
									</ul>
								</div>
							</li>
							
							<li class="header-menu">
								<span>People</span>
							</li>
							<li>
								<a href="#">
									<i class="fa fa-book"></i>
									<span>Documentation</span>
									
								</a>
							</li>
							<li>
								<a href="#">
									<i class="fa fa-calendar"></i>
									<span>Calendar</span>
								</a>
							</li>
						</ul>
					</div>
				</div>	
    			
    		</div>			
	    	
		</nav>
	</div>
	
</body>

<script type="text/javascript">
	$(function() {
		if('${channel}' == null || '${channel}' == '') {
			$(".sidebar-content").empty();
			
			// 내가만든 그룹채널 리스트 가져오기
			selectMyGroupChannelList();
			
			// 팔로우한 그룹채널 리스트 가져오기
			selectFollowGroupChannelList();
			
		} else {
			
			// 내가만든 그룹채널 리스트 가져오기
			selectMyGroupChannelList();
			
			// 팔로우한 그룹채널 리스트 가져오기
			selectFollowGroupChannelList();
			
			// 이 채널의 채팅방 리스트 가져오기
			findGroupChannelChatRoomList();
			
			// 이 채널의 화상채팅방 리스트 가져오기
			findGroupChannelRtcRoomList();

			// 에디터 ROLE을 가진 팔로워 가져오기
			selectFollowerRoleEditer();
			
			// 팔로워 가져오기
			selectFollowerRoleReader();
			
			
			// 소켓으로부터 메시지 도착 시 (메시지의 attribute 이름에 따라 이벤트를 구분)
	        ws.onmessage=function(e) {
				
	    		console.log('message', e.data);
	    		var message = JSON.parse(e.data);
            	
	    		if (message.type == 'chat_send') { // 접속자의 메시지가 도착함 

		            findGroupChannelChatRoomList(); // 채팅방 리스트 최신화
					
	            }
	    		
	        };
	        
		}

	});
</script>

<!-- START :: 채널 입장하기 -->
	<script type="text/javascript">
		function openChannel(channelcode) {
			location.href = "/group/channel?channelcode=" + channelcode; 	
		}
	</script>
<!-- END :: 채널 입장하기 -->

<!-- START :: 메인 페이지로 이동 -->
	<script type="text/javascript">
		function goMainPage() {
			location.href = "/"; 
		}
	</script>
<!-- END :: 메인 페이지로 이동 -->

<!-- START :: 내가만든, 내가 팔로우한 채널 리스트 가져오기 -->
	<script type="text/javascript">
		function selectMyGroupChannelList() {
			$.ajax({
				type: "post",
				url: "/group/selectMyGroupChannelList",
				data: JSON.stringify({
					membercode : '${loginMember.membercode}'				
				}),
				contentType: "application/json",
				dataType: "json",
				
				success: function(data){
					console.log("myGroupChannel >>> ");
					console.log(data);
					$("#group-channel-container").empty();
					fillMyGroupChannelList(data);
				},
				
				error: function(){
					alert("통신실패");
				}
			});
		}
		
		function fillMyGroupChannelList(data) {
			$.each(data, function(index, item) {
				var div = null; 
				if ('${channel.channelcode}' == item.channelcode)
					div = $("<div>").attr({"class":"my_channel bg-primary width-100 height-100 overflow-hidden", "data-channelcode":item.channelcode});
				else
					div = $("<div>").attr({"class":"my_channel width-100 height-100 overflow-hidden", "data-channelcode":item.channelcode, "onclick":"openChannel(" + item.channelcode + ");"});
				
				var channel_name = $("<div>").attr({"class":"channel_name"});
				var channel_img = $("<div>").attr({"class":"channel_img"});
				
				if (item.channelimgservername != null) {
					
					channel_img.append($("<img>").attr({"class":"width-100 height-100", "src":"/resources/images/groupchannelprofileupload/" + item.channelimgservername}))
					div.append(channel_img);

				} else {
					
					channel_name.append($("<div>").attr({"style":"color: white;"}).text(item.channelname.charAt(0).toUpperCase()));
					div.append(channel_name);
					
				}
					
				$("#my_group_channel_container").append(div);
			});
		}
		
		/////////////////////////////////////////////////////////////////////////////
		
		function selectFollowGroupChannelList() {
			$.ajax({
				type: "post",
				url: "/group/selectFollowGroupChannelList",
				data: JSON.stringify({
					membercode : '${loginMember.membercode}'				
				}),
				contentType: "application/json",
				dataType: "json",
				
				success: function(data){
					console.log("followGroupChannel >>> ");
					console.log(data);
					$("#group-channel-container").empty();
					fillFollowGroupChannelList(data);
				},
				
				error: function() {
					alert("통신실패");
				}
			});
		}
		
		function fillFollowGroupChannelList(data) {
			$.each(data, function(index, item) {
				
				var div = null; 
				if ('${channel.channelcode}' == item.channelcode)
					div = $("<div>").attr({"class":"follow_channel bg-primary width-100 height-100 overflow-hidden", "data-channelcode":item.channelcode});
				else
					div = $("<div>").attr({"class":"follow_channel width-100 height-100 overflow-hidden", "data-channelcode":item.channelcode, "onclick":"openChannel(" + item.channelcode + ");"});
				
				var channel_name = $("<div>").attr({"class":"channel_name"});
				var channel_img = $("<div>").attr({"class":"channel_img"});
				
				if (item.channelimgservername != null) {
					
					channel_img.append($("<img>").attr({"class":"width-100 height-100", "src":"/resources/images/groupchannelprofileupload/" + item.channelimgservername}))
					div.append(channel_img);

				} else {
					
					channel_name.append($("<div>").attr({"style":"color: white;"}).text(item.channelname.charAt(0).toUpperCase()));
					div.append(channel_name);
					
				}
					
				$("#follow_group_channel_container").append(div);
			});
		}
	</script>
<!-- END :: 내가 팔로우한 채널 리스트 가져오기 -->

<!-- START :: 채팅방 리스트 가져오기 -->
	<script type="text/javascript">
		function findGroupChannelChatRoomList() {
			
			console.log("findGroupChannelChatRoomList (CHANNELCODE) >> ", '${channel.channelcode}');
			
			$.ajax({
				type: "post",
				url: "/chat/findGroupChannelChatRoomList",
				data: JSON.stringify({
					channelcode : '${channel.channelcode}'
				}),
				contentType: "application/json",
				dataType: "json",
				
				success: function(data){
					console.log("findGroupChannelChatRoomList >> ", data);
					$("#chat_room_ul").empty();
					fillChatRoomList(data);
				},
				
				error: function(){
					alert("통신실패");
				}
			});
		}
	</script>
<!-- END :: 채팅방 리스트 가져오기 -->

<!-- START :: 채팅방 리스트 뿌리기 -->
	<script type="text/javascript">
		function fillChatRoomList(data) {
			$("#chatting_list_size").text(data.length);
			$("#chat_room_ul").empty();
			
			$.each(data, function(index, item) {
				
				var chat_list = $("<li>").attr({"class":"chat_list", "data-roomcode":item.room_code, "onclick":"openChatRoom(" + item.room_code + ");"});
				var chat_room_name = $("<a>").attr({"class":"chat_room_name"}).text(item.room_name);
				var chat_people = $("<div>").attr({"class":"chat_people"});
				var chat_ib = $("<div>").attr({"class":"chat_ib"});
				
				var recent_message_member_id;
				$.each(item.member_list, function(idx, member) {		
					if(item.member_code == member.member_code) {
						recent_message_member_id = member.memberid; // 최신 메시지를 보낸 멤버의 아이디를 추출
					}
				});
				
				if(item.member_code != 0) {
					
					// 메세지 내용이 이미지 태그라면 '사진'이라는 글자로 대체
					var imgtag_exp = /<IMG(.*?)>/gi;
					if(item.message.match(imgtag_exp)) {
						item.message = item.message.replace(imgtag_exp, "사진");
					}
					
					chat_ib
						.append($("<h5>").text(recent_message_member_id))
						.append($("<p>").text(item.message)
								.append($("<span>").attr({"class":"chat_date float-right"}).text(getElapsedTime(item.message_date)))
								);
				}
					
				$("#chat_room_ul").append(chat_list.append(chat_room_name).append(chat_people.append(chat_ib)));
			})
		}
		
		function getElapsedTime(recent_date){
			var recent_year = recent_date.substr(0, 4);
			var recent_month = recent_date.substr(6, 2) - 1;
			var recent_day = recent_date.substr(10, 2);
			var recent_hour = recent_date.substr(14, 2);
			var recent_min = recent_date.substr(17, 2);

			var new_date = new Date();
			var old_date = new Date(recent_year, recent_month, recent_day, recent_hour, recent_min);
			
			var betweenDay = Math.floor((new_date.getTime() - old_date.getTime())/1000/60/60/24);
			var betweenHour = Math.floor((new_date.getTime() - old_date.getTime())/1000/60/60);
			var betweenMin = Math.floor((new_date.getTime() - old_date.getTime())/1000/60);
			
			return (betweenDay != 0) ? betweenDay+"일" : (betweenHour != 0) ? betweenHour+"시간" : (betweenMin != 0) ? betweenMin+"분" : "방금";
		}
	</script>
<!-- END :: 채팅방 리스트 뿌리기 -->
	
<!-- START :: 채팅방 생성 -->
	<script type="text/javascript">
		var jsonArray = new Array();
		
		// 에디터들 기본으로 넣어주고~ (에디터 리스트 불러오기에서 호출함!)
		function setChatRoomEditorList(data) {
			var jsonTempArray = new Array();

			// 채널주인은 기본으로 넣어주고~
			var json = new Object();
			json.membercode = ${channel.membercode};
			jsonTempArray.push(json);
			
			console.log("채널주인", json)
			
			// 에디터들의 리스트를 add
			$.each(data, function(index, item) {
				var json = new Object();
				json.membercode = item.membercode;
				jsonTempArray.push(json);
			})
			
			jsonArray = jsonTempArray;
		}
		
		function makeChatRoom(){
			console.log("makeChatRoom : " + JSON.stringify(jsonArray));
			
			// 방 이름을 jsonArray에 넣어서 controller에 전달 ,,, jackson이 List<Map<String, Object>> 타입으로 파싱
			var json = new Object();
			json.room_name = $("#chatRoomName").val();

			jsonArray.push(json);
			
			console.log("만들어질 방 데이터", jsonArray);
			
			$.ajax({
				type: "post",
				url: "/chat/makeChatRoom",
				data: JSON.stringify(jsonArray),
				contentType: "application/json",
				dataType: "json",
				
				success: function(msg) {
					console.log(msg.insertedChatRoom);
					
					var data = new Array();
					data.push(msg.insertedChatRoom);
					fillChatRoomList(data);
					
					////////////////////////////////////////////////////////////////////////
					////////////////////////////////////////////////////////////////////////
					////////////////////////////////////////////////////////////////////////
					// 채팅방 생성 알림
					sendAlarmMessage({
						type : "make_chat_room",
						data : {
							channelcode : ${channel.channelcode}
						}
					});
					////////////////////////////////////////////////////////////////////////
					////////////////////////////////////////////////////////////////////////
					////////////////////////////////////////////////////////////////////////
					
					openChatRoom(msg.insertedChatRoom.room_code);
				},
				
				error: function() {
					alert("통신실패");
				}
			});
		}
	</script>
<!-- END :: 채팅방 생성  -->

<!-- START :: 채팅방 입장하기  -->
	<script type="text/javascript">
		function openChatRoom(room_code) {
			location.href="/chat/chatroom?room_code=" + room_code;
		}
	</script>
<!-- END :: 채팅방 입장하기 -->


<!-- START :: 화상채팅방 리스트 가져오기 -->
	<script type="text/javascript">
		function findGroupChannelRtcRoomList() {
			$.ajax({
				type: "post",
				url: "/rtc/findGroupChannelRtcRoomList",
				data: JSON.stringify({
					channelcode : '${channel.channelcode}'
				}),
				contentType: "application/json",
				dataType: "json",
				
				success: function(data) {
					console.log(data);
					$("#rtc_room_ul").empty();
					fillRtcRoomList(data);
				},
				
				error: function(){
					alert("통신실패");
				}
			})
		}
	</script>
<!-- END :: 화상채팅방 리스트 가져오기 -->

<!-- START :: 화상채팅방 리스트 뿌리기 -->
	<script type="text/javascript">
		function fillRtcRoomList(data){
			$("#rtc_list_size").text(data.length);
			
			$.each(data, function(index, item) {
				
				var chat_list = $("<li>").attr({"class":"rtc_list", "data-roomcode":item.room_code, "onclick":"openRtcRoom(" + item.room_code + ");"});
				var chat_room_name = $("<a>").attr({"class":"rtc_room_name"}).text(item.room_name);
					
				$("#rtc_room_ul").append(chat_list.append(chat_room_name));
			});
		}
	</script>
<!-- END :: 화상채팅방 리스트 뿌리기 -->
	
<!-- START :: 화상채팅방 생성 -->
	<script type="text/javascript">
		function makeRtcRoom(){
			
			var newRtcRoomName = $("#rtcRoomName").val();
			
			$.ajax({
				type: "post",
				url: "/rtc/makeRtcRoom",
				data:JSON.stringify({
					room_name : newRtcRoomName
				}),
				contentType: "application/json",
				dataType: "json",
				
				success: function(msg) {
					console.log(msg.insertedRtcRoom);
					
					var data = new Array();
					data.push(msg.insertedRtcRoom);
					fillRtcRoomList(data);
					
					//////////////////////////////////////////////////////////////////////////////////////
					//////////////////////////////////////////////////////////////////////////////////////
					//////////////////////////////////////////////////////////////////////////////////////
					// 화상채팅방 생성 알림
					sendAlarmMessage({
						type : "make_rtc_room",
						data : {
							channelcode : ${channel.channelcode} 
						}
					});
					//////////////////////////////////////////////////////////////////////////////////////
					//////////////////////////////////////////////////////////////////////////////////////
					//////////////////////////////////////////////////////////////////////////////////////
					
					// 만들고 바로 입장
					openRtcRoom(msg.insertedRtcRoom.room_code);
				},
				
				error: function() {
					alert("통신실패");
				}
			});
		}
	</script>
<!-- END :: 화상채팅방 생성  -->

<!-- START :: 화상채팅방 입장하기  -->
	<script type="text/javascript">
		function openRtcRoom(room_code) {
			location.href="/rtc/rtcroom?room_code=" + room_code;
		}
	</script>
<!-- END :: 화상채팅방 입장하기 -->


<!-- START :: 에디터 리스트 가져오기 -->
	<script type="text/javascript">
		function selectFollowerRoleEditer() {
			$.ajax({
				type: "post",
				url: "/group/selectFollowerRoleEditer",
				data: JSON.stringify({
					channelcode : '${channel.channelcode}'		
				}),
				contentType: "application/json",
				dataType: "json",
				
				success: function(data) {
					console.log(">>> EditorList");
					console.log(data);
					$("#editor_list_container").empty();
					fillEditorList(data);
					
					// 채팅방 만들기 명단에 에디터권한의 팔로워들을 전부 넣는다.
					setChatRoomEditorList(data);
				},
				
				error: function() {
					alert("통신실패");
				}
			});
		}
	</script>
<!-- END :: 에디터 리스트 가져오기 -->

<!-- START :: 에디터 리스트 뿌리기 -->
	<script type="text/javascript">
		function fillEditorList(data) {
			
			console.log(">>> fillEditorList");
			console.log(data);
			
			$("#editor_list_size").text(data.length);
			$("#editor_list_container").empty();
			
			$.each(data, function(index, item) {
				
				var li_item = $("<li>").attr({"class":"editor d-flex align-items-center ml-2"});
								
				li_item.append($("<img>").attr({
									"class" : "rounded-circle m-1 w-40 h-40 bg-white vertical-align-baseline",
									"src" : '/resources/images/profileupload/' + item.image
								}))
								.append($("<span>").attr({"class":"name mx-1"}).text(item.membername))
								.append($("<span>").attr({"class":"id mx-1"}).text(item.memberid))
								.append($("<span>").attr({"class":"email mx-1"}).text(item.memberemail));
				
				// 채널 editor online 여부 표시
				var isOnline = false;
				
                for(var i=0; i < LoginIdList.length; i++) {
                   if(item.memberid == LoginIdList[i])
                      isOnline = true;
                }
                
                if(isOnline) {
                   li_item.append($("<i>").attr({"class":"fa fa-circle mx-1", "style":"color:green; font-size: 8px;"}));
                } else {
                   li_item.append($("<i>").attr({"class":"fa fa-circle mx-1", "style":"color:gray; font-size: 8px;"}));
                }
				
				$("#editor_list_container").append(li_item);
			});
		}
	</script>
<!-- END :: 에디터 리스트 뿌리기 -->

<!-- START :: 팔로워 리스트 가져오기 -->
	<script type="text/javascript">
		function selectFollowerRoleReader() {
			$.ajax({
				type: "post",
				url: "/group/selectFollowerRoleReader",
				data: JSON.stringify({
					channelcode : '${channel.channelcode}'		
				}),
				contentType: "application/json",
				dataType: "json",
				
				success: function(data) {
					console.log(">>> FollowerList");
					console.log(data);
					$("#follower_list_container").empty();
					fillReaderList(data);
				},
				
				error: function() {
					alert("통신실패");
				}
			});
		}
	</script>
<!-- END :: 팔로워 리스트 가져오기 -->

<!-- START :: 팔로워 리스트 뿌리기 -->
	<script type="text/javascript">
		function fillReaderList(data) {
			$("#follower_list_size").text(data.length);
			
			$.each(data, function(index, item) {
				
				var li_item = $("<li>").attr({"class":"follower d-flex align-items-center ml-2"});
								
				li_item.append($("<img>").attr({
									"class" : "rounded-circle m-1 w-40 h-40 bg-white vertical-align-baseline",
									"src" : '/resources/images/profileupload/' + item.image
								}))
								.append($("<span>").attr({"class":"name mx-1"}).text(item.membername))
								.append($("<span>").attr({"class":"id mx-1"}).text(item.memberid))
								.append($("<span>").attr({"class":"email mx-1"}).text(item.memberemail))
								.append($("<button>").attr({"class":"change_role mx-1", "onclick":"changeFollowerRole(" + item.membercode + ");"}).text("upgrade"));
				
				// 채널 editor online 여부 표시
				var isOnline = false;
				
                for(var i=0; i < LoginIdList.length; i++) {
                   if(item.memberid == LoginIdList[i])
                      isOnline = true;
                }
                
                if(isOnline) {
                   li_item.append($("<span>").attr({"class":"fa fa-circle mx-1", "style":"color:green; font-size: 8px;"}));
                } else {
                   li_item.append($("<span>").attr({"class":"fa fa-circle mx-1", "style":"color:gray; font-size: 8px;"}));
                }
								
				$("#follower_list_container").append(li_item);
			});
		}
		
		// 팔로워 -> 에디터 role 바꿔주기
		function changeFollowerRole(membercode) {
			$.ajax({
				type: "post",
				url: "/group/changeFollowerRole",
				data: JSON.stringify({
					channelcode : '${channel.channelcode}',
					membercode : membercode
				}),
				contentType: "application/json",
				dataType: "json",
				
				success: function(data) {
					
					if(data.res) {
						console.log("에디터로 바꾸기 성공");
						
						///////////////////////////////////////////////////////////////////////
						///////////////////////////////////////////////////////////////////////
						///////////////////////////////////////////////////////////////////////
						// 에디터로 바뀐 알람 주기
						sendAlarmMessage({
							type : "change_channel",
							fromId : '${loginMember.memberid}',
							data : {
								'channelcode' : ${channel.channelcode}
							}
						});
						///////////////////////////////////////////////////////////////////////
						///////////////////////////////////////////////////////////////////////
						///////////////////////////////////////////////////////////////////////
						
						// 에디터 추가된 멤버 채팅방에 추가하기
			    		$.ajax({
							type: "post",
							url: "/chat/findChannelChatRoomList",
							data: JSON.stringify({
								channelcode : '${channel.channelcode}'
							}),
							contentType: "application/json",
							dataType: "json",
							
							success: function(data) {
								
								$.each(data, function(index, item) {
									
									var room_code = item.room_code;
									// 초대받은 사람 = membercode
									
							    	sendPayloadMessage({
										type : "chat_invite",
										data : {
											'room_code' : room_code,
											'invited_member_code' : membercode
										}
									});
									
								})
								console.log("chatRoomList on this Channel >> ", data.chatRoomList)
							},
							
							error: function() {
								alert("통신실패");
							}
						});
						
					} 
				},
				
				error: function() {
					alert("통신실패");
				}
			});
		}
	</script>
<!-- END :: 팔로워 리스트 뿌리기 -->

<!-- START :: 알람 메시지 보내기 -->
	<script type="text/javascript">
		$(function() {
			
    		// (alarm.js , AlarmSocketHandler) 알람 소켓에 채널접속 메시지 보냄 -> 같은 채널에 있는 접속자들에게 메시지 
   		    alarm.onopen = function(event) {	    
    			
    			if('${channel.channelcode}' != null && '${channel.channelcode}' != "") {
		    		sendAlarmMessage({
						type : "change_channel",
						fromId : '${loginMember.memberid}',
						data : {
							'channelcode' : ${channel.channelcode}
						}
		    		});
    			}
    		};
    		
		});
	</script>
<!-- END :: 알람 메시지 보내기 -->

<!-- START :: 알람 소켓 온메시지 핸들러 -->
	<script type="text/javascript">
		var LoginIdList;
		
		$(function() {
		    alarm.onmessage = function(e) {
				console.log('message', e.data);
				var message = JSON.parse(e.data);
				
			    // 1. 유저 채널접속 알람 (channel.jsp/$(function(){}) 에서 보낸 메시지)
				if(message.type == 'change_channel') {
					console.log("change_channel")
					LoginIdList = message.loginIds;
					selectFollowerRoleEditer();
					selectFollowerRoleReader();
				} 
				
			    // 3. webrtc room 생성 알람
				else if(message.type == 'make_chat_room') {
					console.log("make_chat_room")
					findGroupChannelChatRoomList();
				} 
			    
				// 4. reader -> editor 변경 알람
				else if(message.type == 'make_rtc_room') {
					console.log("make_rtc_room")
					findGroupChannelRtcRoomList();
				}
		    	
		    }
		})
	</script>
<!-- END :: 알람 소켓 온메시지 핸들러 -->

</html>