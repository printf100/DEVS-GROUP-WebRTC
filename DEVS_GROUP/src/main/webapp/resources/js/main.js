// getUserMedia 타입 체크
navigator.getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.msGetUserMedia;

// RTCPeerConnection 타입 체크
window.RTCPeerConnection = window.RTCPeerConnection || window.mozRTCPeerConnection || window.webkitRTCPeerConnection;
window.RTCIceCandidate = window.RTCIceCandidate || window.mozRTCIceCandidate || window.webkitRTCIceCandidate;
window.RTCSessionDescription = window.RTCSessionDescription || window.mozRTCSessionDescription || window.webkitRTCSessionDescription;

//stun / turn 서버 리스트 셋팅
var configuration = {
   'iceServers' : [
		{
			'urls' : [ 
						'stun:stun.l.google.com:19302',
						'stun:stun2.l.google.com:19302',
						'stun:stun3.l.google.com:19302',
						'stun:stun4.l.google.com:19302' 
					]
		},

		{
			url : 'turn:numb.viagenie.ca',
			credential : 'muazkh',
			username : 'webrtc@live.com'
		}, 
		{
			url : 'turn:192.158.29.39:3478?transport=udp',
			credential : 'JZEOEt2V3Qb0y27GRntt2u2PAYA=',
			username : '28224511:1379330808'
		}, 
		{
			url : 'turn:192.158.29.39:3478?transport=tcp',
			credential : 'JZEOEt2V3Qb0y27GRntt2u2PAYA=',
			username : '28224511:1379330808'
		} 
	]
};

var peer;
var memberId; // 내 멤버 아이디

var connections = [];

var room_code; // 화상채팅 방번호

var isFirstEnter = 0;


// 소켓 연결 @param (memberid)
function connect(memberid) {
	console.log('연결됨');

	var loc = window.location
	var uri = CLIENT_SOCKET_PROTOCOL + "://" + loc.hostname + "/signal"
	console.log("uri : " + uri);

	sock = new WebSocket(uri);

	sock.onopen = function(e) {
		console.log('open', e);
		
		memberId = memberid; // 내 멤버아이디 전역변수에 셋팅
		
		// enter메시지는 나를 포함한 모두에게 전송되고, 이때 이 방에 들어와있는 client list가 각각의 client들에게 셋팅된다.
		sendMessage({
			type : "enter",
			data : memberId
		});
	}

	sock.onmessage = function(e) {
		console.log('message', e.data);
		var message = JSON.parse(e.data);

		if (message.type == 'enter') {
			// enter 메시지를 받은 모든 client들은 startRTC( 1. offer(peer), 2. ice/addStream )

			var clientList = message.loginIds; // enter 메세지를 받은 모든 사람은 최신화된 connections 배열을 가질 수 있다.
			console.log("clientList 배열 : " + clientList);

			if (message.fromId == memberId) { // 내가 들어왔어
				
				console.log("내가 들어왔음. peerConnection 시작!!");
				
				isFirstEnter = clientList.length-1;
				clientList.forEach(function(client) {
					
					if(memberId != client) {
						connections[client] = new RTCPeerConnection(configuration);
						startPeerConnection(connections[client], client);
					}
					
				});
			
				
			} else {		// 이미 접속한 사람들이 새 접속자에게 offer 전송
			
				console.log(message.fromId + " 가 들어왔음. 내가 offer 날려");	
				
				connections[message.fromId] = new RTCPeerConnection(configuration);
				startPeerConnection(connections[message.fromId], message.fromId);
				
				offer(message.fromId);

			}
		}

		else if (message.type === 'signal') {
			/*
			 * SDP
			 */
			if (message.data.sdp) {
				console.log("================= SDP ================");

				// 상대 peer로부터 전달받은 SDP 셋팅
				connections[message.fromId].setRemoteDescription(new RTCSessionDescription(message.data.sdp), function() {
					console.log("## setRemoteDescription", message.data.sdp);

					if (connections[message.fromId].remoteDescription.type == 'offer') {
						peer = message.fromId;
						console.log(peer + "로부터 offer SDP 받았어");

						// answer SDP를 생성해 상대 peer에게 전달
						console.log("## createAnswer : 그래서 " + peer + "한테 answer SDP 줄거야~~~");
						connections[message.fromId].createAnswer(localDescCreated, logError);
						
					} else { // 'answer' 라면
						console.log(message.fromId + "로부터 answer SDP 도착");
						
					}
				}, // 성공적으로 수행되면, 각 브라우저에서 서로에 대해 인지하고 있는 상태가 된것, p2p 연결이 성공적으로 완료된 것!
				logError);
			}

			/*
			 * ICE Candidate
			 */
			else {
		    	console.log("## addIceCandidate : " + message.fromId + " 한테 candidate 받아서 셋팅 !!!!!!!!!!!!", message.data.candidate);
				
		    	// 도착한 상대 peer의 네트워크 정보 등록
		    	connections[message.fromId].addIceCandidate(new RTCIceCandidate(message.data.candidate));
			}

		}
		
		else if (message.type == "disconnect") {
			console.log(message.fromId, " 연결 종료함");
			
			var leftVideo = document.querySelector('[data-socket="'+ message.fromId +'"]');
            leftVideo.remove();
		}

	}// END onmessage
	
	sock.onclose = function(e) {
		console.log('close', e);
	}

	sock.onerror = function(e) {
		console.log('error', e);
	}

}

// ICE Candidate (Network 정보) 교환하기
function startPeerConnection(connection, toId) {
	console.log(memberId + "가 " + toId + " 와 peer Connection 시작 !!!!!!!!!!!!!!!!!");

	// 자신의 미디어를 셋팅 (signaling을 통해 connection이 이루어지기 전에 미리 자신의 video/audio 스트림 취득)
	navigator.getUserMedia({
		'audio' : true,
		'video' : true
	}, function(stream) { // 성공 시
		console.log("## getUserMedia 내 화면 입력 ", stream);
		myVideo.srcObject = stream;
		connection.addStream(stream); // 미디어 스트림 입력
		
		// 처음 들어온 사람이 offer를 시작할수 있는 첫 시점
		if(isFirstEnter > 0){
			console.log("나는 처음 들어온 사람이야 내 화면 셋팅됐으니까 모두에게 offer 보낼거야~~");
			offer(toId);
			isFirstEnter--;
		}
		
	}, logError);
	
	// onicecandidate : 내 네트워크 정보가 확보되면 실행될 callback 셋팅
	connection.onicecandidate = function(evt) {

		// signaling server를 통해 상대 peer에게 전송
		if (evt.candidate) {
			console.log("## onicecandidate", evt.candidate);
			sendMessage({
				type : "signal",
				toId : toId,
				data : { 'candidate' : evt.candidate }
			});
		}
	};
	
	// 원격 스트림이 도착하면 remoteView에 화면 뿌려주기
	connection.onaddstream = function(evt) {
		console.log("#### onaddstream 상대방 화면 셋팅", evt);
		
		const newVideo = document.createElement('video');
		newVideo.setAttribute('data-socket', toId);
		newVideo.srcObject = evt.stream;
		newVideo.autoplay = true;
		newVideo.muted = false;
		newVideo.playsinline = true;
		
		$("#videos").append(newVideo);
	};
}

// 송신자는 미디어 정보 입력 후, 상대  peer에게 전달할 SDP 생성
// SDP(Session Description Protocol) : 내 브라우저에서 사용가능한 코덱이나 해상도에 대한 정보 (offer / answer)
function offer(dest) {
	peer = dest;
	
	console.log("## createOffer : " + peer + "한테 offer SDP 보낼거야~~~~");
	connections[peer].createOffer(
			localDescCreated,
			logError
	);
}

function localDescCreated(desc) {

	console.log("## localDescCreated 진입 ");

	// 생성된 SDP를 로컬 SDP로 설정
	connections[peer].setLocalDescription(desc, function() {
		console.log("## setLocalDescription : 내 SDP를 로컬에 셋팅하고 " + peer + "한테 보낼거야~~", desc);
		
		// 상대 peer에게 SDP 전송
		sendMessage({
			type : "signal",
			toId : peer,
			data : {	'sdp' : connections[peer].localDescription }
		});
	}, logError);

};

function sendMessage(payload) {
	sock.send(JSON.stringify(payload));
}

function disconnect() {
	console.log('연결 종료');

	if (sock != null) {
		sock.close();
	}

	setConnected(false);
}

function logError(error) {
	console.log(error.name + ' : ' + error.message);
}