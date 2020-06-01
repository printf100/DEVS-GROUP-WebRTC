function connectAlarmSocket() {
	//////////////////////// 웹소켓 객체 생성  ////////////////////////
	var loc = window.location;
	var uri = CLIENT_SOCKET_PROTOCOL + "://" + loc.hostname + "/alarm";
	console.log("uri : " + uri);
   
	alarm = new WebSocket(uri);
   
	if(alarm !== undefined && alarm.readyState !== WebSocket.CLOSED) {
		return;
	}
	/////////////////////////////////////////////////////////////

	// 처리 테스크
	// 1. 유저 채널접속 알람 - sidebar.jsp
	// 2. chatting room 생성 알람
	// 3. webrtc room 생성 알람
	// 4. reader -> editor 변경 알람 = 유저 채널접속알람으로 퉁침... 개굳!
	
	/////////////////////////////////////////////////////////////
	// 웹소켓 열림
	alarm.onopen = function(event) {       
       
		console.log("chat_socket : ", event.data);
   
		if(event.data === undefined) {
			return;
		} else {                  
			console.log("소켓 연결");
		}               
       
	};
    /////////////////////////////////////////////////////////////
	// 메시지 보내기
	
	// ... sidebar.jsp 에서 이벤트 발생시 sendAlarmMessage() 호출...   
	
	/////////////////////////////////////////////////////////////
	// 메시지 받은 후 처리 - sidebar.jsp 에서 처리
	
	/////////////////////////////////////////////////////////////
	
	// 소켓 통신 종료 시
	alarm.onclose = function(event) {
		console.log("chatting socket closed");
	};
	/////////////////////////////////////////////////////////////
}

function sendAlarmMessage(payload) {
	alarm.send(JSON.stringify(payload));
}