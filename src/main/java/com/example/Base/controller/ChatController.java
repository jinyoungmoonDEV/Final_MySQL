package com.example.Base.controller;

import com.example.Base.domain.dto.ChatDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Log4j2
@RequestMapping("/chat")
public class ChatController {

    WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8082")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    @PostMapping("/insert")
    public Mono<ChatDTO> setMsg(){
        log.info("in!");
        return webClient.get()
                .uri("/chat/insert")
                .retrieve()
                .bodyToMono(ChatDTO.class);
    }

    @GetMapping("/sender/roomNum/{roomNum}")
    public Flux<ChatDTO> getMsg(@PathVariable String roomNum){
        log.info("in!");
        return webClient.get()
                .uri("/chat/sender/roomNum/"+ roomNum)
                .retrieve()
                .bodyToFlux(ChatDTO.class);
    }
}
