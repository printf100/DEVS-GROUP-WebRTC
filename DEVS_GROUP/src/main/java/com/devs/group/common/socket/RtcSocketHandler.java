package com.devs.group.common.socket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.devs.group.common.ssohandler.domain.entity.Member;
import com.devs.group.common.ssohandler.domain.entity.MemberProfile;
import com.devs.group.common.ssohandler.web.SsoController;
import com.devs.group.model.entity.GroupChannel;
import com.devs.group.model.vo.RtcVo;
import com.devs.group.model.vo.SignalMessage;
import com.devs.group.service.MongoChatService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class RtcSocketHandler extends TextWebSocketHandler {

	private static final Logger log = LoggerFactory.getLogger(SsoController.class);

	@Autowired
	private MongoChatService mongoChatService;

	@Autowired
	private AlarmSocketHandler alarmSocketHandler;

	private static final String ENTER_TYPE = "enter";
	private static final String SIGNAL_TYPE = "signal";
	private static final String SCREEN_SIGNAL_TYPE = "screenSignal";
	private static final String CHAT_TYPE = "chat";
	private static final String DISCONNECT_TYPE = "disconnect";

	// Jackson JSON converter
	private ObjectMapper objectMapper = new ObjectMapper();

	// <멤버 아이디, 세션>
	private Map<String, WebSocketSession> clients = new HashMap<String, WebSocketSession>();

	// <멤버 아이디, 화상채팅방 번호>
	private Map<String, Integer> roomMember = new HashMap<String, Integer>();

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message)
			throws InterruptedException, IOException {
		log.debug("handleTextMessage : {}", message.getPayload());

		SignalMessage signalMessage = objectMapper.readValue(message.getPayload(), SignalMessage.class);

		if (ENTER_TYPE.equalsIgnoreCase(signalMessage.getType())) {
			enterProcess(session, signalMessage);
		}

		else if (SIGNAL_TYPE.equalsIgnoreCase(signalMessage.getType())) {
			signalProcess(session, signalMessage);
		}

		else if (SCREEN_SIGNAL_TYPE.equalsIgnoreCase(signalMessage.getType())) {
			screenSignalProcess(session, signalMessage);
		}

		else if (CHAT_TYPE.equalsIgnoreCase(signalMessage.getType())) {
			chatProcess(session, signalMessage);
		}

	}

	private void enterProcess(WebSocketSession session, SignalMessage signalMessage) throws IOException {

		String enterId = (String) signalMessage.getData();
		int room_code = roomMember.get(enterId);

		System.out.println(enterId + "가 " + room_code + "번방에 접속!");

		List<String> valueList = new ArrayList<>();

		for (String clientId : roomMember.keySet()) {

			if (roomMember.get(clientId) == room_code) {
				valueList.add(clientId);
			}
		}
		System.out.println(valueList);

		for (String clientId : roomMember.keySet()) {

			if (roomMember.get(clientId) == room_code) {
				SignalMessage out = new SignalMessage();
				out.setType(ENTER_TYPE);
				out.setToId(clientId);
				out.setFromId(enterId);
				out.setLoginIds(valueList);

				String stringifiedJSONmsg = objectMapper.writeValueAsString(out);

				System.out.println(out.getFromId() + "가 " + out.getToId() + "에게 메세지 전송 : " + stringifiedJSONmsg);
				log.debug("{}가 {}에게 메세지 전송 : {}", out.getFromId(), out.getToId(), stringifiedJSONmsg);

				WebSocketSession loginSession = clients.get(clientId);
				loginSession.sendMessage(new TextMessage(stringifiedJSONmsg));
			}
		}
	}

	private void signalProcess(WebSocketSession session, SignalMessage signalMessage) throws IOException {

		String sender = ((Member) session.getAttributes().get("user")).getMemberid();

		// dest로 멤버 아이디 추출해서 소켓세션 뽑기
		String dest = signalMessage.getToId();
		WebSocketSession destSocket = clients.get(dest);

		if (destSocket != null && destSocket.isOpen()) {

			SignalMessage out = new SignalMessage();
			out.setType(SIGNAL_TYPE);
			out.setToId(dest);
			out.setFromId(sender);
			out.setData(signalMessage.getData());

			String stringifiedJSONmsg = objectMapper.writeValueAsString(out);

			System.out.println(out.getFromId() + "가 " + out.getToId() + "에게 메세지 전송 : " + stringifiedJSONmsg);
			log.debug("{}가 {}에게 메세지 전송 : {}", out.getFromId(), out.getToId(), stringifiedJSONmsg);

			destSocket.sendMessage(new TextMessage(stringifiedJSONmsg));
		}

	}

	/*
	 * 화면 공유
	 */
	private void screenSignalProcess(WebSocketSession session, SignalMessage signalMessage) throws IOException {

		String sender = ((Member) session.getAttributes().get("user")).getMemberid();

		// dest로 멤버 아이디 추출해서 소켓세션 뽑기
		String dest = signalMessage.getToId();
		WebSocketSession destSocket = clients.get(dest);

		if (destSocket != null && destSocket.isOpen()) {

			SignalMessage out = new SignalMessage();
			out.setType(SCREEN_SIGNAL_TYPE);
			out.setToId(dest);
			out.setFromId(sender);
			out.setData(signalMessage.getData());

			String stringifiedJSONmsg = objectMapper.writeValueAsString(out);

			System.out.println(out.getFromId() + "가 " + out.getToId() + "에게 메세지 전송 : " + stringifiedJSONmsg);
			log.debug("{}가 {}에게 메세지 전송 : {}", out.getFromId(), out.getToId(), stringifiedJSONmsg);

			destSocket.sendMessage(new TextMessage(stringifiedJSONmsg));
		}
	}

	/*
	 * 채팅
	 */
	private void chatProcess(WebSocketSession session, SignalMessage signalMessage) throws IOException {

		String sender = ((Member) session.getAttributes().get("user")).getMemberid();
		String profileImageName = ((MemberProfile) session.getAttributes().get("profile")).getMemberImgServerName();

		int room_code = roomMember.get(sender);

		List<String> valueList = new ArrayList<>();

		for (String clientId : roomMember.keySet()) {

			if (roomMember.get(clientId) == room_code) {
				valueList.add(clientId);
			}
		}
		System.out.println(valueList);

		for (String clientId : roomMember.keySet()) {

			if (roomMember.get(clientId) == room_code) {
				SignalMessage out = new SignalMessage();
				out.setType(CHAT_TYPE);
				out.setFromId(sender);
				out.setFromProfileImageName(profileImageName);
				out.setData(signalMessage.getData());

				String stringifiedJSONmsg = objectMapper.writeValueAsString(out);

				System.out.println(out.getFromId() + "가 모두에게 메세지 전송 : " + stringifiedJSONmsg);
				log.debug("{}가 모두에게 메세지 전송 : {}", out.getFromId(), stringifiedJSONmsg);

				WebSocketSession loginSession = clients.get(clientId);
				loginSession.sendMessage(new TextMessage(stringifiedJSONmsg));
			}
		}
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {

		String loginId = ((Member) session.getAttributes().get("user")).getMemberid();

		WebSocketSession client = clients.get(loginId);

		if (client == null || !client.isOpen()) {
			log.debug("{} 화상채팅 접속!!!!!", loginId);
			clients.put(loginId, session);
			roomMember.put(loginId, ((RtcVo) session.getAttributes().get("roomInfo")).getRoom_code());

		} else {
			log.debug("Login {} : KO", loginId);
		}
	}

	// 클라이언트와 연결을 끊겼을 때 실행되는 메소드
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

		String sender = ((Member) session.getAttributes().get("user")).getMemberid();
		int room_code = roomMember.get(sender);

		clients.remove(sender);
		roomMember.remove(sender);

		System.out.println("연결 끊김 : " + session.getId() + " , " + sender);

		List<String> valueList = new ArrayList<>();

		for (String clientId : roomMember.keySet()) {

			if (roomMember.get(clientId) == room_code) {
				valueList.add(clientId);
			}
		}
		System.out.println(valueList);

		// 소켓 접속자가 0이면 화상채팅방 삭제
		if (valueList.size() == 0) {
			System.out.println(room_code + "번 방에 접속자가 이제 없어~~~~ 방 사라진다~~~~~? 방 없앤다!?!!~~~??~~?");
			mongoChatService.deleteRtcRoom(room_code);

			int channelcode = ((GroupChannel) session.getAttributes().get("channel")).getChannelcode();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("channelcode", channelcode);

			// 현재 채널에 접속중인 사람들에게 알람 보내기
			for (WebSocketSession sess : alarmSocketHandler.sessionMap.keySet()) {

				if (alarmSocketHandler.sessionMap.get(sess) == channelcode) {

					SignalMessage out = new SignalMessage();
					out.setType("make_rtc_room");
					out.setData(map);

					String stringifiedJSONmsg = objectMapper.writeValueAsString(out);
					alarmSocketHandler.handleTextMessage(session, new TextMessage(stringifiedJSONmsg));
				}

			}
		}

		for (String clientId : roomMember.keySet()) {

			if (roomMember.get(clientId) == room_code) {
				SignalMessage out = new SignalMessage();
				out.setType(DISCONNECT_TYPE);
				out.setFromId(sender);
				out.setLoginIds(valueList);

				String stringifiedJSONmsg = objectMapper.writeValueAsString(out);

				WebSocketSession loginSession = clients.get(clientId);
				loginSession.sendMessage(new TextMessage(stringifiedJSONmsg));
			}
		}
	}
}