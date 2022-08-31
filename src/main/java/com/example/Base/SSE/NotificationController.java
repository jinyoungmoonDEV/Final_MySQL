package com.example.Base.SSE;

import com.example.Base.domain.dto.user.UserDTO;
import com.example.Base.service.token.TokenServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
@Log4j2
public class NotificationController {

    private final NotificationService notificationService;

    private final TokenServiceImpl tokenService;

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId, HttpServletRequest request,HttpServletResponse response){

//        UserDTO user = tokenService.decodeJWT(request);
//        String email = user.getEmail();

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/event-stream");
        response.setHeader("Content-Type", "text/event-stream");
        response.setHeader("Cache-Control","no-cache");
        response.setHeader("Connection", "keep-alive");

        return notificationService.subscribe("user@gmail.com", lastEventId);
    }

//    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public Flux<ServerSentEvent> subscribe(@RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId, HttpServletRequest request, HttpServletResponse response){
//        return Flux.interval(Duration.ofSeconds(1))
//                .map(i -> ServerSentEvent.builder("data" + i).build());
//    }

}