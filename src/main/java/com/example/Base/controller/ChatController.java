package com.example.Base.controller;

import com.example.Base.SSE.NotificationService;
import com.example.Base.SSE.domain.NotificationType;
import com.example.Base.domain.dto.chat.ChatDTO;
import com.example.Base.service.user.UserServiceImpl;
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
import reactor.netty.http.client.HttpClient;

import java.nio.charset.Charset;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@RestController
@Log4j2
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final UserServiceImpl userService;

    private final NotificationService notificationService;

    HttpClient client = HttpClient.create()
            .responseTimeout(Duration.ofSeconds(1));

    WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8020")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .clientConnector(new ReactorClientHttpConnector(client))
            .build();

    @PostMapping("/new") //채팅방 생성
    public Mono<ChatDTO> createRoom(@RequestBody ChatDTO info){
        String user = info.getUser();
        String gosu = info.getGosu();

        String userName = userService.getName(user);
        String gosuName = userService.getName(gosu);

        info.setUser(userName);
        info.setGosu(gosuName);

        return webClient.post()
                .uri("/chat/new")
                .bodyValue(info)
                .retrieve()
                .bodyToMono(ChatDTO.class);
    }

    @PostMapping("/insert") //채팅 input 저장
    public ResponseEntity setMsg(@RequestBody ChatDTO insert){

        Mono<ChatDTO> result =  webClient.post()
                .uri("/chat/insert")
                .bodyValue(insert)
                .retrieve()
                .bodyToMono(ChatDTO.class);

        List<ChatDTO> resultList = new ArrayList<>();

        resultList.add(result.block());

        ChatDTO input = resultList.get(0);

        String user = input.getUser();
        String gosu = input.getGosu();
        String room = input.getRoom().toString();

        String userEmail = userService.getEmail(user);
        String gosuEmail = userService.getEmail(gosu);

        Integer index = input.getInfo().size()-1;

        if (input.getInfo().get(index).getUser() != null){
            log.info(gosuEmail);
            notificationService.send(gosuEmail,user + "님의 새로운 채팅", NotificationType.CHAT_INSERTED, room);
        }
        else {
            log.info(userEmail);
            notificationService.send(userEmail, gosu + "님의 새로운 채팅", NotificationType.CHAT_INSERTED, room);
        }

        return ResponseEntity.ok().body("inserted");
    }

    @GetMapping(value = "/sender/room/{room}") //채팅이력 불러오기
    public Mono<ChatDTO> getMsg(@PathVariable Integer room){

        return webClient.get()
                .uri("/chat/sender/room/"+ room)
                .acceptCharset(Charset.forName("UTF-8"))
                .retrieve()
                .bodyToMono(ChatDTO.class);
    }

    @GetMapping(value = "/list/{email}/{role}") //채팅방 리스트 불러오기
    public Flux<ChatDTO> getList(@PathVariable String email, @PathVariable String role){
        String name = userService.getName(email);

        return webClient.get()
                .uri("/chat/list/"+name+"/"+role)
                .acceptCharset(Charset.forName("UTF-8"))
                .retrieve()
                .bodyToFlux(ChatDTO.class);
    }
}
