package com.example.Base.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatDTO {
    private String id;
    private String msg;
    private String sender;
    private String receiver;
    private Integer roomNum;
    private LocalDateTime createdAt;
    //a
}
