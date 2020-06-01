<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!-- START :: HEADER IMPORT -->
<%@ include file="form/header.jsp"%>
<!-- END :: HEADER IMPORT -->

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Insert title here</title>

<!-- START :: CSS -->
<link rel="icon" type="image/png" href="/resources/images/icons/favicon.ico"/>
<link rel="stylesheet" type="text/css" href="/resources/vendor/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="/resources/fonts/font-awesome-4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" type="text/css" href="/resources/fonts/iconic/css/material-design-iconic-font.min.css">
<link rel="stylesheet" type="text/css" href="/resources/vendor/animate/animate.css">
<link rel="stylesheet" type="text/css" href="/resources/vendor/css-hamburgers/hamburgers.min.css">
<link rel="stylesheet" type="text/css" href="/resources/vendor/animsition/css/animsition.min.css">
<link rel="stylesheet" type="text/css" href="/resources/vendor/select2/select2.min.css">
<link rel="stylesheet" type="text/css" href="/resources/vendor/daterangepicker/daterangepicker.css">
<link rel="stylesheet" type="text/css" href="/resources/css/util.css">
<link rel="stylesheet" type="text/css" href="/resources/css/main.css">
<style type="text/css">
</style>
<!-- END :: CSS -->

<!-- START :: JAVASCRIPT -->
<script type="text/javascript">
</script>
<!-- END :: JAVASCRIPT -->

</head>
<body>

	<div class="page-wrapper chiller-theme toggled">
	
		<!-- START :: SIDEBAR FORM -->
		<%@ include file="form/sidebar.jsp"%>
		<!-- END :: SIDEBAR FORM -->
		
		<!-- START :: page - content -->
		<main class="page-content">
		
			<!-- START :: 그룹채널 만들기 폼 -->
			<div class="container-fluid">
				<div class="page-content" id="channel_description">
				
					<span class="login100-form-title p-b-49">
						<img src="/resources/images/logo.png" width="10%;"></img>
						<br><br>
						Group Channel 만들기
					</span>
					
					<div class="row">
						<form action="/group/createGroupChannel" method="post" class="login100-form validate-form">
							<input type="hidden" name="membercode" value="${loginMember.membercode }">
							<input type="hidden" name="channeltype" value="G">
							
							<div class="wrap-input100 validate-input m-b-23 mx-auto" data-validate = "Username is reauired">
								<span class="label-input100" style="font-size: 1.2em;">채널이름 </span>
								<input class="input100" type="text" name="channelname" required="required" placeholder="만들고 싶은 채널 이름을 입력해주세요">
								<span class="focus-input100" data-symbol="&#xf206;"></span>
							</div>
							
							<div class="container-login100-form-btn">
								<div class="wrap-login100-form-btn">
									<div class="login100-form-bgbtn"></div>
									<button class="login100-form-btn" type="submit" value="생성">
										생성
									</button>
								</div>
							</div>
						</form>
						<!-- END :: 그룹채널 만들기 폼 -->
						
					</div>
					
				</div>
	    	</div>
	    </main>
		<!-- END :: page - content -->
		
	</div>
	
	<script src="/resources/vendor/jquery/jquery-3.2.1.min.js"></script>
	<script src="/resources/vendor/animsition/js/animsition.min.js"></script>
	<script src="/resources/vendor/bootstrap/js/popper.js"></script>
	<script src="/resources/vendor/bootstrap/js/bootstrap.min.js"></script>
	<script src="/resources/vendor/select2/select2.min.js"></script>
	<script src="/resources/vendor/daterangepicker/moment.min.js"></script>
	<script src="/resources/vendor/daterangepicker/daterangepicker.js"></script>
	<script src="/resources/vendor/countdowntime/countdowntime.js"></script>
	<script src="/resources/js/main.js"></script>
	
</body>

<!-- START :: 채널 입장하기 -->
	<script type="text/javascript">
		function openChannel(channelcode){
			location.href = "/group/channel?channelcode=" + channelcode; 	
		}
	</script>
<!-- END :: 채널 입장하기 -->

</html>