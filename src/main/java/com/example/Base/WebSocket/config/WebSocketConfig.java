//package com.example.Base.WebSocket.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.simp.config.MessageBrokerRegistry;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
//import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
//import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
//
//@Configuration
//@EnableWebSocketMessageBroker //broker 사용
//public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
//
//    @Override
//    public void registerStompEndpoints(final StompEndpointRegistry registry) {
//        registry.addEndpoint("/ws"); //연결 시작점 "ws://localhost:8000/....."
//        registry.addEndpoint("/ws").withSockJS(); //socketjs 사용
//    }
//
//    @Override
//    public void configureMessageBroker(final MessageBrokerRegistry registry) {
//        registry.enableSimpleBroker("/sub"); //topic으로 소통
//        registry.setApplicationDestinationPrefixes("/pub"); //특정 컨트롤러로 <=> @MessageMapping
//    }
//}
