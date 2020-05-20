navigator.getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.msGetUserMedia;
var loggedIn = false;
var configuration = {
  'iceServers': [
	  {
		  'urls' : [
			  'stun:stun.l.google.com:19302',
			  'stun:stun2.l.google.com:19302',
			  'stun:stun3.l.google.com:19302',
			  'stun:stun4.l.google.com:19302'
		  ]
	  },
	  
//	  {url:'stun:stun01.sipphone.com'},
//	  {url:'stun:stun.ekiga.net'},
//	  {url:'stun:stun.fwdnet.net'},
//	  {url:'stun:stun.ideasip.com'},
//	  {url:'stun:stun.iptel.org'},
//	  {url:'stun:stun.rixtelecom.se'},
//	  {url:'stun:stun.schlund.de'},
//	  {url:'stun:stunserver.org'},
//	  {url:'stun:stun.softjoys.com'},
//	  {url:'stun:stun.voiparound.com'},
//	  {url:'stun:stun.voipbuster.com'},
//	  {url:'stun:stun.voipstunt.com'},
//	  {url:'stun:stun.voxgratia.org'},
//	  {url:'stun:stun.xten.com'},
//	  {url:'stun:s1.voipstation.jp'},
//	  {url:'stun:s2.voipstation.jp'},
//	  {url:'stun:stun.services.mozilla.com'},
//	  {url:'stun:s2.voipstation.jp'},
	  {
		  url: 'turn:numb.viagenie.ca',
		  credential: 'muazkh',
		  username: 'webrtc@live.com'
	  },
	  {
		  url: 'turn:192.158.29.39:3478?transport=udp',
		  credential: 'JZEOEt2V3Qb0y27GRntt2u2PAYA=',
		  username: '28224511:1379330808'
	  },
	  {
		  url: 'turn:192.158.29.39:3478?transport=tcp',
		  credential: 'JZEOEt2V3Qb0y27GRntt2u2PAYA=',
		  username: '28224511:1379330808'
	  }
  ]
};

var pc;
var peer;

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
    console.error('WebRTC 1.0 (RTCPeerConnection) API seems NOT available in this browser.');
}


function logError(error) {
  console.log(error.name + ': ' + error.message);
}

function connect(username) {
  console.log('connect');
  var loc = window.location
  var uri = CLIENT_SOCKET_PROTOCOL + "://" + loc.hostname + "/signal"
  console.log("uri : " + uri);

  sock = new WebSocket(uri);

  sock.onopen = function(e) {
    console.log('open', e);
    sock.send(
      JSON.stringify(
        {
          type: "login",
          data: username
        }
      )
    );
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
    if (!pc) {
      startRTC();
    }

    var message = JSON.parse(e.data);
    if (message.type === 'rtc') {
      if (message.data.sdp) {
        pc.setRemoteDescription(
          new RTCSessionDescription(message.data.sdp),
          function () {
            // if we received an offer, we need to answer
            if (pc.remoteDescription.type == 'offer') {
              peer = message.dest;
              pc.createAnswer(localDescCreated, logError);
            }
          },
          logError);
        }
        else {
          pc.addIceCandidate(new RTCIceCandidate(message.data.candidate));
        }
      }
    }

    //setConnected(true);
  }

  function startRTC() {
    pc = new RTCPeerConnection(configuration);
    // send any ice candidates to the other peer
    pc.onicecandidate = function (evt) {
      if (evt.candidate) {
        sendMessage(
          {
            type: "rtc",
            dest: peer,
            data: {
              'candidate': evt.candidate
            }
          }
        );
      }
    };

    // 일단 원격 스트림이 도착하면 원격 비디오 엘리먼트 안에 그것을 보여줍니다.
    pc.onaddstream = function (evt) {
    	remoteView.srcObject = evt.stream;
    };

    // 로컬 스트림을 받으면 그것을 자체 뷰에서 보여주고 전송을 위해 추가합니다.
    navigator.getUserMedia({
      'audio': true,
      'video': true
    }, function (stream) {
      selfView.srcObject = stream;
      pc.addStream(stream);
    }, logError);

  }

  function offer(dest) {
    peer = dest;
    pc.createOffer(localDescCreated, logError);
  }

  function localDescCreated(desc) {
    pc.setLocalDescription(desc, function () {
      // ici en voyé un obj {type: offer, dest: B, data: desc}
      sendMessage(
        {
          type: "rtc",
          dest: peer,
          data: {
            'sdp': pc.localDescription
          }
        }
      );
    }, logError);
  };

  function sendMessage(payload) {
    sock.send(JSON.stringify(payload));
  }

  function disconnect() {
    console.log('disconnect');
    if(sock != null) {
      sock.close();
    }
    setConnected(false);
  }
