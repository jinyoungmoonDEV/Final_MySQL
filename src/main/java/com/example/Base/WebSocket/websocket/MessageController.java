//package com.example.Base.WebSocket.websocket;
//
//import com.example.Base.SSE.NotificationService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.util.HtmlUtils;
//
//import java.security.Principal;
//
//@Controller
//@RequiredArgsConstructor
//public class MessageController {
//
//    private NotificationService notificationService;
//
//    @MessageMapping("/message")
//    @SendTo("/topic/messages")
//    public ResponseMessageDTO getMessage(final MessageDTO messageDTO) throws InterruptedException {
//        Thread.sleep(1000);
//        notificationService.sendGlobalNotification();
//        return new ResponseMessageDTO(HtmlUtils.htmlEscape(messageDTO.getMessageContent()));
//    }
//
//    @MessageMapping("/private-message")
//    @SendTo("/topic/private-messages")
//    public ResponseMessageDTO getPrivateMessage(final MessageDTO messageDTO, final Principal principal) throws InterruptedException {
//        Thread.sleep(1000);
//        notificationService.sendPrivateNotification(principal.getName());
//        return new ResponseMessageDTO(HtmlUtils.htmlEscape("Sending private message to user " + principal.getName() + ": " + messageDTO.getMessageContent()));
//    }
//}
