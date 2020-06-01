function connectChatSocket() {
    // 웹소켓 객체 생성
	var loc = window.location
	var uri = CLIENT_SOCKET_PROTOCOL + "://" + loc.hostname + "/chatsocket"
	console.log("uri : " + uri);
	
	ws = new WebSocket(uri);
	
	if(ws !== undefined && ws.readyState !== WebSocket.CLOSED) {
        return;
    }

    // 웹소켓 열림
    ws.onopen=function(event) {
    	
    	console.log("chat_socket onopen : ", event.data);
    	
        if(event.data === undefined) {
        	return;
        } else {
            console.log("소켓 연결");
        }
    };
    
    // 소켓 통신 종료 시
    ws.onclose=function(event) {
        console.log("chatting socket closed");
    }
}

function sendPayloadMessage(payload) {
	ws.send(JSON.stringify(payload));
}