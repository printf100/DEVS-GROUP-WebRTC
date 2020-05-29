<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Insert title here</title>

<!-- START :: HEADER FORM -->
<%@ include file="form/header.jsp"%>
<!-- END :: HEADER FORM -->

<!-- START :: CSS -->
<!-- <link href="resources/css/master.css" rel="stylesheet" type="text/css"> -->
<style type="text/css">
</style>
<!-- END :: CSS -->

<!-- START :: JAVASCRIPT -->
	<script type="text/javascript">
		$(function(){
			
			$("#channel_image").click(function(){
				// 채널주인만 수정 가능
				if('${channel.membercode}' != '${loginMember.membercode}'){
					return false;
				}
				
				$("input[name='channelimgoriginalname']").click();
			})
			
			$("input[name='channelimgoriginalname']").change(function(e){
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
					success: function(msg){
						$("#channel_image").attr("src","/resources/images/groupchannelprofileupload/" + msg.img);
						/* notifyProfileImgChange(msg.img); */
					},
					error : function() {
						alert("통신 실패");
					}
				})				
			})
			
		})
	</script>
<!-- END :: JAVASCRIPT -->

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
			<div id="channel_description">
				<h1>채널이름 : ${channel.channelname }</h1>
				
				<!-- START :: 채널 이미지 -->
				<form id="imageForm" action="/group/updatechannelimage" method="POST" enctype="multipart/form-data">
					<img id="channel_image" src="
			                                       <c:choose>
			                                          <c:when test="${not empty channel.channelimgservername}">
			                                          	/resources/images/groupchannelprofileupload/${channel.channelimgservername }
			                                          </c:when>
			                                          <c:otherwise>
			                                          	/resources/images/groupchannelprofileupload/default.png
			                                          </c:otherwise>
			                                       </c:choose>   
			                                       ">
					<input type="file" name="channelimgoriginalname" value="${channel.channelimgoriginalname }">					
				</form>
				<!-- END :: 채널 이미지 -->
				
				<!-- START :: 기타 채널 정보 -->
				<c:choose>
					<c:when test="${channel.membercode eq loginMember.membercode}">
						<form action="/group/updategroupchanneldescription" method="POST">
							<input type="hidden" value="${channel.channelcode}">
							<br>채널이름 : <input type="text" name="channelname" value="${channel.channelname }">
							<br>웹사이트 : <input type="text" name="channelwebsite" value="${channel.channelwebsite }">
							<br>채널소개 : <input type="text" name="channelintroduce" value="${channel.channelintroduce }">
							<input type="submit" value="수정">
						</form>
					</c:when>
					<c:otherwise>
						<br>채널이름 : ${channel.channelname}
						<br>웹사이트 : ${channel.channelwebsite}
						<br>채널소개 : ${channel.channelintroduce}
					</c:otherwise>
				</c:choose>
				<!-- END :: 기타 채널 정보 -->
				
				<!-- START :: 채널 글 -->
				<div>
					<h1>board</h1>
				</div>
				<!-- END :: 채널 글 -->
			</div>
			
		</div>
		<!-- END :: page - content -->
		
	</div>
	
</body>
</html>