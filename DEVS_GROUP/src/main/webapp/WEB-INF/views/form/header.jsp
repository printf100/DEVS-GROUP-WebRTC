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
	<link href="/resources/css/master.css" rel="stylesheet" type="text/css">
	
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
<!-- END :: set JSTL variable -->

<!-- START :: JAVASCRIPT -->
	<script type="text/javascript" src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
	<script type="text/javascript" src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
	<script type="text/javascript">
	
	</script>
<!-- END :: JAVASCRIPT -->

</head>
<body>
	<header>
		<nav class="navbar bg-white border">
			<div class="d-flex justify-content-center mx-auto">
				<!-- brand icon -->
				<a class="navbar-brand mr-5" href="/feed/feed"><h3>instagram</h3></a>				
	
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
						<h4 class="mx-2"><a class="nav-link" href="/feed/"><i class="fas fa-home"></i></a></h4>
					</li>
					<li class="nav-item">
						<h4 class="mx-2"><a class="nav-link" href="/dm/"><i class="far fa-paper-plane"></i></a></h4>
					</li>
					<li class="nav-item">
						<h4 class="mx-2"><a class="nav-link" href="#"><i class="far fa-compass"></i></a></h4>
					</li>
					<li class="nav-item">
						<h4 class="mx-2"><a class="nav-link" href="#"><i class="far fa-heart"></i></a></h4>
					</li>
					<li class="nav-item">
						<h4 class="mx-2">
							<a class="nav-link" href="/member/profile">
							<c:choose>
								<c:when test="${not empty loginProfile.memberImgServerName}">
									<div class="rounded-circle border border-dark  w-26 h-26 overflow-hidden">										
										<img 
											id="header_profile_image" 
											class="w-26 h-26 bg-white cursor-pointer vertical-align-baseline"
											src="/resources/images/profileupload/${loginProfile.memberImgServerName }"
										>
									</div>
								</c:when>
								<c:otherwise>
									<i class="far fa-user-circle"></i>
								</c:otherwise>
							</c:choose>
						
							</a>
						</h4>
					</li>
					<li class="nav-item">
						<h4 class="mx-2"><a class="nav-link" href="/ssoclient/logout"><i class="fas fa-sign-out-alt"></i></a></h4>
					</li>
			    </ul>
			</div>
		</nav>

	</header>	
</body>
</html>