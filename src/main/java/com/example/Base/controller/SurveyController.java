package com.example.Base.controller;

import com.example.Base.SSE.NotificationService;
import com.example.Base.SSE.domain.NotificationType;
import com.example.Base.domain.dto.SurveyDto;
import com.example.Base.domain.dto.SurveyIdListDto;
import com.example.Base.domain.dto.user.ExpertOnlyDto;
import com.example.Base.domain.dto.user.UserDTO;
import com.example.Base.domain.entity.UserEntity;
import com.example.Base.service.token.TokenServiceImpl;
import com.example.Base.service.user.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.LongStream;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/", produces="application/json;charset=UTF-8")
public class SurveyController {
    private final UserServiceImpl userService;
    private final TokenServiceImpl tokenService;
    private final NotificationService notificationService;
    WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8010")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();



    // survey data 저장
    @PostMapping("/category/{bigIdpParam}/survey/{SmallIdParam}/save")
    public ResponseEntity saveSurvey(@RequestBody SurveyDto surveyDto,
                                     HttpServletRequest request,
                                     @PathVariable Long bigIdpParam,
                                     @PathVariable Long SmallIdParam) {


        String emailInfoFromAC = tokenService.decodeJWT(request).getEmail();
        log.info("email info from token : " + emailInfoFromAC);
        String nameInfo = userService.getName(emailInfoFromAC);  // 뽑은 email 정보로 일반회원 조회하고 name 정보빼서 SurveyDto에 set하고 Survey 서버로 보내기
        String email = userService.getUser(emailInfoFromAC).getEmail();

        surveyDto.setName(nameInfo);
        Mono<SurveyDto> result = null;

        if (email != null) {
            surveyDto.setEmail(email);

            result = webClient.post()
                    .uri("/category/" + bigIdpParam + "/survey/" + SmallIdParam + "/save")
                    .bodyValue(surveyDto)
                    .retrieve()
                    .bodyToMono(SurveyDto.class);
        } else {
            return ResponseEntity.ok().body("email info not found from DB");
        }


        List<SurveyDto> surveyInfo = new ArrayList<>();

        surveyInfo.add(result.block());
        log.info("sureyInfo : " + surveyInfo.get(0));
        String surveyCategory = surveyInfo.get(0).getCategory();
        log.info("survey Category : " + surveyCategory);

        List allExpert = userService.getAllExpertsByCategory(surveyCategory);

        log.info("all expert email : " + allExpert);
        String userName = surveyInfo.get(0).getName();
        String surveyId = surveyInfo.get(0).getId();
        notificationService.sendList(allExpert, userName + "님의 의뢰서", NotificationType.SURVEY_INSERTED, surveyId);
        Map<String,String> map = new HashMap<>();
        map.put("저장 상태", "의뢰서 저장");
        return ResponseEntity.ok().body(map);
    }

    // 모든 survey 조회 (확인 용도로 만들어둠)
    @GetMapping("/survey/all")
    public Mono<List> showSurveys() {
        return webClient.get()
                .uri("/survey/all")
                .retrieve()
                .bodyToMono(List.class);
    }

    // 전문가에게 매칭된 의뢰서 정보들 조회 (요청 리스트)
    @PostMapping("/matchedList")
    public Mono<List> getCategory(HttpServletRequest request) {

        String emailFromToken = tokenService.decodeJWT(request).getEmail();
        UserEntity userEntity = userService.getUser(emailFromToken);

        return webClient.post()
                .uri("/matchedList")
                .bodyValue(userEntity)
                .retrieve()
                .bodyToMono(List.class);
    }

    @PostMapping(value = "/matchedList/{status}", produces="application/json;charset=UTF-8")
    public Mono getSurveysAccordingToStatus(@PathVariable Integer status, @RequestBody SurveyIdListDto surveyIdListDto) {
        log.info("status : " + status);
        log.info("survey id info : " + surveyIdListDto.getId());

        return webClient.post()
                .uri("/matchedList/" + status)
                .bodyValue(surveyIdListDto)
                .retrieve()
                .bodyToMono(List.class);
    }

    // 요청 리스트 -> 특정 요청 값 + 본인 정보까지 조회
    @PostMapping(value = "/quotation/{id}", produces="application/json;charset=UTF-8")
    public Mono<SurveyDto> getCategory(@PathVariable String id, HttpServletRequest request) {

        String emailFromToken = tokenService.decodeJWT(request).getEmail();

        ExpertOnlyDto expertOnlyDto = userService.getUser(emailFromToken).toExpertOnlyDto();  // 고수 정보 조회

        return webClient.post()
                .uri("/quotation/" + id)  // <-- 고수회원 정보 List type으로 넘겨주기
                .bodyValue(expertOnlyDto)
                .retrieve()
                .bodyToMono(SurveyDto.class);
    }
}
