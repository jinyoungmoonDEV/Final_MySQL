package com.example.Base.WebSocket.domain;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private String sender;
    private String receiver;
    private String content;
    private String url;
    private String createdAt;
}