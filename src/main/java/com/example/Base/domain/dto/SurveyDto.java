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
    private Integer age;
    private Integer career;  // 유저가 원하는 고수의 경력
    private String region;
    /*--------------------------*/
    private String gosuName;
    private Integer gosuAge;
    private String gosuGender;
    private String gosuCategory;
    private String gosuRegion;
    private Integer gosuCareer;  // 고수의 실제 경력
}
