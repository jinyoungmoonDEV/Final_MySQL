package com.example.Base.controller;

import com.example.Base.domain.dto.ChatDTO;
import com.example.Base.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Log4j2
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final UserServiceImpl userService;

    WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8082")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    @PostMapping("/insert")
    public Mono<ChatDTO> setMsg(@RequestBody ChatDTO chatDTO){
        log.info("in!");
        return webClient.post()
                .uri("/chat/insert")
                .bodyValue(chatDTO)
                .retrieve()
                .bodyToMono(ChatDTO.class);
    }

    @GetMapping("/sender/room/{room}")
    public Flux<ChatDTO> getMsg(@PathVariable Integer room){
        log.info("in!");
        return webClient.get()
                .uri("/chat/sender/room/"+ room)
                .retrieve()
                .bodyToFlux(ChatDTO.class);
    }

    @PostMapping("/room")
    public Mono<String> chatInfo(@RequestBody String email){
        String name = userService.getName(email);
        return webClient.get()
                .uri("/chat/room/"+name)
                .retrieve()
                .bodyToMono(String.class);
    }
}
