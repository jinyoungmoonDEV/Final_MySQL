package com.example.Base.domain.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatDTO {
    private String id;
    private String user; //사용자
    private String gosu; //전문가
    private Integer room; //채팅방 번호
    private List<Info> info; // 내장 Document
}
