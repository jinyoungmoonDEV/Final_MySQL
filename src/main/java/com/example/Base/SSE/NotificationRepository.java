package com.example.Base.SSE;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

public interface NotificationRepository {
    SseEmitter save(String emitterId, SseEmitter sseEmitter); //Emitter 저장
    void saveEventCache(String eventCacheId, Object event); //이벤트 저장
    Map<String, SseEmitter> findAllEmitterStartWithByEmail(String email); //해당 회원과  관련된 모든 Emitter를 찾는다(브라우저당 여러개의 연결이 가능하므로 여러 Emiitter가 존재 가능)
    Map<String, Object> findAllEventCacheStartWithByEmail(String email); //해당 회원과관련된 모든 이벤트를 찾는다
    void deleteById(String id); //Emitter를 지운다
    void deleteAllEmitterStartWithId(String email); //해당 회원과 관련된 모든 Emitter를 지운다
    void deleteAllEventCacheStartWithId(String email); //해당 회원과 관련된 모든 이벤트를 지운다
}
