package com.example.Base.WebSocket.websocket.controller;

import com.example.Base.WebSocket.websocket.dto.Message;
import com.example.Base.WebSocket.websocket.dto.ResponseMessage;
import com.example.Base.WebSocket.websocket.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private NotificationService notificationService;

    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public ResponseMessage getMessage(final Message message) throws InterruptedException {
        Thread.sleep(1000);
        notificationService.sendGlobalNotification();
        return new ResponseMessage(HtmlUtils.htmlEscape(message.getMessageContent()));
    }

    @MessageMapping("/private-message")
    @SendTo("/topic/private-messages")
    public ResponseMessage getPrivateMessage(final Message message, final Principal principal) throws InterruptedException {
        Thread.sleep(1000);
        notificationService.sendPrivateNotification(principal.getName());
        return new ResponseMessage(HtmlUtils.htmlEscape("Sending private message to user " + principal.getName() + ": " + message.getMessageContent()));
    }
}
