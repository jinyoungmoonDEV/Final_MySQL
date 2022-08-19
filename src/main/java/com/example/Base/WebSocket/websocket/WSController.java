//package com.example.Base.WebSocket.websocket;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//public class WSController {
//
//    private final WSService service;
//
//    @PostMapping("/send-message")
//    public void sendMessage(@RequestBody final MessageDTO messageDTO){
//        service.notifyFrontEnd(messageDTO.getMessageContent());
//    }
//}
