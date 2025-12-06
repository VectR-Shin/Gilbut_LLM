package com.gilbut.llmService.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gilbut.llmService.DTO.DTOStatus.RosStatusType;
import com.gilbut.llmService.DTO.RosMessageDTO;
import com.gilbut.llmService.Properties.RosProperties;
import com.gilbut.llmService.WebSocketClient.RosClient;
import com.gilbut.llmService.WebSocketClient.RosWebSocketClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicBoolean;

/*
 * ROS 로의 WebSocket 연결, 송신, 재연결 을 지원하는 서비스
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class RosService {

    private final ObjectMapper objectMapper;
    private final RosProperties rosProperties;
    private final RosClient client;

    // 비동기 연결 상태 추적
    private final AtomicBoolean connected = new AtomicBoolean(false);

    @PostConstruct
    public void init() {
        // WebSocket 이벤트 처리
        if (client instanceof RosWebSocketClient wsClient) {
            wsClient.setOnOpen(() -> connected.set(true));
            wsClient.setOnClose(() -> connected.set(false));
            wsClient.setOnError(ex -> connected.set(false));
        }

        try {
            // ROS 블로킹 연결
            this.client.connect();
            log.info("[ROS - RosService] 연결 성공: {}", rosProperties.getUri());
        } catch (Exception e) {
            log.error("[ROS - RosService] 초기 연결 실패", e);
        }
    }

    public void send(RosMessageDTO dto) {
        // LocationService.getRosMessageDTO 는 LLM 응답의 type 이 CHAT, ERROR 인 경우, null 을 반환한다.
        // TTS 를 개발하기 전까지는 CHAT, ERROR 에 대한 ROS 응답을 생략한다.
        // LocationService 참조
        if (dto == null)
            return;

        if (!connected.get()) {
            log.warn("[ROS - RosService] 연결 없음. 재시도...");
            tryReconnection();
            return;
        }

        try {
            String rosJson = objectMapper.writeValueAsString(dto);
            client.send(rosJson);
            log.info("[ROS - RosService] 메시지 전송: {}", rosJson);
        } catch (Exception e) {
            log.error("[ROS - RosService] Json 변환 실패", e);
        }

        /*
        try {
            if (client != null && client.isOpen()) {
                // JSON -> String 변경
                String rosJson = objectMapper.writeValueAsString(dto);

                // ROS 로 메시지 전송
                client.send(rosJson);
                log.info("[ROS - RosService] 메시지 전송: {}", rosJson);
            } else {
                log.warn("[ROS - RosService] 연결 없음. 재시도...");
                tryReconnection();
            }
        } catch (Exception e) {
            log.error("[ROS - RosService] JSON 변환 실패", e);
        }*/
    }

    // ROS 연결 재시도 메서드
    private void tryReconnection() {
        try {
            client.reconnect();
        } catch (Exception e) {
            log.error("[ROS - RosService] 연결 재시도 실패", e);
        }
    }

    public void sendErrorMessageToROS() {
        // 오류 메시지 송신
        RosMessageDTO rosMessageDTO = new RosMessageDTO();
        rosMessageDTO.setStatus(RosStatusType.ERROR);
        this.send(rosMessageDTO);
    }

    public boolean isConnected() {
        return connected.get();
    }

    // 테스트용 메서드
    public void setConnected(boolean connected) {
        this.connected.set(connected);
    }
}
