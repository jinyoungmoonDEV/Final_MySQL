package com.example.Base.controller;

import com.example.Base.domain.dto.ChatDTO;
import com.example.Base.domain.dto.UserDTO;
import com.example.Base.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@Log4j2
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final UserServiceImpl userService;

    HttpClient client = HttpClient.create()
            .responseTimeout(Duration.ofSeconds(1));

    WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8020")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .clientConnector(new ReactorClientHttpConnector(client))
            .build();

    @PostMapping("/new")
    public Mono<ChatDTO> createRoom(@RequestBody ChatDTO chatDTO){
        String user = chatDTO.getUser();
        String gosu = chatDTO.getGosu();
        log.info(user);

        String userName = userService.getName(user);
        log.info(userName);
        String gosuName = userService.getName(gosu);
        log.info("gosu");
        ChatDTO input = ChatDTO.builder()
                .user(userName)
                .gosu(gosuName)
                .build();
        log.info(input);

        return webClient.post()
                .uri("/chat/new")
                .bodyValue(input)
                .retrieve()
                .bodyToMono(ChatDTO.class);
    }

    @PostMapping("/insert")
    public Mono<ChatDTO> setMsg(@RequestBody ChatDTO chatDTO){
        String user = chatDTO.getUser();
        String gosu = chatDTO.getGosu();

        ChatDTO input = ChatDTO.builder()
                .msg(chatDTO.getMsg())
                .user(user)
                .gosu(gosu)
                .room(chatDTO.getRoom())
                .createdAt(chatDTO.getCreatedAt())
                .build();
        return webClient.post()
                .uri("/chat/insert")
                .bodyValue(input)
                .retrieve()
                .bodyToMono(ChatDTO.class);
    }

    @GetMapping(value = "/sender/room/{room}")
    public Mono<ChatDTO> getMsg(@PathVariable Integer room){
        return webClient.get()
                .uri("/chat/sender/room/"+ room)
                .retrieve()
                .bodyToMono(ChatDTO.class);
    }

    @GetMapping(value = "/list/{email}/{role}")
    public Mono<ChatDTO> getList(@PathVariable String email, @PathVariable String role){
        String name = userService.getName(email);
        return webClient.get()
                .uri("/chat/list/"+name+"/"+role)
                .retrieve()
                .bodyToMono(ChatDTO.class);
    }

//    @GetMapping("/room")
//    public Mono<String> chatInfo(@RequestBody ChatDTO chatDTO){
//        String user = chatDTO.getUser();
//        String gosu = chatDTO.getGosu();
//        String user_name = userService.getName(user);
//        String gosu_name = userService.getName(gosu);
//        log.info(user_name);
//        return webClient.get()
//                .uri("/chat/room/"+user_name+"/"+gosu_name)
//                .retrieve()
//                .bodyToMono(String.class);
//    }
}
