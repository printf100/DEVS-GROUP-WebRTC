package com.devs.group.common.socket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.devs.group.model.vo.SignalMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class SocketHandler extends TextWebSocketHandler {

	private static final Logger log = LoggerFactory.getLogger(SocketHandler.class);

	private static final String LOGIN_TYPE = "login";
	private static final String RTC_TYPE = "rtc";

	// Jackson JSON converter
	private ObjectMapper objectMapper = new ObjectMapper();

	// This map saves sockets by usernames
	private Map<String, WebSocketSession> clients = new HashMap<String, WebSocketSession>();

	// Thus map saves username by socket ID
	private Map<String, String> clientIds = new HashMap<String, String>();

	// Message Handler
	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message)
			throws InterruptedException, IOException {
		log.debug("handleTextMessage : {}", message.getPayload());

		SignalMessage signalMessage = objectMapper.readValue(message.getPayload(), SignalMessage.class);

		if (LOGIN_TYPE.equalsIgnoreCase(signalMessage.getType())) {
			loginProcess(session, signalMessage);
		} else if (RTC_TYPE.equalsIgnoreCase(signalMessage.getType())) {
			rtcProcess(session, signalMessage);
		}

	}

	// 로그인
	private void loginProcess(WebSocketSession session, SignalMessage signalMessage) {
		// It's a login message so we assume data to be a String representing the
		// username
		String username = (String) signalMessage.getData();

		WebSocketSession client = clients.get(username);

		// quick check to verify that the username is not already taken and active
		if (client == null || !client.isOpen()) {
			log.debug("Login {} : OK", username);
			// saves socket and username
			clients.put(username, session);
			clientIds.put(session.getId(), username);
		} else {
			log.debug("Login {} : KO", username);
		}

	}

	private void rtcProcess(WebSocketSession session, SignalMessage signalMessage) throws IOException {
		// with the dest username, we can find the targeted socket, if any
		String dest = signalMessage.getDest();
		WebSocketSession destSocket = clients.get(dest);
		// if the socket exists and is open, we go on
		if (destSocket != null && destSocket.isOpen()) {

			// We write the message to send to the dest socket (it's our propriatary format)

			SignalMessage out = new SignalMessage();
			// still an RTC type
			out.setType(RTC_TYPE);
			// we use the dest field to specify the actual exp., but it will be the next
			// dest.
			out.setDest(clientIds.get(session.getId()));
			// The data stays as it is
			out.setData(signalMessage.getData());

			// Convert our object back to JSON
			String stringifiedJSONmsg = objectMapper.writeValueAsString(out);

			log.debug("send message {}", stringifiedJSONmsg);

			destSocket.sendMessage(new TextMessage(stringifiedJSONmsg));
		}
	}

	// 클라이언트와 연결을 끊겼을 때 실행되는 메소드
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

		String username = clientIds.get(session.getId());
		clients.remove(username);

		System.out.println("연결 끊김 : " + username);
	}
}