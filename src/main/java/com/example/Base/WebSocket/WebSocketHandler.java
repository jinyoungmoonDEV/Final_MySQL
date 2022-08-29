package com.example.Base.WebSocket;

import com.example.Base.domain.dto.user.UserDTO;
import com.example.Base.service.token.TokenServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    //현재 접속한 모든 사용자, 전체 알림 을 위하여 혹은 해당 카테고리 설정한 전문가에게 전부 알림
    List<WebSocketSession> sessions = new ArrayList<>();

    // 1 ㄷ 1 알림 을 위한 Map
    private final Map<String, WebSocketSession> sessionsMap = new ConcurrentHashMap<>();

    @Override //웹소캣 연결 시
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session); //세션 저장

        String email = getEmail(session);
        sessionsMap.put(email,session); //로그인중인 개별유저
    }

    //양방향 데이터 송신
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        String msg = message.getPayload();
        if ()
        for (WebSocketSession sess: sessions){
            sess.sendMessage(new TextMessage(email + "님이 알림을 보냄"));
        }
        log.info(email);
    }

    //소캣 연결 종료
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String sessionID = session.getId();

        sessions.remove(session);
        sessionsMap.remove(sessionID);
    }

    //소캣 통신 에러
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        String sessionID = session.getId();

        sessions.remove(session);
        sessionsMap.remove(sessionID);
    }

    private String getEmail(WebSocketSession session) {
        Map<String, Object> httpSession = session.getAttributes();
        UserDTO loginUser = (UserDTO) httpSession.get("email");

        if(loginUser == null) {
            return session.getId();
        } else {
            return loginUser.getEmail();
        }
    }


    //로그인 성공시 혹은 로그인 철와동시에 session구독 처리 한다.
    // 개별 서비스 로직에서 알림이 필요한 서비스 구동시 처리 후 확인 되면 알림 메소드를 사용하여 특정 session구독자들 모아둔 곳에서 보내고자하는 session을 찾아서 그 새션에 보낸다
    //로그인 성공시 혹은 로그인 처리동시에 session구독 처리 한다.
    //현재로그인 중인 유저 전체 에게 알림 발송
    // 개별 서비스 로직에서 알림이 필요한 서비스 구동시 처리 후 확인 되면 알림 메소드를 특정 session구독자들 모아둔 곳에서 보내고자하는 session을 찾아서 그 새션에 보낸다 보낸다

    //로그인 하지 않은유저 혹은 서버 문제로 인해 알림을 전달 받지 못한 유저를 위해 프론트에서 마지막에 유저에게 서비스한 알림ID를 구독 처리 매핑에 정보를 담아서 보낸다
    //백에서는 받은 ID가 없으면 일반적인처리하고, ID가 담아져 왔을시 DB에 저장된 알림 기록을 확인해서 보내지않은 기록들을 다시보낸다(백에서는 이벤트 전송시 그정보를 DB에 저장 필요, 알림에 마지막 보낸후 보내는 알림이 필요하기에 순서를 차순으로 정렬할 기준이 필요하다 ex)createdAt, ID에 작성일자 적어서 저장)
}
