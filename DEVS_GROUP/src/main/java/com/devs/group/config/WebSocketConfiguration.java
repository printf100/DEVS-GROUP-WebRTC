package com.devs.group.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.devs.group.common.socket.SocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//		registry.addHandler(new SocketHandler(), "/socket").setAllowedOrigins("*");
		registry
				// handle on "/signal" endpoint
				.addHandler(signalingSocketHandler(), "/signal")
				// Allow cross origins
				.setAllowedOrigins("*");
	}

	@Bean
	public WebSocketHandler signalingSocketHandler() {
		return new SocketHandler();
	}

}
