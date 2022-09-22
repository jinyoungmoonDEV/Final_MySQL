package com.example.Base.domain.dto.chat;

import lombok.Data;

@Data
public class Info {
    private String msg; //메세지
    private String user; //보낸 유저
    private String gosu; //보낸 전문가
    private String createdAt; //생성 날짜
}
