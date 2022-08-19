//package com.example.Base.WebSocket;
//
//import com.fasterxml.jackson.databind.util.JSONPObject;
//import lombok.RequiredArgsConstructor;
//import org.json.JSONObject;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//import org.thymeleaf.engine.TemplateManager;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.concurrent.CopyOnWriteArrayList;
//
//@RestController
//@RequiredArgsConstructor
//public class AlramController {
//
//    public SseEmitter sseEmitter;
//
//    public List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
//
//    //method for client subscription
//    @RequestMapping(value = "/subscribe", consumes = MediaType.ALL_VALUE)
//    public SseEmitter subscribe() {
//        sseEmitter = new SseEmitter(Long.MAX_VALUE);
//        try {
//            sseEmitter.send(SseEmitter.event().name("INIT"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        sseEmitter.onCompletion(() -> emitters.remove(sseEmitter));
//
//        emitters.add(sseEmitter);
//        return sseEmitter;
//    }
//
//    //method for dispatching events to all clients
//    @PostMapping(value = "/dispatchEvent")
//    public void dispatchEventToClients (@RequestParam String type, @RequestParam String content) {
//
//        String eventFormatted = new JSONObject()
//                .put("type", type)
//                .put("content", content).toString();
//
//        for ( SseEmitter emitter : emitters) {
//            try {
//                emitter.send(SseEmitter.event().name("alarm").data(content));
//            } catch (IOException e) {
//                emitters.remove(emitter);
//            }
//        }
//    }
//
//}
