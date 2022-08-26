package com.example.Base.WebSocket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.socket.TextMessage;

@Controller
@RequiredArgsConstructor
public class AlarmController {

    private final SimpMessageSendingOperations messageSendingOperations;

    // /ws/email
    @MessageMapping("/{email}")
    public void connect(@DestinationVariable("email") String email) {
        messageSendingOperations.convertAndSend("/sub/" + email, "alarm socket connection completed.");
    }
}
