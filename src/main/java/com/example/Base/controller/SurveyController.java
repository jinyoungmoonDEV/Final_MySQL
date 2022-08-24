package com.example.Base.controller;

import com.example.Base.domain.entity.UserEntity;
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
    @GetMapping("/survey/all")
    public Mono<List> showSurveys() {
        return webClient.get()
                .uri("/survey/all")
                .retrieve()
                .bodyToMono(List.class);
    }

    @PostMapping("/matchedList")
    public Mono<List> getCategory(@RequestBody Map<String,String> emailInfo) {

        String a = emailInfo.get("email");
        String categoryInfo = userService.getCategory(a);  // email 정보로 고수회원 정보에서 카테고리 뽑아서 survey 서버로 보내주기

        return webClient.post()
                .uri("/matchedList/" + categoryInfo)
                .retrieve()
                .bodyToMono(List.class);
    }

    @PostMapping("/quotation/{id}")
    public Mono<SurveyDto> getCategory(@PathVariable String id, @RequestBody Map<String, String> emailInfo) {

        String emailValue = emailInfo.get("email");
        UserEntity gosuInfo = userService.getUser(emailValue);  // 고수 정보 조회
        String gosuName = gosuInfo.getName();
        Integer gosuAge = gosuInfo.getAge();
        String gosuGender = gosuInfo.getGender();
        String gosuCategory = gosuInfo.getCategory();
        String gosuRegion = gosuInfo.getAddress();
//        Integer gosuCareer = gosuInfo.getCareer();

        return webClient.post()
                .uri("/quotation/" + id)  // <-- 고수회원 정보 List type으로 넘겨주기
                .headers(httpHeaders ->
                {httpHeaders.add("GosuName", gosuName);
                    httpHeaders.add("GosuAge", gosuAge.toString());
                    httpHeaders.add("GosuGender", gosuGender);
                    httpHeaders.add("GosuCategory", gosuCategory);
                    httpHeaders.add("GosuRegion", gosuRegion);
//                    httpHeaders.add("GosuCareer", gosuCareer.toString());
                })
                .retrieve()
                .bodyToMono(SurveyDto.class);
    }


//    @PostMapping("/quotation/{id}")
//    public Mono<SurveyDto> getCategory(@PathVariable String id, @RequestBody Map<String, String> emailInfo) {
//        DecodedTokenDto a = new DecodedTokenDto();
//        String emailValue = a.getEmail();
//        log.info("email in from jwt : " + emailValue);
////        String emailValue = emailInfo.get("email");
//        UserEntity gosuInfo = userService.getUser(emailValue);  // 고수 정보 조회
//        String gosuName = gosuInfo.getName();
//        Integer gosuAge = gosuInfo.getAge();
//        String gosuGender = gosuInfo.getGender();
//        String gosuCategory = gosuInfo.getCategory();
//        String gosuRegion = gosuInfo.getAddress();
//
//        return webClient.post()
//                .uri("/quotation/" + id)  // <-- 고수회원 정보 List type으로 넘겨주기
//                .headers(httpHeaders ->
//                {httpHeaders.add("GosuName", gosuName);
//                    httpHeaders.add("GosuAge", gosuAge.toString());
//                    httpHeaders.add("GosuGender", gosuGender);
//                    httpHeaders.add("GosuCategory", gosuCategory);
//                    httpHeaders.add("GosuRegion", gosuRegion);
//                })
//                .retrieve()
//                .bodyToMono(SurveyDto.class);
//    }
/*--------------------------------------------------------------*/

/*    @PostMapping("/quotation/{id}")
    public Mono<SurveyDto> getCategory(@PathVariable String id) {

        DecodedTokenDto forEmail = new DecodedTokenDto();
        String a = forEmail.getEmail();

        log.info("email in from jwt 여기만 오면 된다;; ㄹㅇ : " + a);
//        String emailValue = emailInfo.get("email");
        UserEntity gosuInfo = userService.getUser(a);  // 고수 정보 조회
        String gosuName = gosuInfo.getName();
        Integer gosuAge = gosuInfo.getAge();
        String gosuGender = gosuInfo.getGender();
        String gosuCategory = gosuInfo.getCategory();
        String gosuRegion = gosuInfo.getAddress();

        return webClient.post()
                .uri("/quotation/" + id)  // <-- 고수회원 정보 List type으로 넘겨주기
                .headers(httpHeaders ->
                {httpHeaders.add("GosuName", gosuName);
                    httpHeaders.add("GosuAge", gosuAge.toString());
                    httpHeaders.add("GosuGender", gosuGender);
                    httpHeaders.add("GosuCategory", gosuCategory);
                    httpHeaders.add("GosuRegion", gosuRegion);
                })
                .retrieve()
                .bodyToMono(SurveyDto.class);
    }*/
}
