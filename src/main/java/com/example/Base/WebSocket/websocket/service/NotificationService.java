package com.example.Base.WebSocket.websocket.service;

import com.example.Base.WebSocket.websocket.dto.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendGlobalNotification(){
        ResponseMessage message = new ResponseMessage("Global Notification");

        messagingTemplate.convertAndSend("/topic/global-notification", message);
    }

    public void sendPrivateNotification(final String userid){
        ResponseMessage message = new ResponseMessage("Private Notification");

        messagingTemplate.convertAndSendToUser(userid,"/topic/private-notification", message);
    }

}
