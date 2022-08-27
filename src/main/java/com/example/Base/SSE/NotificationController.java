//package com.example.Base.SSE;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//
//@RestController
//@RequiredArgsConstructor
//@CrossOrigin
//public class NotificationController {
//
//    private final NotificationService notificationService;
//
//    @GetMapping(value = "/subscribe", produces = "text/event-stream")
//    public SseEmitter subscribe(@RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId){
//        return notificationService.subscribe("user@gmail.com", lastEventId);
//    }
//}