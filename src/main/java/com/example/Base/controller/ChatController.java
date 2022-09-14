package com.example.Base.controller;

import com.example.Base.SSE.NotificationService;
import com.example.Base.SSE.domain.NotificationType;
import com.example.Base.domain.dto.QuotationDto;
import com.example.Base.domain.dto.chat.ChatDTO;
import com.example.Base.service.user.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(value = "ChatController WebClient")
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

    @ApiOperation(value = "생성", notes = "채팅방 생성")
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

    @ApiOperation(value = "입력", notes = "채팅 입력")
    @PostMapping("/insert")
    public ResponseEntity setMsg(@RequestBody ChatDTO chatDTO){

        Mono<ChatDTO> result =  webClient.post()
                .uri("/chat/insert")
                .bodyValue(chatDTO)
                .retrieve()
                .bodyToMono(ChatDTO.class);

        List<ChatDTO> resultList = new ArrayList<>();

        resultList.add(result.block());

        ChatDTO input = resultList.get(0);

        String user = input.getUser();
        String gosu = input.getGosu();
        String room = input.getRoom().toString();

        Integer index = input.getInfo().size()-1;

        if (input.getInfo().get(index).getUser() != null){
            log.info("gosu");
            notificationService.send(gosu,user + "님의 새로운 채팅", NotificationType.CHAT_INSERTED, room);
        }
        else {
            log.info(user);
            notificationService.send(user, gosu + "님의 새로운 채팅", NotificationType.CHAT_INSERTED, room);
        }

        return ResponseEntity.ok().body("inserted");
    }

    @ApiOperation(value = "이력 조회", notes = "채팅 이력 조회")
    @GetMapping(value = "/sender/room/{room}")
    public Mono<ChatDTO> getMsg(@PathVariable Integer room){

        return webClient.get()
                .uri("/chat/sender/room/"+ room)
                .acceptCharset(Charset.forName("UTF-8"))
                .retrieve()
                .bodyToMono(ChatDTO.class);
    }

    @ApiOperation(value = "리스트 조회", notes = "채팅 리스트 조회")
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
