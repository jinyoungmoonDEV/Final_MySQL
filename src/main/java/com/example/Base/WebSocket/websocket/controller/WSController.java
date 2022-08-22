package com.example.Base.WebSocket.websocket.controller;

import com.example.Base.WebSocket.websocket.service.WSService;
import com.example.Base.WebSocket.websocket.dto.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WSController {

    private final WSService service;

    @PostMapping("/send-message")
    public void sendMessage(@RequestBody final Message message){
        service.notifyFrontEnd(message.getMessageContent());
    }

    @PostMapping("/send-private-message/{id}")
    public void sendPrivateMessage(@PathVariable final String id, @RequestBody final Message message){
        service.notifyUser(id, message.getMessageContent());
    }
}
