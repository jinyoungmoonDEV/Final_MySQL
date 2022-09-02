package com.example.Base.SSE;

import com.example.Base.SSE.domain.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Log4j2
public class NotificationService {
    private final EmitterRepositoryImpl emitterRepository;

//    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private void sleep(int seconds, SseEmitter sseEmitter) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            sseEmitter.completeWithError(e);
        }
    }
    public SseEmitter subscribe(String email, String lastEventId) {

        log.info("subscribe started");
        String emitterId = makeTimeIncludeId(email);

        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(Long.MAX_VALUE)); //id가 key, SseEmitter가 value

        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId)); //네트워크 오류
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId)); //시간 초과
        emitter.onError((e) -> emitterRepository.deleteById(emitterId)); //오류

        // 503 에러를 방지하기 위한 더미 이벤트 전송
        String eventId = makeTimeIncludeId(email);
        sendNotification(emitter, eventId, emitterId, "EventStream Created. [userId=" + email + "]");

        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if (hasLostData(lastEventId)) {
            sendLostData(lastEventId, email, emitterId, emitter);
        }

        return emitter;
    }

    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {

        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .name("sse")
                    .data(data, MediaType.APPLICATION_JSON));
        } catch (IOException exception) {
            emitterRepository.deleteById(emitterId);
            emitter.completeWithError(exception);
        }
    }

    private String makeTimeIncludeId(String email) { return email + "_" + System.currentTimeMillis(); }//Last-Event-ID의 값을 이용하여 유실된 데이터를 찾는데 필요한 시점을 파악하기 위한 형태

    private boolean hasLostData(String lastEventId) {
        return !lastEventId.isEmpty();
    }

    private void sendLostData(String lastEventId, String email, String emitterId, SseEmitter emitter) {

        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByEmail(String.valueOf(email));
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }
//    sse연결 요청 응답
/*-----------------------------------------------------------------------------------------------------------------------------------*/
//    서버에서 클라이언트로 일방적인 데이터 보내기

    public void send(String receiver, String content, String type, String urlValue) {

        log.info("send");
        Notification notification = createNotification(receiver, content, type, urlValue);

        // 로그인 한 유저의 SseEmitter 모두 가져오기
        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllEmitterStartWithByEmail(receiver);
        log.info(sseEmitters);
        sseEmitters.forEach(
                (key, emitter) -> {
                    // 데이터 캐시 저장(유실된 데이터 처리하기 위함)
                    emitterRepository.saveEventCache(key, notification);
                    // 데이터 전송
                    sendToClient(emitter, key, notification);
                }
        );
    }

    private Notification createNotification(String receiver, String content, String type, String urlValue) {

        if (type.equals("chat")){
            return Notification.builder()
                    .receiver(receiver)
                    .content(content)
                    .url("/chat/sender/room/" + urlValue)
                    .notificationType(type)
                    .isRead(false)
                    .build();
        }

        else if (type.equals("survey")) {
            return Notification.builder()
                    .receiver(receiver)
                    .content(content)
                    .url("/quotation/" + urlValue)
                    .notificationType(type)
                    .isRead(false)
                    .build();
        }

        else if (type.equals("quotation")) {
            return Notification.builder()
                    .receiver(receiver)
                    .content(content)
                    .url("/matchedgosulist/" + urlValue)
                    .notificationType(type)
                    .isRead(false)
                    .build();
        }

        else {
            return null;
        }
    }

    private void sendToClient(SseEmitter emitter, String id, Object data) {

        try {
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name("sse")
                    .data(data, MediaType.APPLICATION_JSON)
                    .reconnectTime(0));

            emitter.complete();

            //emitterRepository.deleteById(id);

        } catch (Exception exception) {
            emitterRepository.deleteById(id);
            emitter.completeWithError(exception);
        }
    }
}
