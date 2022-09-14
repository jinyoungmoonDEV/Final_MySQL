package com.example.Base.domain.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter@Setter
@Builder
public class ExpertOnlyDto {

    private String gosuName;
    private Integer gosuAge;
    private String gosuGender;
    private String gosuCategory;
    private String gosuAddress;
    private Integer gosuCareer;
}
