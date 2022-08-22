//package com.example.Base.SSE;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestHeader;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//
//@RestController
//@RequiredArgsConstructor
//public class NotificationController {
//
//    private final NotificationService notificationService;
//
//    @GetMapping(value = "/subscribe/{email}", produces = "text/event-stream")
//    public SseEmitter subscribe(@PathVariable String email, @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId){
//        return notificationService.subscribe(email, lastEventId);
//    }
//}
