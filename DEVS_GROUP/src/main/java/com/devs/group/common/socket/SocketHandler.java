package com.devs.group.common.socket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.devs.group.common.ssohandler.web.SsoController;
import com.devs.group.model.vo.SignalMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class SocketHandler extends TextWebSocketHandler {

	private static final Logger log = LoggerFactory.getLogger(SsoController.class);

	private static final String LOGIN_TYPE = "login";
	private static final String ENTER_TYPE = "enter";
	private static final String RTC_TYPE = "rtc";
	private static final String SIGNAL_TYPE = "signal";

	// Jackson JSON converter
	private ObjectMapper objectMapper = new ObjectMapper();

	// <멤버 아이디, 세션>
	private Map<String, WebSocketSession> clients = new HashMap<String, WebSocketSession>();

	// <세션 아이디, 멤버 아이디>
	private Map<String, String> clientIds = new HashMap<String, String>();

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message)
			throws InterruptedException, IOException {
		log.debug("handleTextMessage : {}", message.getPayload());

		SignalMessage signalMessage = objectMapper.readValue(message.getPayload(), SignalMessage.class);

		if (LOGIN_TYPE.equalsIgnoreCase(signalMessage.getType())) {
			loginProcess(session, signalMessage);
		}

		else if (ENTER_TYPE.equalsIgnoreCase(signalMessage.getType())) {
			enterProcess(session, signalMessage);
		}

		else if (RTC_TYPE.equalsIgnoreCase(signalMessage.getType())) {
			rtcProcess(session, signalMessage);
		}

		else if (SIGNAL_TYPE.equalsIgnoreCase(signalMessage.getType())) {
			signalProcess(session, signalMessage);
		}

	}

	private void loginProcess(WebSocketSession session, SignalMessage signalMessage) throws IOException {
		// It's a login message so we assume data to be a String representing the
		// username
		String username = (String) signalMessage.getData();

		WebSocketSession client = clients.get(username);

		// quick check to verify that the username is not already taken and active
		if (client == null || !client.isOpen()) {
			log.debug("{} 화상채팅 접속!!!!!", username);
			// saves socket and username
			clients.put(username, session);
			clientIds.put(session.getId(), username);

		} else {
			log.debug("Login {} : KO", username);
		}
	}

	private void enterProcess(WebSocketSession session, SignalMessage signalMessage) throws IOException {

		String enterId = (String) signalMessage.getData();

		String value = enterId + " 접속!";
		System.out.println(value);

		List<String> valueList = new ArrayList<>(clientIds.values());
		System.out.println(valueList);

		// 소켓에 있는 사람 전체에게 입장했다고 알림
		for (String enter : clients.keySet()) {

			SignalMessage out = new SignalMessage();
			out.setType(ENTER_TYPE);
			out.setDest(enter);
			out.setEnterId(enterId);
			out.setConnections(valueList.size());
			out.setLoginIds(valueList);
			out.setData(value);

			String stringifiedJSONmsg = objectMapper.writeValueAsString(out);

			System.out.println(out.getEnterId() + "가 " + out.getDest() + "에게 메세지 전송 : " + stringifiedJSONmsg);
			log.debug("{} 에게 메세지 전송 : {}", out.getDest(), stringifiedJSONmsg);

			WebSocketSession loginSession = clients.get(enter);
			loginSession.sendMessage(new TextMessage(stringifiedJSONmsg));
		}
	}

	private void rtcProcess(WebSocketSession session, SignalMessage signalMessage) throws IOException {

		// dest로 멤버 아이디 추출해서 소켓세션 뽑기
		String dest = signalMessage.getDest();
		WebSocketSession destSocket = clients.get(dest);

		if (destSocket != null && destSocket.isOpen()) {

			SignalMessage out = new SignalMessage();

			out.setType(RTC_TYPE);
			out.setDest(clientIds.get(session.getId()));
			out.setData(signalMessage.getData());

			String stringifiedJSONmsg = objectMapper.writeValueAsString(out);

			log.debug("{} 에게 메세지 전송 : {}", out.getDest(), stringifiedJSONmsg);

			destSocket.sendMessage(new TextMessage(stringifiedJSONmsg));
		}
	}

	private void signalProcess(WebSocketSession session, SignalMessage signalMessage) throws IOException {

		// dest로 멤버 아이디 추출해서 소켓세션 뽑기
//      String dest = signalMessage.getDest();
//      WebSocketSession destSocket = clients.get(dest);

		List<String> valueList = new ArrayList<>(clientIds.values());
		System.out.println(valueList);

		// 소켓에 있는 사람 전체에게 입장했다고 알림
		for (String login : clients.keySet()) {

			SignalMessage out = new SignalMessage();
			out.setType(SIGNAL_TYPE);
			out.setData(signalMessage.getData());

			String stringifiedJSONmsg = objectMapper.writeValueAsString(out);

			System.out.println("모두에게 메세지 전송 : " + stringifiedJSONmsg);
			log.debug("모두에게 메세지 전송 : {}", stringifiedJSONmsg);

			WebSocketSession loginSession = clients.get(login);
			loginSession.sendMessage(new TextMessage(stringifiedJSONmsg));
		}

	}

	// 클라이언트와 연결을 끊겼을 때 실행되는 메소드
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

		String username = clientIds.get(session.getId());
		clients.remove(username);
		clientIds.remove(session.getId());

		System.out.println("연결 끊김 : " + session.getId() + " , " + username);

		// 소켓에 있는 사람 전체에게 종료했다고 알림
		for (String loginId : clients.keySet()) {

			SignalMessage out = new SignalMessage();
			out.setType("disconnect");
			out.setData(username);

			String stringifiedJSONmsg = objectMapper.writeValueAsString(out);

			WebSocketSession loginSession = clients.get(loginId);
			loginSession.sendMessage(new TextMessage(stringifiedJSONmsg));
		}
	}
}