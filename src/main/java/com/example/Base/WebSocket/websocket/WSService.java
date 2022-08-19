//package com.example.Base.WebSocket.websocket;
//
//import com.example.Base.SSE.NotificationService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class WSService {
//
//    private final SimpMessagingTemplate messagingTemplate;
//
//    private final NotificationService notificationService;
//
//    public void notifyFrontEnd(final String message) {
//        ResponseMessageDTO responseMessageDTO = new ResponseMessageDTO(message);
//
//        notificationService.sendGlobalNotification();
//        messagingTemplate.convertAndSend("/topic/messages", responseMessageDTO);
//    }
//
//    public void notifyUser(final String id, final String message) {
//        ResponseMessageDTO responseMessageDTO = new ResponseMessageDTO(message);
//
//        notificationService.sendPrivateNotification(id);
//        messagingTemplate.convertAndSendToUser(id, "/topic/private-message", responseMessageDTO);
//    }
//}
