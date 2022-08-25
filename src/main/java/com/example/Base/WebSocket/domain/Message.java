package com.example.Base.WebSocket.domain;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private String name;
//    private String type;
//    private String sender;
//    private String channelID;
//    private Object data;
//
//    public void newConnect(){ this.type = "new";}
//
//    public void closeConnect(){ this.type = "close";}
}
