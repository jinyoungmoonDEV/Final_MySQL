package com.example.Base.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/category")
@Log4j2
public class CategoryController {
    WebClient webClient = WebClient.builder()
            .baseUrl("http://13.209.216.147:8010")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    @GetMapping("/categories")
    public Mono<List> showAll(){
        return webClient.get()
                .uri( "/categories")
                .retrieve()
                .bodyToMono(List.class);
    }

    @GetMapping("/{categoryId}")
    public Mono<List> showCategoryName(@PathVariable String categoryId) {
        return webClient.get()
                .uri( "/category/"+categoryId)
                .retrieve()
                .bodyToMono(List.class);

    }

    @GetMapping("/{categoryId}/survey/{indexNum}")
    public Mono<Object> showCategoryInfo(@PathVariable String categoryId, @PathVariable Integer indexNum) {
        log.info("/category/"+categoryId+"/survey/"+indexNum);
        return webClient.get()
                .uri( "/category/"+categoryId+"/survey/"+indexNum)
                .retrieve()
                .bodyToMono(Object.class);
    }
}
