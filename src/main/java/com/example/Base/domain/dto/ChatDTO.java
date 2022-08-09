package com.example.Base.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatDTO {
    private String id;
    private String msg;
    private String user;
    private String gosu;
    private Integer room;
    private LocalDateTime createdAt;
}
