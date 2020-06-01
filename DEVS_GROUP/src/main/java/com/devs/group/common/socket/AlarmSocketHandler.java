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
import com.devs.group.model.vo.SignalMessage;
import com.devs.group.service.MongoChatService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AlarmSocketHandler extends TextWebSocketHandler {

	private static final Logger log = LoggerFactory.getLogger(AlarmSocketHandler.class);

	@Autowired
	private MongoChatService mongoChatService;

	private ObjectMapper objectMapper = new ObjectMapper();

	private static final String CHANGE_CHANNEL_TYPE = "change_channel";
	private static final String MAKE_CHAT_ROOM_TYPE = "make_chat_room";
	private static final String MAKE_RTC_ROOM_TYPE = "make_rtc_room";

	// 세션, 채널번호
	public Map<WebSocketSession, Integer> sessionMap = new HashMap<>(); // <세션, 채팅방번호>

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		log.debug("handleTextMessage : {}", message.getPayload());

		SignalMessage signalMessage = objectMapper.readValue(message.getPayload(), SignalMessage.class);

		if (CHANGE_CHANNEL_TYPE.equalsIgnoreCase(signalMessage.getType())) {
			changeChannelProcess(session, signalMessage);
		} else if (MAKE_CHAT_ROOM_TYPE.equalsIgnoreCase(signalMessage.getType())) {
			makeChatRoomProcess(session, signalMessage);
		} else if (MAKE_RTC_ROOM_TYPE.equalsIgnoreCase(signalMessage.getType())) {
			makeRtcRoomProcess(session, signalMessage);
		} else {
			System.out.println("아무 타입도 아니야~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		}

	}

	// connection 후!!
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println(("Alarm socket 연결 : " + session.getId()));
		sessionMap.put(session, -1);
	}

	// 채널 입장상태 변경 메시지 보내기
	private void changeChannelProcess(WebSocketSession session, SignalMessage signalMessage) throws IOException {

		String fromId = signalMessage.getFromId();

		int AfterChannelcode = (Integer) ((HashMap<String, Object>) signalMessage.getData()).get("channelcode");

		// session 정보에 접속한 채널번호를 저장! 원래 존재하던 session은 중복되어 덮어써진다.
		sessionMap.put(session, AfterChannelcode);
		System.out.println("AlarmSocketHander - fromId : " + fromId + "이 " + AfterChannelcode + "번 채널 접속");

		// 현재 해당 채널에 접속중인 리스트 뽑기
		List<String> loginIds = new ArrayList<>();
		for (WebSocketSession sess : sessionMap.keySet()) {
			if (sessionMap.get(sess) == AfterChannelcode) {

				loginIds.add(((Member) sess.getAttributes().get("user")).getMemberid());
			}
		}

		// 현재 채널에 접속중인 사람들에게 리스트 보내기
		for (WebSocketSession sess : sessionMap.keySet()) {
			if (sessionMap.get(sess) == AfterChannelcode) {

				SignalMessage out = new SignalMessage();

				out.setType(CHANGE_CHANNEL_TYPE);
				out.setFromId(fromId);
				out.setLoginIds(loginIds);

				String stringifiedJSONmsg = objectMapper.writeValueAsString(out);

				sess.sendMessage(new TextMessage(stringifiedJSONmsg));
			}
		}
	}

	// 채팅룸 만들었을 때
	private void makeChatRoomProcess(WebSocketSession session, SignalMessage signalMessage) throws IOException {

		int Channelcode = (Integer) ((HashMap<String, Object>) signalMessage.getData()).get("channelcode");

		// 현재 채널에 접속중인 사람들에게 리스트 보내기
		for (WebSocketSession sess : sessionMap.keySet()) {
			if (sessionMap.get(sess) == Channelcode) {

				SignalMessage out = new SignalMessage();

				out.setType(MAKE_CHAT_ROOM_TYPE);

				String stringifiedJSONmsg = objectMapper.writeValueAsString(out);

				sess.sendMessage(new TextMessage(stringifiedJSONmsg));
			}
		}
	}

	// 화상채팅룸 만들었을 때
	private void makeRtcRoomProcess(WebSocketSession session, SignalMessage signalMessage) throws IOException {
		
		System.out.println("누가 방을 만들든 지우든 암튼 바꼇어~~~~~~~~~~~~~~~~~~~");

		int Channelcode = (Integer) ((HashMap<String, Object>) signalMessage.getData()).get("channelcode");

		// 현재 채널에 접속중인 사람들에게 리스트 보내기
		for (WebSocketSession sess : sessionMap.keySet()) {
			if (sessionMap.get(sess) == Channelcode) {

				SignalMessage out = new SignalMessage();

				out.setType(MAKE_RTC_ROOM_TYPE);

				String stringifiedJSONmsg = objectMapper.writeValueAsString(out);

				sess.sendMessage(new TextMessage(stringifiedJSONmsg));
			}
		}
	}

	// 클라이언트와 연결을 끊었을 때 실행되는 메소드 (채널 입장상태 변경 메시지)
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

		int BeforeChannelcode = sessionMap.get(session); // 참여중이던 채널
		String fromId = ((Member) session.getAttributes().get("user")).getMemberid();

		sessionMap.remove(session); // 세션맵에서 지우기

		// 이전 채널에 접속중인 리스트 뽑기
		List<String> loginIds = new ArrayList<>();
		for (WebSocketSession sess : sessionMap.keySet()) {
			if (sessionMap.get(sess) == BeforeChannelcode) {

				loginIds.add(((Member) sess.getAttributes().get("user")).getMemberid());
			}
		}

		// 이전 채널에 접속중인 사람들에게 리스트 보내기
		for (WebSocketSession sess : sessionMap.keySet()) {
			if (sessionMap.get(sess) == BeforeChannelcode) {

				SignalMessage out = new SignalMessage();

				out.setType(CHANGE_CHANNEL_TYPE);
				out.setFromId(fromId);
				out.setLoginIds(loginIds);

				String stringifiedJSONmsg = objectMapper.writeValueAsString(out);

				sess.sendMessage(new TextMessage(stringifiedJSONmsg));
			}
		}
		System.out.println(("Alarm socket 연결 끊김 : " + session.getId()));
	}

}