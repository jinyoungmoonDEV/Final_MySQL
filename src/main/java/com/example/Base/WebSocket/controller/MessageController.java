//package com.example.Base.WebSocket.controller;
//
//import com.example.Base.WebSocket.domain.Message;
//import lombok.RequiredArgsConstructor;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Controller;
//
//@Controller
//@RequiredArgsConstructor
//public class MessageController {
//
//    private final SimpMessagingTemplate simpMessagingTemplate;
//
//    // /app/application
//    @MessageMapping("/application")
//    @SendTo("/all/messages")
//    public Message send(final Message message) throws Exception{
//        return message;
//    }
//
//    @MessageMapping("/private")
//    public void sendToSpecificUser(@Payload Message message) {
//        simpMessagingTemplate.convertAndSendToUser(message.getSender(), "/specific", message);
//    }
//
//}
