package com.example.Base.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatDTO {
    private String id;
    private String msg;
    private String user;
    private String gosu;
    private Integer room;
    private String createdAt;
}
