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
		#channelimgoriginalname {
			/* 파일 필드 숨기기 */
			display: none;
		}
</style>
<!-- END :: CSS -->

<!-- START :: JAVASCRIPT -->
	<script type="text/javascript">
		$(function() {
			
			$("#channel_image").click(function() {
				// 채널주인만 수정 가능
				if('${channel.membercode}' != '${loginMember.membercode}') {
					return false;
				}
				
				$("input[name='channelimgoriginalname']").click();
			});
			
			$("input[name='channelimgoriginalname']").change(function(e) {
				var form = $("#imageForm")[0];
				var formData = new FormData(form);
				
				$.ajax({
					type: "POST",
					enctype: "multipart/form-data",
					url: "/group/updategroupchannelimage",
					processData: false,
					contentType: false,
					data: formData,
					dataType: "JSON",
					success: function(msg) {
						$("#channel_image").attr("src","/resources/images/groupchannelprofileupload/" + msg.img);
						/* notifyProfileImgChange(msg.img); */
					},
					error : function() {
						alert("통신 실패");
					}
				});				
			});
			
		});
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
			<div class="container-fluid">
				<div class="page-content" id="channel_description">
				
					<span class="login100-form-title p-b-49">
						<img src="/resources/images/logo.png" width="3%;"></img> ${channel.channelname }
					</span>
				
					<div class="row">
						<!-- START :: 채널 이미지 -->
						<form id="imageForm" class="mx-auto m-b-23" action="/group/updatechannelimage" method="POST" enctype="multipart/form-data" style="text-align: center;">
							<img id="channel_image" src="
					                                       <c:choose>
					                                          <c:when test="${not empty channel.channelimgservername}">
					                                          	/resources/images/groupchannelprofileupload/${channel.channelimgservername }
					                                          </c:when>
					                                          <c:otherwise>
					                                          	/resources/images/groupchannelprofileupload/default.png
					                                          </c:otherwise>
					                                       </c:choose>   
					                                       "style="width: 150px; cursor: pointer;">
							<input type="file" id="channelimgoriginalname" name="channelimgoriginalname" value="${channel.channelimgoriginalname }">					
						</form>
						<!-- END :: 채널 이미지 -->
						
						<!-- START :: 기타 채널 정보 -->
						<c:choose>
							<c:when test="${channel.membercode eq loginMember.membercode}">
								<form action="/group/updategroupchanneldescription" method="POST" class="login100-form validate-form">
									<input type="hidden" value="${channel.channelcode}">
									
									<div class="wrap-input100 validate-input m-b-23 mx-auto" data-validate = "Username is reauired">
										<span class="label-input100">채널이름 </span>
										<input class="input100" type="text" name="channelname" value="${channel.channelname }">
										<span class="focus-input100" data-symbol="&#xf206;"></span>
									</div>
									<div class="wrap-input100 validate-input m-b-23 mx-auto" data-validate="Password is required">
										<span class="label-input100">웹사이트</span>
										<input class="input100" type="text" name="channelwebsite" value="${channel.channelwebsite }">
										<span class="focus-input100" data-symbol="&#xf206;"></span>
									</div>
									<div class="wrap-input100 validate-input m-b-23 mx-auto" data-validate = "Username is reauired">
										<span class="label-input100">채널소개 </span>
										<input class="input100" type="text" name="channelintroduce" value="${channel.channelintroduce }">
										<span class="focus-input100" data-symbol="&#xf206;"></span>
									</div>
									
									<div class="container-login100-form-btn">
										<div class="wrap-login100-form-btn">
											<div class="login100-form-bgbtn"></div>
											<button class="login100-form-btn" type="submit" value="수정">
												수정
											</button>
										</div>
									</div>
								</form>
							</c:when>
							<c:otherwise>
							<div class="login100-form validate-form">
								<div class="wrap-input100 validate-input m-b-23 mx-auto" data-validate = "Username is reauired">
									<span class="label-input100">채널이름 </span>
									<input class="input100" disabled="disabled" name="channelname" value="${channel.channelname }">
									<span class="focus-input100" data-symbol="&#xf206;"></span>
								</div>
								<div class="wrap-input100 validate-input m-b-23 mx-auto" data-validate="Password is required">
									<span class="label-input100">웹사이트</span>
									<input class="input100" disabled="disabled" name="channelname" value="${channel.channelwebsite }">
									<span class="focus-input100" data-symbol="&#xf206;"></span>
								</div>
								<div class="wrap-input100 validate-input m-b-23 mx-auto" data-validate = "Username is reauired">
									<span class="label-input100">채널소개 </span>
									<input class="input100" disabled="disabled" name="channelname" value="${channel.channelintroduce }">
									<span class="focus-input100" data-symbol="&#xf206;"></span>
								</div>
							</div>
							</c:otherwise>
						</c:choose>
						<!-- END :: 기타 채널 정보 -->
						
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
</html>