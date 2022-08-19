package com.example.Base.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Getter@Setter
public class SurveyDto {
    private String name;
    private String email;
    private String category;
    private String gender;
    private String age;
    private String career;
    private String region;
}
