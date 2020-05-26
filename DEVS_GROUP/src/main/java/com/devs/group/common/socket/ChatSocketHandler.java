package com.devs.group.common.socket;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.devs.group.model.vo.ChatVo;
import com.devs.group.model.vo.MemberJoinProfileSimpleVo;
import com.devs.group.model.vo.SignalMessage;
import com.devs.group.service.MongoChatService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ChatSocketHandler extends TextWebSocketHandler {

	private static final Logger log = LoggerFactory.getLogger(ChatSocketHandler.class);

	@Autowired
	private MongoChatService mongoChatService;

	private ObjectMapper objectMapper = new ObjectMapper();

	private static final String CHAT_ENTER_TYPE = "chat_enter";
	private static final String CHAT_SEND_TYPE = "chat_send";
	private static final String CHAT_OUT_TYPE = "chat_out";

	private Map<WebSocketSession, Integer> sessionMap = new HashMap<>(); // <세션, 채팅방번호>

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		sessionMap.put(session, -1);
		System.out.println("WebSocketSession.getId() : " + session.getId());
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		log.debug("handleTextMessage : {}", message.getPayload());

		SignalMessage signalMessage = objectMapper.readValue(message.getPayload(), SignalMessage.class);

		if (CHAT_ENTER_TYPE.equalsIgnoreCase(signalMessage.getType())) {
			sendChatEnterMessage(session, signalMessage);
		} else if (CHAT_SEND_TYPE.equalsIgnoreCase(signalMessage.getType())) {
			sendChatMessage(session, signalMessage);
		} else if (CHAT_OUT_TYPE.equalsIgnoreCase(signalMessage.getType())) {
			sessionMap.put(session, -1);
		}

	}

	// 채팅방 입장 메시지 보내기
	private void sendChatEnterMessage(WebSocketSession session, SignalMessage signalMessage) throws IOException {

		System.out.println("입장입장입장입장");
		Member enterMember = (Member) session.getAttributes().get("user");
		HashMap<String, Object> signalMessageMap = (HashMap<String, Object>) signalMessage.getData();

		System.out.println(enterMember);
		System.out.println(signalMessageMap);

		int member_code = enterMember.getMembercode();
		int room_code = Integer.parseInt((String) signalMessageMap.get("room_code"));

		// session 정보에 접속한 방번호를 저장! 원래 존재하던 session은 중복되어 덮어써진다.
		sessionMap.put(session, room_code);
		System.out.println("member_code : " + member_code + "이 채팅방을 열었다아아아아아아!!!!! + " + room_code + "번 방");

		// 해당 방에 unread_member_code_list, 방 입장시 이 리스트에서 자신의 멤버코드를 지운다.
		mongoChatService.removeUnreadMemberCodeList(room_code, member_code);

		// 채팅에 접속중인 사람들에게 읽었음을 표시한다.
		ChatVo recentChat = mongoChatService.findRecentChat(room_code);

		for (WebSocketSession sess : sessionMap.keySet()) {
			if (sessionMap.get(sess) == room_code) {

				SignalMessage out = new SignalMessage();

				out.setType(CHAT_ENTER_TYPE);

				Map<String, Object> map = new HashMap<>();
				map.put("member_code", member_code);
				out.setData(map);

				String stringifiedJSONmsg = objectMapper.writeValueAsString(out);

				sess.sendMessage(new TextMessage(stringifiedJSONmsg));
			}
		}

		System.out.println("거의다왔다,,,");

	}

	// 채팅 메시지 보내기
	private void sendChatMessage(WebSocketSession session, SignalMessage signalMessage) throws IOException {
		Member enterMember = (Member) session.getAttributes().get("user");
		HashMap<String, Object> signalMessageMap = (HashMap<String, Object>) signalMessage.getData();

		int member_code = enterMember.getMembercode();
		int room_code = Integer.parseInt((String) signalMessageMap.get("room_code"));
		String chat_message = (String) signalMessageMap.get("message");

		System.out.println((room_code + "방, " + session.getId() + ", " + member_code + "로 부터 " + chat_message + " 받음"));

		// 가장 최근 채팅 document를 통해 현재 채팅방에 참여중인 사람들의 정보를 추출
		ChatVo recentChat = mongoChatService.findRecentChat(room_code);

		// 채팅방에 포함된 사람들의 멤버코드리스트
		List<Integer> unread_member_code_list = new ArrayList<>();
		for (MemberJoinProfileSimpleVo chat_member : recentChat.getMember_list()) {
			unread_member_code_list.add(chat_member.getMembercode());
		}

		// 현재 채팅방에 접속중인 사람은 unread에서 지운다.
		for (WebSocketSession sess : sessionMap.keySet()) {
			if (sessionMap.get(sess) == room_code) {
				unread_member_code_list.remove((Integer) ((Member) sess.getAttributes().get("user")).getMembercode());
			}
		}

		// MongoDB에 insert
		ChatVo newChat = new ChatVo();

		newChat.setRoom_code(room_code);
		newChat.setMember_list(recentChat.getMember_list());
		newChat.setMember_code(member_code);

		newChat.setUnread_member_code_list(unread_member_code_list); // 현재는 모두가 읽지않은 상태!

		newChat.setMessage(chat_message);
		newChat.setMessage_date(new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm").format(new Date()));

		// 소켓통신 이용하여 채팅방에 전송
		for (WebSocketSession sess : sessionMap.keySet()) {
			for (MemberJoinProfileSimpleVo room_member : newChat.getMember_list()) {

				// 채팅방에 참여중인 멤버코드리스트 와 접속중인 멤버코드들 중 일치하는 코드가 있다면 메시지 보냄
				if (room_member.getMembercode() == ((Member) sess.getAttributes().get("user")).getMembercode()) {

					SignalMessage out = new SignalMessage();

					out.setType(CHAT_SEND_TYPE);

					Map<String, Object> map = new HashMap<>();
					map.put("writer_img", ((MemberProfile) session.getAttributes().get("profile")).getMemberImgServerName());
					map.put("new_chat", newChat);
					out.setData(map);

					String stringifiedJSONmsg = objectMapper.writeValueAsString(out);

					sess.sendMessage(new TextMessage(stringifiedJSONmsg));
				}
			}
		}

		ChatVo insertedChat = mongoChatService.insertChat(newChat);
	}

	// 클라이언트와 연결을 끊었을 때 실행되는 메소드
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		sessionMap.remove(session);
		System.out.println(("chatting socket 연결 끊김 : " + session.getId()));
	}

}
