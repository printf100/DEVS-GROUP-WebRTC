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
<!-- <link href="resources/css/master.css" rel="stylesheet" type="text/css"> -->
<style type="text/css">
</style>
<!-- END :: CSS -->

<!-- START :: JAVASCRIPT -->
<script type="text/javascript">
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
			
			<!-- START :: 그룹채널 만들기 폼 -->
			<div>
				<h1>Group Channel 만들기</h1>
				<form action="/group/createGroupChannel" method="post">
					<input type="hidden" name="membercode" value="${loginMember.membercode }">
					<input type="hidden" name="channeltype" value="G">
					
					채널 이름 : <input type="text" name="channelname" required="required">
					
					<input type="submit" value="생성">
				</form>
			
			</div>
			<!-- END :: 그룹채널 만들기 폼 -->
			
			<!-- START :: 나의 그룹채널, 내가 팔로우한 그룹채널 리스트 -->
			<div>
				<h1>나의 Group Channel</h1>			
    			<div id="my_group_channel_c">
    			</div>
    			
    			<h1>팔로우한 Group Channel</h1>
    			<div id="follow_group_channel_c">
    			</div>
			</div>
			<!-- END :: 나의 그룹채널, 내가 팔로우한 그룹채널 리스트 -->
	    	
	    	

	    	
	    	
		</div>
		<!-- END :: page - content -->
		
	</div>
	
</body>

<!-- START :: 내가만든, 내가 팔로우한 채널 리스트 가져오기 -->
	<script type="text/javascript">
	
		$(function() {
			selectMyGroupChannelList_main();
			selectFollowGroupChannelList_main();
		});
		
		/////////////////////////////////////////////////////////////////////////////

		function selectMyGroupChannelList_main() {
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
					fillMyGroupChannelList_main(data);
				},
				
				error: function(){
					alert("통신실패");
				}
			});
		}
		
		function fillMyGroupChannelList_main(data){
			$.each(data, function(index, item){
				
				var div = $("<div>").attr({"class":"my_channel", "data-channelcode":item.channelcode, "onclick":"openChannel(" + item.channelcode + ");"});
				var channel_name = $("<div>").attr({"class":"channel_name"});
				var channel_img = $("<div>").attr({"class":"channel_img"});
				
				channel_name.append($("<div>").text(item.channelname));
				channel_img.append($("<img>").attr({"src":"/resources/images/groupchannelprofileupload/" + item.channelimgservername}))

				div.append(channel_img).append(channel_name);
					
				$("#my_group_channel_c").append(div);
			})
		}
		
		/////////////////////////////////////////////////////////////////////////////
		
		function selectFollowGroupChannelList_main(){
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
					fillFollowGroupChannelList_main(data)
				},
				
				error: function(){
					alert("통신실패");
				}
			})
		}
		
		function fillFollowGroupChannelList_main(data) {
			$.each(data, function(index, item){
				
				var div = $("<div>").attr({"class":"follow_channel", "data-channelcode":item.channelcode, "onclick":"openChannel(" + item.channelcode + ");"});
				var channel_name = $("<div>").attr({"class":"channel_name"});
				var channel_img = $("<div>").attr({"class":"channel_img"});
				
				channel_name.append($("<div>").text(item.channelname));
				channel_img.append($("<img>").attr({"src":"/resources/images/groupchannelprofileupload/" + item.channelimgservername}))

				div.append(channel_img).append(channel_name);
					
				$("#follow_group_channel_c").append(div);
			})
		}
	</script>
<!-- END :: 내가 팔로우한 채널 리스트 가져오기 -->

<!-- START :: 채널 입장하기 -->
	<script type="text/javascript">
		function openChannel(channelcode){
			location.href = "/group/channel?channelcode=" + channelcode; 	
		}
	</script>
<!-- END :: 채널 입장하기 -->

</html>