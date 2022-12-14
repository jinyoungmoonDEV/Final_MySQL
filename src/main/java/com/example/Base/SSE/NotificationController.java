package com.example.Base.SSE;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
@Log4j2
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(value = "/subscribe/{email}", produces = MediaType.TEXT_EVENT_STREAM_VALUE) //구독
    public SseEmitter subscribe(@PathVariable String email, @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId){
        return notificationService.subscribe(email, lastEventId);
    }
}