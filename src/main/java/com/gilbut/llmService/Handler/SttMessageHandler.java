package com.gilbut.llmService.Handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gilbut.llmService.DTO.SttMessageDTO.SttMessageDTO;
import com.gilbut.llmService.Service.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * 다음과 같은 기능을 수행하는 핸들러
 * 1. STT Server 로부터 데이터를 수신
 * 2. 수신한 데이터를 역직렬화
 * 3. DTO 를 오케스트레이션 서비스로 넘기기
 *
 * ps. 아래 내용은 '노션' 반드시 참조
 *
 * ==========
 *
 * STT 서버로부터의 데이터 수신 관련
 * 프로토콜: WebSocket
 * 형식: JSON
 * 참조:
 *      DTO.SttMessageDTO.*
 *
 *
 * ==========
 *
 * ROS 로의 응답 관련
 * 프로토콜: WebSocket
 * 형식: JSON
 * 참조:
 *      DTO.RosMessageDTO.*
 *
 * ==========
 *
 * TTS 로의 응답 관련
 * 프로토콜: WebSocket
 * 형식: JSON
 * 참조:
 *      DTO.TtsMessageDTO.*
 *
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class SttMessageHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper;
    private final OrchestrationService orchestrationService;
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("[SttMessageHandler - afterConnectionEstablished()] Whisper 연결: {}", session.getId());
    }

    @Override
    public void handleTextMessage(@NonNull WebSocketSession session,
                                  @NonNull TextMessage message) {

        try {
            // STT 서버 메시지 파싱
            String payload = message.getPayload();
            SttMessageDTO msg = objectMapper.readValue(payload, SttMessageDTO.class);

            // 확인용 로그 출력
            log.info("==============================");
            log.info("Whisper 로부터 데이터 수신");
            log.info("status: {}", msg.getStatus());
            log.info("text: {}", msg.getText());
            log.info("time: {}", msg.getTime());
            log.info("==============================");

            executorService.submit(() -> {
                try {
                    orchestrationService.handle(msg);
                } catch (InterruptedException e) {
                    log.error("[SttMessageHandler - handleTextMessage()] 오케스트레이션 처리 실패", e);
                }
            });

        } catch (Exception e) {
            log.error("[SttMessageHandler - handleTextMessage()] STT 요청 수행 실패", e);
        }
    }
}