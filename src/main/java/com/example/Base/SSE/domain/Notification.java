package com.example.Base.SSE.domain;


import lombok.*;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    private String receiver;

    private String notificationType;

    private String content; //알람의 내용

    private String url;

    private Boolean isRead; //읽었는지에 대한 여부
}
