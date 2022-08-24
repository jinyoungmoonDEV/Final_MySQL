package com.example.Base.controller;

import com.example.Base.domain.dto.SurveyDto;
import com.example.Base.service.user.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
public class SurveyController {
    private final UserServiceImpl userService;

    WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8010")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    // survey data 저장
    @PostMapping("/category/{bigIdpParam}/survey/{SmallIdParam}/save")
    public Mono<String> saveSurvey(@RequestBody SurveyDto surveyDto,
                                   @PathVariable Long bigIdpParam,
                                   @PathVariable Long SmallIdParam) {

        String emailInfo = surveyDto.getEmail();  // 일반회원의 email 정보뽑기
        String nameInfo = userService.getName(emailInfo);  // 뽑은 email 정보로 일반회원 조회하고 name 정보빼서 SurveyDto에 set하고 Survey 서버로 보내기
        surveyDto.setName(nameInfo);  // set은 void type!!!

        return webClient.post()
                .uri("/category/" + bigIdpParam + "/survey/" + SmallIdParam + "/save")
                .bodyValue(surveyDto)
                .retrieve()
                .bodyToMono(String.class);
    }
    // 모든 survey 조회 (확인 용도로 만들어둠)
    @GetMapping("/category/all")
    public Mono<List> showSurveys() {
        return webClient.get()
                .uri("/category/all")
                .retrieve()
                .bodyToMono(List.class);
    }

    @PostMapping("/matchedList")
    public Mono<String> getCategory(@RequestBody Map<String,String> emailInfo) {

        String a = emailInfo.get("email");
        String categoryInfo = userService.getCategory(a);  // email 정보로 고수회원 정보에서 카테고리 뽑아서 survey 서버로 보내주기

        return webClient.post()
                .uri("/matchedList")
                .bodyValue(categoryInfo)
                .retrieve()
                .bodyToMono(String.class);
    }

    @GetMapping("/quotation/{id}")
    public Mono<SurveyDto> getCategory(@PathVariable String id) {
        return webClient.get()
                .uri("/matchedList/detail/" + id)
                .retrieve()
                .bodyToMono(SurveyDto.class);

    }
}