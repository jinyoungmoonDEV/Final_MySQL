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

import java.net.URI;
import java.nio.charset.Charset;
import java.time.Duration;
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

    @PostMapping("/new")
    public Mono<ChatDTO> createRoom(@RequestBody ChatDTO chatDTO){
        String user = chatDTO.getUser();
        String gosu = chatDTO.getGosu();

        String userName = userService.getName(user);
        String gosuName = userService.getName(gosu);

        ChatDTO input = ChatDTO.builder()
                .user(userName)
                .gosu(gosuName)
                .info(chatDTO.getInfo())
                .build();

        return webClient.post()
                .uri("/chat/new")
                .bodyValue(input)
                .retrieve()
                .bodyToMono(ChatDTO.class);
    }

    @PostMapping("/insert")
    public ResponseEntity setMsg(@RequestBody ChatDTO chatDTO){
        Mono<ChatDTO> result =  webClient.post()
                .uri("/chat/insert")
                .bodyValue(chatDTO)
                .retrieve()
                .bodyToMono(ChatDTO.class);

        ChatDTO chat = result.share().block();
        Integer chatRoom = chat.getRoom();

        if (chatDTO.getInfo().get(0).getUser().isEmpty()){
            String sender =chatDTO.getInfo().get(0).getGosu();
            String user = chat.getUser();
            log.info("insert by gosu");
            notificationService.send(user, sender + "님의 새로운 채팅!", NotificationType.CHAT_INSERTED, chatRoom);
        }
        else {
            String sender =chatDTO.getInfo().get(0).getUser();
            String gosu = chat.getGosu();
            log.info("insert by user");
            notificationService.send(gosu, sender + "님의 새로운 채팅!", NotificationType.CHAT_INSERTED, chatRoom);
        }

        return ResponseEntity.created(URI.create("/chat/insert")).body("Inserted");
    }

//    @PostMapping("/insert")
//    public Mono<ChatDTO> setMsg(@RequestBody ChatDTO chatDTO){
//        return webClient.post()
//                .uri("/chat/insert")
//                .bodyValue(chatDTO)
//                .retrieve()
//                .bodyToMono(ChatDTO.class);
//    }

    @GetMapping(value = "/sender/room/{room}")
    public Mono<ChatDTO> getMsg(@PathVariable Integer room){
        return webClient.get()
                .uri("/chat/sender/room/"+ room)
                .acceptCharset(Charset.forName("UTF-8"))
                .retrieve()
                .bodyToMono(ChatDTO.class);
    }

    @GetMapping(value = "/list/{email}/{role}")
    public Flux<ChatDTO> getList(@PathVariable String email, @PathVariable String role){
        String name = userService.getName(email);
        return webClient.get()
                .uri("/chat/list/"+name+"/"+role)
                .acceptCharset(Charset.forName("UTF-8"))
                .retrieve()
                .bodyToFlux(ChatDTO.class);
    }
}
