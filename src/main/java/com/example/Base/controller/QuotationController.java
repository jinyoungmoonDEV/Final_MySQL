package com.example.Base.controller;

import com.example.Base.SSE.NotificationService;
import com.example.Base.SSE.domain.NotificationType;
import com.example.Base.domain.dto.QuotationDto;
import com.example.Base.domain.dto.SurveyIdListDto;
import com.example.Base.domain.dto.user.UserDTO;
import com.example.Base.domain.entity.UserEntity;
import com.example.Base.service.token.TokenServiceImpl;
import com.example.Base.service.user.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.weaver.patterns.ITokenSource;
import org.hibernate.collection.internal.PersistentMap;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.w3c.dom.ls.LSException;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2 @RequiredArgsConstructor
@RestController
public class QuotationController {

    private final UserServiceImpl userService;
    private final NotificationService notificationService;
    private final TokenServiceImpl tokenService;

    WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8010")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    // 견적서 저장 내용 : 견적서(quotation)를 작성한 고수 회원 정보 및 의뢰서(survey)+ 견적가격 및 소개글
    @PostMapping("/quotationSubmit/{id}")
    public ResponseEntity savingQuotation(@PathVariable String id, @RequestBody QuotationDto quotationDto, HttpServletRequest request) {
        log.info("id from front : " + id);
        log.info("Gosun name on quotation from front : " + quotationDto.getGosuName());
        String expertEmail = tokenService.decodeJWT(request).getEmail();
        UserEntity gosuInfo = userService.getUser(expertEmail);

        String gosuName = gosuInfo.getName();
        Integer gosuAge = gosuInfo.getAge();
        String gosuGender = gosuInfo.getGender();
        String gosuCategory = gosuInfo.getCategory();
        String gosuRegion = gosuInfo.getAddress();
        Integer gosuCareer = gosuInfo.getCareer();

        quotationDto.setGosuEmail(expertEmail);
        quotationDto.setGosuName(gosuName);
        quotationDto.setGosuAge(gosuAge);
        quotationDto.setGosuGender(gosuGender);
        quotationDto.setGosuCategory(gosuCategory);
        quotationDto.setGosuRegion(gosuRegion);
        quotationDto.setGosuCareer(gosuCareer);

        Mono<QuotationDto> result = webClient.post()
                .uri("/quotationSubmit/" + id)
                .bodyValue(quotationDto)
                .retrieve()
                .bodyToMono(QuotationDto.class);
        List<QuotationDto> ls = new ArrayList();
        ls.add(result.block());
        String userEmail = ls.get(0).getUserEmail();
        String quotationId = ls.get(0).getId();
        notificationService.send(userEmail, gosuName + "님의 새로운 견적서", NotificationType.QUOTATION_INSERTED, quotationId);
        Map<String,String> map = new HashMap<>();
        map.put("status","saved");
        return ResponseEntity.ok().body(map);
    }


    @PostMapping("/matchedgosulist")
    public Mono<List> searchingQuo(HttpServletRequest request) {
        UserDTO userDTO = tokenService.decodeJWT(request);
        String emailfrom = userDTO.getEmail();
        log.info("userEmail info : " + emailfrom);

        return webClient.post()
                .uri("/matchedgosulist")
                .bodyValue(emailfrom)
                .retrieve()
                .bodyToMono(List.class);
    }

    @PostMapping("/matchedgosulist/{status}")
    public Mono getSurveysAccordingToStatus(@PathVariable Integer a, @RequestBody SurveyIdListDto surveyIdListDto) {
        log.info("status : " + a);
        log.info("survey id info : " + surveyIdListDto.getId());

        return webClient.post()
                .uri("/matchedgosulist/" + a)
                .bodyValue(surveyIdListDto)
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
