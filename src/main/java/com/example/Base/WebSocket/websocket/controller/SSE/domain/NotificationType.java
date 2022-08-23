package com.example.Base.WebSocket.websocket.controller.SSE.domain;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@AllArgsConstructor
public enum NotificationType {
    CHAT_INSERTED,//채팅 알람 보내는 이벤트 타입
    SURVEY_INSERTED,//의뢰서 알람 보내는 이벤트 타입
    QUOTATION_INSERTED//견적서 알람 보내는 이벤트 타입
}
