package com.example.Base.controller;

import com.example.Base.domain.dto.ChatDTO;
import com.example.Base.domain.dto.UserDTO;
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

import java.util.Map;

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
        return webClient.post()
                .uri("/chat/insert")
                .bodyValue(chatDTO)
                .retrieve()
                .bodyToMono(ChatDTO.class);
    }

    @GetMapping(value = "/sender/room/{room}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatDTO> getMsg(@PathVariable Integer room){
        log.info(room);
        return webClient.get()
                .uri("/chat/sender/room/"+ room)

                .retrieve()
                .bodyToFlux(ChatDTO.class);
    }

    @GetMapping("/room")
    public Mono<String> chatInfo(@RequestBody ChatDTO chatDTO){
        String user = chatDTO.getUser();
        String gosu = chatDTO.getGosu();
        String user_name = userService.getName(user);
        String gosu_name = userService.getName(gosu);
        log.info(user_name);
        return webClient.get()
                .uri("/chat/room/"+user_name+"/"+gosu_name)
                .retrieve()
                .bodyToMono(String.class);
    }
}
