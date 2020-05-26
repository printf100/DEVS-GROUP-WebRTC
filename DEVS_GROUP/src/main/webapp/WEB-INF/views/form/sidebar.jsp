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
	<script type="text/javascript">
	
	</script>
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
			        	<a href="#">채널이름 : ${channel.channelname}</a>
			        	<div id="close-sidebar">
			            	<i class="fas fa-times"></i>
			        	</div>
			        </div>
		        
		        
			        <div class="sidebar-header">
						<div class="user-pic">
							<img class="img-responsive img-rounded"
							  src="/resources/images/profileupload/${loginProfile.memberImgServerName }"
							  alt="User picture">
						</div>
						<div class="user-info">
							<span class="user-name">${loginMember.memberid}</span>
							<span class="user-role">${loginMember.memberemail }</span>
							<span class="user-status">
								<i class="fa fa-circle"></i>
								<span>Online</span>
							</span>
						</div>
			        </div>
		        
		        
					<div class="sidebar-search">
						<div>
							<div class="input-group">
								<input type="text" class="form-control search-menu" placeholder="Search...">
								<div class="input-group-append">
									<span class="input-group-text">
										<i class="fa fa-search" aria-hidden="true"></i>
									</span>
								</div>
							</div>
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
												<a href="javascript:makeChatRoom();">+ Add a Chatting Room</a>
											</li>
										</ul>
									</c:if>
								</div>
							</li>
							
							<li class="sidebar-dropdown">
								<a href="#">
									<i class="fa fa-shopping-cart"></i>
									<span>WebRTC</span>
									<span id="rtc_list_size" class="badge badge-pill badge-danger">3</span>
								</a>
								<div class="sidebar-submenu">
									<ul id="">
										<li>
											<a href="#">Example</a>
										</li>
										<li>
											<a href="#">Example</a>
										</li>
										<li>
											<a href="#">Example</a>
										</li>
									</ul>
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
	$(function(){
		if('${channel}' == null || '${channel}' == ''){
			$(".sidebar-content").empty();
			
			// 내가만든 그룹채널 리스트 가져오기
			selectMyGroupChannelList()
			
			// 팔로우한 그룹채널 리스트 가져오기
			selectFollowGroupChannelList()
		} else {
			
			// 내가만든 그룹채널 리스트 가져오기
			selectMyGroupChannelList()
			
			// 팔로우한 그룹채널 리스트 가져오기
			selectFollowGroupChannelList()
			
			// 이 채널의 채팅방 리스트 가져오기
			findGroupChanelChatRoomList()

			// 에디터 ROLE을 가진 팔로워 가져오기
			selectFollowerRoleEditer();
			
			// 팔로워 가져오기
			selectFollowerRoleReader();
		}
		

	})
</script>

<!-- START :: 채널 입장하기 -->
	<script type="text/javascript">
		function openChannel(channelcode){
			location.href = "/?channelcode=" + channelcode; 	
		}
	</script>
<!-- END :: 채널 입장하기 -->

<!-- START :: 메인 페이지로 이동 -->
	<script type="text/javascript">
		function goMainPage(){
			location.href = "/"; 	
		}
	</script>
<!-- END :: 메인 페이지로 이동 -->

<!-- START :: 내가만든, 내가 팔로우한 채널 리스트 가져오기 -->
	<script type="text/javascript">
		function selectMyGroupChannelList(){
			$.ajax({
				type: "post",
				url: "/group/selectMyGroupChannelList",
				data: JSON.stringify({
					membercode : '${loginMember.membercode}'				
				}),
				contentType: "application/json",
				dataType: "json",
				
				success: function(data){
					console.log("myGroupChannel >>> ")
					console.log(data)
					$("#group-channel-container").empty();
					fillMyGroupChannelList(data)
				},
				
				error: function(){
					alert("통신실패");
				}
			})
		}
		
		function fillMyGroupChannelList(data){
			$.each(data, function(index, item){
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
			})
		}
		
		/////////////////////////////////////////////////////////////////////////////
		
		function selectFollowGroupChannelList(){
			$.ajax({
				type: "post",
				url: "/group/selectFollowGroupChannelList",
				data: JSON.stringify({
					membercode : '${loginMember.membercode}'				
				}),
				contentType: "application/json",
				dataType: "json",
				
				success: function(data){
					console.log("followGroupChannel >>> ")
					console.log(data)
					$("#group-channel-container").empty();
					fillFollowGroupChannelList(data)
				},
				
				error: function(){
					alert("통신실패");
				}
			})
		}
		
		function fillFollowGroupChannelList(data){
			$.each(data, function(index, item){
				
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
			})
		}
	</script>
<!-- END :: 내가 팔로우한 채널 리스트 가져오기 -->

<!-- START :: 채팅방 리스트 가져오기 -->
	<script type="text/javascript">
		function findGroupChanelChatRoomList(){
			$.ajax({
				type: "post",
				url: "/chat/findGroupChanelChatRoomList",
				data: JSON.stringify({
					channelcode : '${channel.channelcode}'
				}),
				contentType: "application/json",
				dataType: "json",
				
				success: function(data){
					console.log(data)
					$("#chat_room_ul").empty();
					fillChatRoomList(data)
				},
				
				error: function(){
					alert("통신실패");
				}
			})
		}
	</script>
<!-- END :: 채팅방 리스트 가져오기 -->

<!-- START :: 채팅방 리스트 뿌리기 -->
	<script type="text/javascript">
		function fillChatRoomList(data){
			$("#chatting_list_size").text(data.length);
			
			$.each(data, function(index, item){
				
				var chat_list = $("<li>").attr({"class":"chat_list", "data-roomcode":item.room_code, "onclick":"openChatRoom(" + item.room_code + ", this);"});
				var chat_room_name = $("<a>").attr({"class":"chat_room_name"}).text(item.room_name);
				var chat_people = $("<div>").attr({"class":"chat_people"});
				var chat_ib = $("<div>").attr({"class":"chat_ib"});
				
				var recent_message_member_id;
				$.each(item.member_list, function(idx, member){		
					if(item.member_code == member.member_code){
						recent_message_member_id = member.member_id; // 최신 메시지를 보낸 멤버의 아이디를 추출
					}
				})
				
				if(item.member_code != 0){
					chat_ib
						.append($("<h5>").text(recent_message_member_id))
						.append($("<p>").text(item.message)
								.append($("<span>").attr({"class":"chat_date float-right"}).text(getElapsedTime(item.message_date)))
								)
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
			
			return (betweenDay != 0) ? betweenDay+"일" : (betweenHour != 0) ? betweenHour+"시" : (betweenMin != 0) ? betweenMin+"분" : "방금";
		}
	</script>
<!-- END :: 채팅방 리스트 뿌리기 -->
	
<!-- START :: 채팅방 생성 -->
	<script type="text/javascript">
		var jsonArray = new Array();
		var json = new Object()
		
		// 채널주인은 기본으로 넣어주고~
		json.membercode = ${channel.membercode}			
		jsonArray.push(json)
		
		// 에디터들 기본으로 넣어주고~ (에디터 리스트 불러오기에서 호출함!)
		function setChatRoomEditorList(data){
			$.each(data, function(index, item){
				var json = new Object()
				json.membercode = ${item.membercode}			
				jsonArray.push(json)
			})
		}
		
		function makeChatRoom(){
			console.log("makeChatRoom : " + JSON.stringify(jsonArray))
			
			$.ajax({
				type: "post",
				url: "/chat/makeChatRoom",
				data: JSON.stringify(jsonArray),
				contentType: "application/json",
				dataType: "json",
				
				success: function(msg){
					console.log(msg.insertedChatRoom);
					
					var data = new Array();
					data.push(msg.insertedChatRoom)
					fillChatRoomList(data);
				},
				
				error: function(){
					alert("통신실패");
				}
			})
		}
	</script>
<!-- END :: 채팅방 생성  -->

<!-- START :: 채팅방 입장하기  -->
	<script type="text/javascript">
		function openChatRoom(room_code, thisElem){
			location.href="/chat/chatroom?room_code=" + room_code;
		}
	</script>
<!-- END :: 채팅방 입장하기 -->

<!-- START :: 에디터 리스트 가져오기 -->
	<script type="text/javascript">
		function selectFollowerRoleEditer(){
			$.ajax({
				type: "post",
				url: "/group/selectFollowerRoleEditer",
				data: JSON.stringify({
					channelcode : '${channel.channelcode}'		
				}),
				contentType: "application/json",
				dataType: "json",
				
				success: function(data){
					console.log(">>> EditorList")
					console.log(data)
					$("#editor_list_container").empty();
					fillEditorList(data)
					
					// 채팅방 만들기 명단에 에디터권한의 팔로워들을 전부 넣는다.
					setChatRoomEditorList(data);
				},
				
				error: function(){
					alert("통신실패");
				}
			})
		}
	</script>
<!-- END :: 에디터 리스트 가져오기 -->

<!-- START :: 에디터 리스트 뿌리기 -->
	<script type="text/javascript">
		function fillEditorList(data){
			$("#editor_list_size").text(data.length);

			$.each(data, function(index, item){
				
				var li_item = $("<li>").attr({"class":"editor d-flex align-items-center ml-2"});
								
				li_item.append($("<img>").attr({
									"class" : "rounded-circle m-1 w-40 h-40 bg-white vertical-align-baseline",
									"src" : '/resources/images/profileupload/' + item.image
								}))
								.append($("<span>").attr({"class":"name mx-1"}).text(item.membername))
								.append($("<span>").attr({"class":"id mx-1"}).text(item.memberid))
								.append($("<span>").attr({"class":"email mx-1"}).text(item.memberemail))
								
				$("#editor_list_container").append(li_item);
			})
		}
	</script>
<!-- END :: 에디터 리스트 뿌리기 -->

<!-- START :: 팔로워 리스트 가져오기 -->
	<script type="text/javascript">
		function selectFollowerRoleReader(){
			$.ajax({
				type: "post",
				url: "/group/selectFollowerRoleReader",
				data: JSON.stringify({
					channelcode : '${channel.channelcode}'		
				}),
				contentType: "application/json",
				dataType: "json",
				
				success: function(data){
					console.log(">>> FollowerList")
					console.log(data)
					$("#follower_list_container").empty();
					fillReaderList(data)
				},
				
				error: function(){
					alert("통신실패");
				}
			})
		}
	</script>
<!-- END :: 팔로워 리스트 가져오기 -->

<!-- START :: 팔로워 리스트 뿌리기 -->
	<script type="text/javascript">
		function fillReaderList(data){
			$("#follower_list_size").text(data.length);
			
			$.each(data, function(index, item){
				
				var li_item = $("<li>").attr({"class":"follower d-flex align-items-center ml-2"});
								
				li_item.append($("<img>").attr({
									"class" : "rounded-circle m-1 w-40 h-40 bg-white vertical-align-baseline",
									"src" : '/resources/images/profileupload/' + item.image
								}))
								.append($("<span>").attr({"class":"name mx-1"}).text(item.membername))
								.append($("<span>").attr({"class":"id mx-1"}).text(item.memberid))
								.append($("<span>").attr({"class":"email mx-1"}).text(item.memberemail))
								
				$("#follower_list_container").append(li_item);
			})
		}
	</script>
<!-- END :: 팔로워 리스트 뿌리기 -->
</html>