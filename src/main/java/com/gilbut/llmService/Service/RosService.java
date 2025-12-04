package com.gilbut.llmService.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gilbut.llmService.DTO.DTOStatus.ROSStatusType;
import com.gilbut.llmService.DTO.ROSMessageDTO;
import com.gilbut.llmService.Properties.RosProperties;
import com.gilbut.llmService.WebSocketClient.ROSWebSocketClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.URI;

/*
 * ROS 로의 WebSocket 연결, 송신, 재연결 을 지원하는 서비스
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class RosService {
    private final ObjectMapper objectMapper;
    private final RosProperties rosProperties;
    private ROSWebSocketClient client;
    private URI uri;

    @PostConstruct
    public void init() {
        try {
            // ROS 연결
            this.uri = new URI(rosProperties.getUri());
            this.client = new ROSWebSocketClient(uri);
            this.client.connect();

            log.info("[ROS - RosService] 연결 성공: {}", uri);
        } catch (Exception e) {
            log.error("[ROS - RosService] 연결 실패", e);
        }
    }

    public void send(ROSMessageDTO dto) {
        // LocationService.getRosMessageDTO 는 LLM 응답의 type 이 CHAT, ERROR 인 경우, null 을 반환한다.
        // TTS 를 개발하기 전까지는 CHAT, ERROR 에 대한 ROS 응답을 생략한다.
        // LocationService 참조
        if (dto == null)
            return;

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
        }
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
        ROSMessageDTO rosMessageDTO = new ROSMessageDTO();
        rosMessageDTO.setStatus(ROSStatusType.ERROR);
        this.send(rosMessageDTO);
    }
}
