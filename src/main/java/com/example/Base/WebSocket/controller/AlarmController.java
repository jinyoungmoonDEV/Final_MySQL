//package com.example.Base.WebSocket.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.messaging.handler.annotation.DestinationVariable;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.simp.SimpMessageSendingOperations;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//
//@Controller
//@RequiredArgsConstructor
//public class AlarmController {
//
//    private final SimpMessageSendingOperations messageSendingOperations;
//
//    // stomp 테스트 화면
//    @GetMapping("/alarm/stomp")
//    public String stompAlarm() {
//        return "/stomp";
//    }
//
//    @MessageMapping("/{userId}")
//    public void message(@DestinationVariable("userId") Long userId) {
//        messageSendingOperations.convertAndSend("/sub/" + userId, "alarm socket connection completed.");
//    }
//}
