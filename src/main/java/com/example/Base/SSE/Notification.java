package com.example.Base.SSE;


import com.example.Base.SSE.domain.NotificationType;
import lombok.*;
import org.springframework.data.annotation.Id;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    private String receiver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType notificationType;

    private String content; //알람의 내용

    private String url;

    private Boolean isRead; //읽었는지에 대한 여부
}
