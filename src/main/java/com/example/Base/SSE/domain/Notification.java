package com.example.Base.SSE.domain;


import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    private String receiver;
    private Enum notificationType; //알림 종류별 분류를 위한
    private String content; //알람의 내용
    private String url; //해당 알림 클릭시 이동할 mapping url
    private Boolean isRead; //알림 열람에 대한 여부
}
