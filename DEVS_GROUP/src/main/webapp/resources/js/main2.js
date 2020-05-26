navigator.getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.msGetUserMedia;

var loggedIn = false;

var configuration = {
      'iceServers': [
         {
            'urls' : [
               'stun:stun.l.google.com:19302',
               'stun:stun2.l.google.com:19302'
//               'stun:stun3.l.google.com:19302',
//               'stun:stun4.l.google.com:19302'
               ]
         },
     
//     {url:'stun:stun01.sipphone.com'},
//     {url:'stun:stun.ekiga.net'},
//     {url:'stun:stun.fwdnet.net'},
//     {url:'stun:stun.ideasip.com'},
//     {url:'stun:stun.iptel.org'},
//     {url:'stun:stun.rixtelecom.se'},
//     {url:'stun:stun.schlund.de'},
//     {url:'stun:stunserver.org'},
//     {url:'stun:stun.softjoys.com'},
//     {url:'stun:stun.voiparound.com'},
//     {url:'stun:stun.voipbuster.com'},
//     {url:'stun:stun.voipstunt.com'},
//     {url:'stun:stun.voxgratia.org'},
//     {url:'stun:stun.xten.com'},
//     {url:'stun:s1.voipstation.jp'},
//     {url:'stun:s2.voipstation.jp'},
//     {url:'stun:stun.services.mozilla.com'},
//     {url:'stun:s2.voipstation.jp'},
     
//         {
//            url: 'turn:numb.viagenie.ca',
//            credential: 'muazkh',
//            username: 'webrtc@live.com'
//         },
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

var pc;   // RTCPeerConnection 객체
var peer;

var connections = [];   // 소켓에 접속한 아이디 리스트
var localStream;
var memberId;      // 내 멤버 아이디

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
    console.error('WebRTC 1.0 (RTCPeerConnection) API seems NOT available in this browser.');
}


function logError(error) {
  console.log(error.name + ' : ' + error.message);
}

// 소켓 연결
function connect(memberid) {
   console.log('연결됨');
  
   var loc = window.location
   var uri = CLIENT_SOCKET_PROTOCOL + "://" + loc.hostname + "/signal"
   console.log("uri : " + uri);

   sock = new WebSocket(uri);
   
   var constraints = {
         video: true,
         audio: true
   };
   
   if(navigator.getUserMedia) {
      console.log("##getUserMedia");
      
      // signaling을 통해 connection이 이루어지기 전에 미리 자신의 video/audio 스트림 취득
      navigator.getUserMedia(constraints, getUserMediaSuccess);

      sock.onopen = function(e) {
         console.log('open', e);
         
         memberId = memberid;
         
         sock.send(JSON.stringify({
                     type: "login",
                     data: memberid
                  })
               );
         
         sendMessage({
            type: "enter",
            data: memberid
         });
         
         if(!pc) {
            pc = new RTCPeerConnection(configuration);
            console.log("##create RTCPeerConnection : " + memberid);
         }
         
         // onicecandidate : 내 네트워크 정보가 확보되면 실행될 callback 셋팅
         pc.onicecandidate = function(evt) {
            
            // signaling server를 통해 상대 peer에게 전송
            if (evt.candidate != null) {
               console.log("##onicecandidate : ", evt.candidate);
               sendMessage({
                  type: "signal",
                  data: { 'candidate': evt.candidate }
               });
            }
         };
         
         // 상대방 화면 셋팅
         pc.onaddstream = function(evt) {
//            console.log("##onaddstream : " + loginId);
            console.log("##onaddstream");
            gotRemoteStream(evt);
         };

         loggedIn = true;
      }
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
      
       // enter
      if(message.type === "enter") {
         
         connections = message.loginIds;   // 전역 배열에 소켓에 접속한 아이디 리스트 셋팅
         console.log("##connections : " + connections);

         createOfferToClients();
      }
      
      // 접속 종료한 멤버 화면 없애기
      if(message.type === "disconnect") {
         console.log("##disconnect : " + message.data);
         var video = document.querySelector('[data-socket="'+ message.data +'"]');
            var parentDiv = video.parentElement;
            video.parentElement.parentElement.removeChild(parentDiv);
      }

    }// END onmessage
   
   
}


function createOfferToClients() {
   connections.forEach(function(loginId) {
      console.log("forEach : " + loginId);
      
      if(loginId != memberId) {
         console.log("loginId != memberId", pc);

         offer(loginId);         
      }
      
   });
}

//송신자는 미디어 정보 입력 후, 상대 peer에게 전달할 SDP 생성
//SDP(Session Description Protocol) : 내 브라우저에서 사용가능한 코덱이나 해상도에 대한 정보 (offer / answer)
function offer(dest) {
   console.log("##createOffer");
   
   peer = dest;
   pc.createOffer(
          localDescCreated,      // SDP 생성 완료 시
          logError
       );
}

function localDescCreated(description) {
   
   console.log("##localDescCreated", description);
   
   // 생성된 SDP를 로컬 SDP로 설정
   pc.setLocalDescription(description, function () {
      console.log("##setLocalDescription");
      
       // 상대 peer에게 SDP 전송
      sendMessage({
               type: "signal",
                dest: peer,
                data: { 'sdp': pc.localDescription }
             });
      
   }, logError);
 
};

// 내 화면 셋팅
function getUserMediaSuccess(stream) {
   console.log("##내 화면 입력 ", stream);
    myVideo.srcObject = stream;
   pc.addStream(stream);
}

// 상대방 화면 셋팅
//function gotRemoteStream(event, id) {
function gotRemoteStream(event) {
//   console.log("##gotRemoteStream : " + id);
   console.log("##gotRemoteStream");

    var videos = document.querySelectorAll('video'),
       video = document.createElement('video'),
       div = document.createElement('div');

//    video.setAttribute('data-socket', id);
    video.srcObject = event.stream;
    video.autoplay = true; 
    video.muted = false;
    video.playsinline = true;
    
    div.appendChild(video);
    document.querySelector('.videos').appendChild(div);
}

function gotMessageFromServer(fromId, message) {
   console.log("##gotMessageFromServer : " + fromId);

    var signal = JSON.parse(message)

    if(fromId != memberId) {
        
       if(signal.data.sdp){
           
           // 상대 peer로부터 전달받은 SDP 셋팅
            pc.setRemoteDescription(new RTCSessionDescription(signal.data.sdp), function() {
               console.log("##setRemoteDescription : " + fromId);
               
                if(signal.data.sdp.type == 'offer') {
                   console.log("##offer SDP");
                   
                    pc.createAnswer(function(description) {
                       console.log("##createAnswer : " + fromId);

                        pc.setLocalDescription(description, function() {
                           console.log("##setLocalDescription : " + fromId);

                           sendMessage({
                              type: "signal",
                              dest: fromId,
                              data: { 'sdp': connections[fromId].localDescription }
                           });
                           
                        }, logError);
                        
                    }, logError);
                }
                
            }, logError);
        }
    
        if(signal.data.candidate) {
           // 도착한 상대 peer의 네트워크 정보 등록
            pc.addIceCandidate(new RTCIceCandidate(signal.data.candidate)).catch(e => console.log(e));
        }                
    }
}

function sendMessage(payload) {
    sock.send(JSON.stringify(payload));
}

function disconnect() {
    console.log('연결 종료');
    
    if(sock != null) {
      sock.close();
    }
    
    setConnected(false);
}