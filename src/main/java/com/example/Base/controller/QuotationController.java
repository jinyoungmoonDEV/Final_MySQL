package com.example.Base.controller;

import com.example.Base.domain.entity.UserEntity;
import com.example.Base.service.user.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Log4j2 @RequiredArgsConstructor
@RestController
public class QuotationController {

    private final UserServiceImpl userService;

    WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8010")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    // 견적서 저장 내용 : 견적서(quotation)를 작성한 고수 회원 정보 및 의뢰서(survey)+ 견적가격 및 소개글
    @PostMapping("/quotationSubmit/{id}")
    public Mono<String> savingQuotation(@PathVariable String id, @RequestBody QuotationDto quotationDto) {
        log.info("id from front : " + id);
        log.info("Gosun name on quotation from front : " + quotationDto.getGosuName());
        String emailInfo = quotationDto.getGosuEmail();
        UserEntity gosuInfo = userService.getUser(emailInfo);

        String gosuName = gosuInfo.getName();
        Integer gosuAge = gosuInfo.getAge();
        String gosuGender = gosuInfo.getGender();
        String gosuCategory = gosuInfo.getCategory();
        String gosuRegion = gosuInfo.getAddress();
        Integer gosuCareer = gosuInfo.getCareer();

        quotationDto.setGosuName(gosuName);
        quotationDto.setGosuAge(gosuAge);
        quotationDto.setGosuGender(gosuGender);
        quotationDto.setGosuCategory(gosuCategory);
        quotationDto.setGosuRegion(gosuRegion);
        quotationDto.setGosuCareer(gosuCareer);

        return webClient.post()
                .uri("/quotationSubmit/" + id)
                .bodyValue(quotationDto)
                .retrieve()
                .bodyToMono(String.class);
    }

    @PostMapping("/matchedgosulist")
    public Mono<List> searchingQuo(@RequestBody Map<String, String> emailInfo) {
        String emailfrom = emailInfo.get("email");
        log.info("userEmail info : " + emailfrom);

        return webClient.post()
                .uri("/matchedgosulist")
                .bodyValue(emailfrom)
                .retrieve()
                .bodyToMono(List.class);
    }

    //특정 고수 견적서 리스트
    @PostMapping("/matchedgosulist/{id}")
    public Mono<Object> quotationDetail(@PathVariable String id) {

        return webClient.post()
                        .uri("/matchedgosulist/" + id)
                        .retrieve()
                        .bodyToMono(Object.class);
    }
}
