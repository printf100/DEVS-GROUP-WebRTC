navigator.getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia
		|| navigator.mozGetUserMedia || navigator.msGetUserMedia;

var loggedIn = false;

var configuration = {
	'iceServers' : [
			{
				'urls' : [ 'stun:stun.l.google.com:19302',
						'stun:stun2.l.google.com:19302',
						'stun:stun3.l.google.com:19302',
						'stun:stun4.l.google.com:19302' ]
			},

			// {url:'stun:stun01.sipphone.com'},
			// {url:'stun:stun.ekiga.net'},
			// {url:'stun:stun.fwdnet.net'},
			// {url:'stun:stun.ideasip.com'},
			// {url:'stun:stun.iptel.org'},
			// {url:'stun:stun.rixtelecom.se'},
			// {url:'stun:stun.schlund.de'},
			// {url:'stun:stunserver.org'},
			// {url:'stun:stun.softjoys.com'},
			// {url:'stun:stun.voiparound.com'},
			// {url:'stun:stun.voipbuster.com'},
			// {url:'stun:stun.voipstunt.com'},
			// {url:'stun:stun.voxgratia.org'},
			// {url:'stun:stun.xten.com'},
			// {url:'stun:s1.voipstation.jp'},
			// {url:'stun:s2.voipstation.jp'},
			// {url:'stun:stun.services.mozilla.com'},
			// {url:'stun:s2.voipstation.jp'},

			{
				url : 'turn:numb.viagenie.ca',
				credential : 'muazkh',
				username : 'webrtc@live.com'
			}, {
				url : 'turn:192.158.29.39:3478?transport=udp',
				credential : 'JZEOEt2V3Qb0y27GRntt2u2PAYA=',
				username : '28224511:1379330808'
			}, {
				url : 'turn:192.158.29.39:3478?transport=tcp',
				credential : 'JZEOEt2V3Qb0y27GRntt2u2PAYA=',
				username : '28224511:1379330808'
			} ]
};

var pc;
var peer;

var head; // 방장
var connections = [];
var room_code; // 화상채팅 방번호

// RTCPeerConnection 타입 체크
var RTCPeerConnection;
if (typeof mozRTCPeerConnection !== 'undefined') {
	RTCPeerConnection = mozRTCPeerConnection;
	console.log("mozilla RTCPeerConnection");
} else if (typeof webkitRTCPeerConnection !== 'undefined') {
	RTCPeerConnection = webkitRTCPeerConnection;
	console.log("chrome RTCPeerConnection");
} else if (typeof window.RTCPeerConnection !== 'undefined') {
	RTCPeerConnection = window.RTCPeerConnection;
	console.log("window.RTCPeerConnection");
} else {
	console
			.error('WebRTC 1.0 (RTCPeerConnection) API seems NOT available in this browser.');
}

function logError(error) {
	console.log(error.name + ' : ' + error.message);
}

// 소켓 연결
function connect(username) {
	console.log('연결됨');

	var loc = window.location
	var uri = CLIENT_SOCKET_PROTOCOL + "://" + loc.hostname + "/signal"
	console.log("uri : " + uri);

	sock = new WebSocket(uri);

	sock.onopen = function(e) {
		console.log('open', e);

		sock.send(JSON.stringify({
			type : "login",
			data : username
		}));
		// should check better here, it could have failed
		// moreover not logout implemented
		loggedIn = true;
	}

	sock.onclose = function(e) {
		console.log('close', e);
	}

	sock.onerror = function(e) {
		console.log('error', e);
	}

	sock.onmessage = function(e) {
		console.log('message', e.data);
		var message = JSON.parse(e.data);

		if (message.type != 'enter') {
			if (!pc) {
				console.log("## NOT pc startRTC()");

				if (confirm(message.dest + "로부터 화상채팅 초대가 왔습니다. 수락하시겠습니까?")) {
					startRTC();
				} else {
					return false;
				}
			}

		}

		if (message.type === 'rtc') {
			/*
			 * SDP
			 */
			if (message.data.sdp) {
				console.log("##onmessage - SDP : " + message.data.sdp);

				// 상대 peer로부터 전달받은 SDP 셋팅
				pc.setRemoteDescription(new RTCSessionDescription(
						message.data.sdp), function() {
					console.log("##SDP - setRemoteDescription");

					if (pc.remoteDescription.type == 'offer') {
						console.log(message.dest + "로부터 offer SDP 도착");

						peer = message.dest;
						// answer SDP를 생성해 상대 peer에게 전달
						console.log("##createAnswer");
						pc.createAnswer(localDescCreated, logError);

					} else { // 'answer' 라면
						console.log(message.dest + "로부터 answer SDP 도착");
					}
				}, // 성공적으로 수행되면, 각 브라우저에서 서로에 대해 인지하고 있는 상태가 된것, p2p
				// 연결이 성공적으로 완료된 것!
				logError);
			}

			/*
			 * ICE Candidate
			 */
			else {
				console.log("##onmessage - addIceCandidate : " + message.data.candidate);
				// 도착한 상대 peer의 네트워크 정보 등록
				pc.addIceCandidate(new RTCIceCandidate(message.data.candidate));

				if (HEAD == "" || HEAD != MEMBER_ID) {
					head = message.dest;
					console.log("방장 " + head + " 셋팅 완료");
				}
			}

		} else if (message.type === 'enter') {
			console.log("####" + message.data);
			connections.push(message.data);
			console.log("####connections : " + connections);
		}

	}// END onmessage

	// setConnected(true);
}

// 1. ICE Candidate (Network 정보) 교환하기
function startRTC() {
	console.log("##startRTC()");

	sendMessage({
		type : "enter",
		data : MEMBER_ID
	});

	pc = new RTCPeerConnection(configuration);

	// onicecandidate : 내 네트워크 정보가 확보되면 실행될 callback 셋팅
	pc.onicecandidate = function(evt) {

		// signaling server를 통해 상대 peer에게 전송
		if (evt.candidate) {
			console.log("##onicecandidate : " + evt.candidate);
			sendMessage({
				type : "rtc",
				dest : peer,
				data : {
					'candidate' : evt.candidate
				}
			});
		}

	};

	// signaling을 통해 connection이 이루어지기 전에 미리 자신의 video/audio 스트림 취득
	navigator.getUserMedia({
		'audio' : true,
		'video' : true
	}, function(stream) { // 성공 시
		console.log("##getUserMedia 내 화면 입력 " + stream);
		myVideo.srcObject = stream;
		pc.addStream(stream); // 미디어 스트림 입력
		// addMyView(stream);

		// 서로의 네트워크 정보 등록이 완료되면 초대받은 사람이 createOffer 실행
		if (HEAD == "" || HEAD != MEMBER_ID) {
			console.log("----- 초대받은 사람이 createOffer");
			offer(head);
		}

	}, logError);

	// 원격 스트림이 도착하면 remoteView에 화면 뿌려주기
	pc.onaddstream = function(evt) {
		console.log("######onaddstream : " + evt);
		// remoteView.srcObject = evt.stream;

		if (HEAD != "" && HEAD == MEMBER_ID) {
			console.log("----- 방장한테 초대 수락한 사람 화면 추가");
			addNewPeer(evt.stream);
		} else {
			console.log("-------- 초대받은 사람한테 방장 화면 추가");
			addNewPeer(evt.stream);
		}
	};

	// ///////////////////////// 방 만들기 /////////////////////////
	// if(HEAD != "" && HEAD == MEMBER_ID) {
	// console.log("----- 방장이 방 만듦");
	// room_code += 1;
	// sendMessage({
	// type: "room",
	// room_code: room_code
	// });
	// }

}

// 송신자는 미디어 정보 입력 후, 상대 peer에게 전달할 SDP 생성
// SDP(Session Description Protocol) : 내 브라우저에서 사용가능한 코덱이나 해상도에 대한 정보 (offer /
// answer)
function offer(dest) {
	console.log("##createOffer");

	peer = dest;
	pc.createOffer(localDescCreated, // SDP 생성 완료 시
	logError);
}

function localDescCreated(desc) {

	console.log("##localDescCreated : " + desc);

	// 생성된 SDP를 로컬 SDP로 설정
	pc.setLocalDescription(desc, function() {
		console.log("##setLocalDescription");
		// 상대 peer에게 SDP 전송 (이것이 signaling!)
		sendMessage({
			type : "rtc",
			dest : peer,
			data : {
				'sdp' : pc.localDescription
			}
		});
	}, logError);

};

function sendMessage(payload) {
	sock.send(JSON.stringify(payload));
}

function disconnect() {
	console.log('연결 종료');
	alert("소켓 연결 종료!")
	if (sock != null) {
		sock.close();
	}

	setConnected(false);
}