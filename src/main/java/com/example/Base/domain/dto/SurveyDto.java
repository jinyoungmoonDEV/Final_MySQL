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
    private Long age;
    private Long career;
    private String region;
    /*--------------------------*/
    private String GosuName;
    private String GosuAge;
    private String GosuGender;
    private String GosuCategory;
    private String GosuRegion;
}
