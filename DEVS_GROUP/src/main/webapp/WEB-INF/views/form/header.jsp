<%@ page import="org.apache.catalina.SessionListener"%>
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
<title>Insert title here</title>

<!-- bootstrap -->
   <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">
   <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
   <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
   <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"></script>
   <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.3.1/css/all.css" integrity="sha384-mzrmE5qonljUremFsqc01SB46JvROS7bZs3IO2EmfFsd15uHvIt+Y8vEf7N7fWAU" crossorigin="anonymous">
<!-- end bootstrap --!>

<!-- START :: css -->
	<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<!-- 	<link href="/resources/css/master.css" rel="stylesheet" type="text/css"> -->
	
	<style type="text/css">
	
	</style>
<!-- END :: css -->

<!-- START :: set JSTL variable -->
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
	
	<c:set var="loginMember" value="${sessionScope.user}"></c:set>
	<c:set var="loginProfile" value="${sessionScope.profile}"></c:set>
	
	<c:set var="SERVER_PORT" value="${sessionScope.SERVER_PORT}"></c:set>
	<c:set var="CLIENT_DOMAIN" value="${sessionScope.CLIENT_DOMAIN}"></c:set>
	<c:set var="CLIENT_SOCKET_PROTOCOL" value="${sessionScope.CLIENT_SOCKET_PROTOCOL}"></c:set>
	<c:set var="CLIENT_PROTOCOL" value="${sessionScope.CLIENT_PROTOCOL}"></c:set>
	
	<c:set var="channel" value="${sessionScope.channel}"></c:set> <!-- 현재 채널 (채널주인 || 팔로우한사람만 접근가능)-->
	<c:set var="follow" value="${sessionScope.follow}"></c:set> <!-- 현재 채널의 follow 상태(null || '' 이라면 방장) -->
<!-- END :: set JSTL variable -->

<!-- START :: JAVASCRIPT -->
	<script type="text/javascript" src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
	<script type="text/javascript" src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
	   
	<script src='/resources/js/adapter-latest.js'></script>
	<script src='/resources/js/chat_socket.js'></script>
	
	<script type="text/javascript">
		var SERVER_PORT = "${SERVER_PORT}";
		var CLIENT_DOMAIN = "${CLIENT_DOMAIN}";
		var CLIENT_SOCKET_PROTOCOL = "${CLIENT_SOCKET_PROTOCOL}";
		var CLIENT_PROTOCOL = "${CLIENT_PROTOCOL}";

		// rtc 소켓 연결
		// var sock;
		// connect("${loginMember.memberid}");
		
		// chatting 소켓 연결
		var ws;
		connectChatSocket();
	</script>
<!-- END :: JAVASCRIPT -->

</head>
<body>
	<header>
		<nav class="navbar bg-white border">
			<div class="d-flex justify-content-center mx-auto">
				<!-- brand icon -->
				<a class="navbar-brand mr-5" href="/group/"><h3>instagram</h3></a>				
	
				<!-- 검색창 -->
				<form id="headerSearch" class="form-inline mx-5" action="/member/headerSearch" method="post">
					<div class="input-group">
						<div class="input-group-prepend">	
							<span id="headerSearchSubmitButton" class="input-group-text bg-light"><i class="fas fa-search" onclick=";"></i></span>						
						</div>
						
						<input class="form-control bg-light" type="text" name="search" value="${search}" placeholder="검색">
						
						<div class="input-group-append">	
							<span id="headerSearchSubmitButton" class="input-group-text bg-light"><i class="fas fa-times-circle" onclick=";"></i></span>						
						</div>
					</div>
				</form>				
				
				<!-- nav list -->						
				<ul class="navbar-nav list-group-horizontal ml-5">
					<li class="nav-item">
						<h4 class="mx-2"><a class="nav-link" href="/ssoclient/logout"><i class="fas fa-sign-out-alt"></i></a></h4>
					</li>
			    </ul>
			</div>
		</nav>

	</header>
	
	<!-- START :: 채팅방 만들기 modal -->
	<div class="modal" id="makeNewChatRoom">
		<div class="modal-dialog">
			<div class="modal-content">
		
		    <!-- Modal Header -->
		    <div class="modal-header">
		    	<h4 class="modal-title">채팅방 만들기</h4>
		    	<button type="button" class="close" data-dismiss="modal">&times;</button>
		    </div>
		
		    <!-- Modal body -->
		    <div class="modal-body">
		    	<input type="text" id="chatRoomName">
		    </div>
		
		    <!-- Modal footer -->
		    <div class="modal-footer">
		    	<button type="button" onclick="makeChatRoom();">만들기</button>
		    </div>
		
		 	</div>
		</div>
	</div>
	<!-- END :: 채팅방 만들기 modal -->
	
	<!-- START :: 화상채팅방 만들기 modal -->
	<div class="modal" id="makeNewRtcRoom">
		<div class="modal-dialog">
			<div class="modal-content">
		
		    <!-- Modal Header -->
		    <div class="modal-header">
		    	<h4 class="modal-title">화상채팅방 만들기</h4>
		    	<button type="button" class="close" data-dismiss="modal">&times;</button>
		    </div>
		
		    <!-- Modal body -->
		    <div class="modal-body">
		    	<input type="text" id="rtcRoomName">
		    </div>
		
		    <!-- Modal footer -->
		    <div class="modal-footer">
		    	<button type="button" onclick="makeRtcRoom();">만들기</button>
		    </div>
		
		 	</div>
		</div>
	</div>
	<!-- END :: 화상채팅방 만들기 modal -->
</body>
</html>