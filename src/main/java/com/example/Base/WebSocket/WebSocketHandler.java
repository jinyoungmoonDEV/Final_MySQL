//package com.example.Base.WebSocket;
//
//import com.example.Base.WebSocket.domain.Message;
//import com.sun.xml.bind.Utils;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//public class WebSocketHandler extends TextWebSocketHandler {
//
//    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
//    //웹소캣 연결
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        String sessionID = session.getId();
//        sessions.put(sessionID, session); //세션 저장
//
//        Message message = Message.builder().sender(sessionID).recriver("all").build();
//        message.newConnect();
//
//        //모든 세션에 알림
//        sessions.values().forEach(s -> {
//            try {
//                if(!s.getId().equals(sessionID)) {
//                    s.sendMessage(new TextMessage(Utils.getString(message)));
//                }
//            } catch (Exception e){
//                //throw
//            }
//        });
//    }
//
//    //양방향 데이터 송신
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
//        Message message = Utils.getObject(textMessage.getPayload());
//        message.setSender(session.getId());
//
//        WebSocketSession receiver = sessions.get(message.getReciever());
//        if (receiver != null && receiver.isOpen()){
//            receiver.sendMessage(new TextMessage(Utils.getString(message)));
//        }
//    }
//
//    //소캣 연결 종료
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        String sessionID = session.getId();
//
//        sessions.remove(sessionID);
//
//        final Message message = new Message();
//        message.closeConnect();
//        message.setSender(sessionID);
//
////        sessions.values().forEach(s -> {
////            try {
////                s.sendMessage(new TextMessage(Utils.getString(message)));
////            } catch (Exception e) {
////                //throw
////            }
////        });
//    }
//
//    //소캣 통신 에러
//    @Override
//    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
//        String sessionID = session.getId();
//
//        sessions.remove(sessionID);
//    }
//}
