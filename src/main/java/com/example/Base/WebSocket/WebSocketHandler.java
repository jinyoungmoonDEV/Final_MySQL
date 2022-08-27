package com.example.Base.WebSocket;

import com.example.Base.domain.dto.user.UserDTO;
import com.example.Base.service.token.TokenServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final TokenServiceImpl tokenService;
    private final HttpServletRequest request;

    @Override //웹소캣 연결 시
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        UserDTO user = tokenService.decodeJWT(request);
        String email = user.getEmail();
        log.info(email);

        sessions.put(email, session); //세션 저장
        log.info(sessions);
    }

    //양방향 데이터 송신
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("session : " + session);
        log.info("content : " + message);

        TextMessage test = new TextMessage("aaa"+"aaa");
        String sender = session.getId();

        log.info(sender);
    }

    //소캣 연결 종료
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String sessionID = session.getId();

        sessions.remove(sessionID);
    }

    //소캣 통신 에러
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        String sessionID = session.getId();

        sessions.remove(sessionID);
    }

    //로그인 성공시 혹은 로그인 처리동시에 session구독 처리 한다.
    //현재로그인 중인 유저 전체 에게 알림 발송
    // 개별 서비스 로직에서 알림이 필요한 서비스 구동시 처리 후 확인 되면 알림 메소드를 특정 session구독자들 모아둔 곳에서 보내고자하는 session을 찾아서 그 새션에 보낸다 보낸다
    
    //로그인 하지 않은유저 혹은 서버 문제로 인해 알림을 전달 받지 못한 유저를 위해 프론트에서 마지막에 유저에게 서비스한 알림ID를 구독 처리 매핑에 정보를 담아서 보낸다
    //백에서는 받은 ID가 없으면 일반적인처리하고, ID가 담아져 왔을시 DB에 저장된 알림 기록을 확인해서 보내지않은 기록들을 다시보낸다(백에서는 이벤트 전송시 그정보를 DB에 저장 필요, 알림에 마지막 보낸후 보내는 알림이 필요하기에 순서를 차순으로 정렬할 기준이 필요하다 ex)createdAt, ID에 작성일자 적어서 저장)
}
