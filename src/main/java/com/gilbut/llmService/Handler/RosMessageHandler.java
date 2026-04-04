package com.gilbut.llmService.Handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gilbut.llmService.DTO.RosMessageDTO.RosNavigationStatusDTO.RosNavigationStatusDTO;
import com.gilbut.llmService.Service.Navigation.RosService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class RosMessageHandler extends TextWebSocketHandler {
    private final RosService rosService;
    private final ObjectMapper objectMapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("[HANDLER - RosMessageHandler] ROS 연결: {}", session.getId());
    }

    @Override
    public void handleTextMessage(@NonNull WebSocketSession session,
                                  @NonNull TextMessage message) {

        try {
            // Ros 메시지 파싱
            String payload = message.getPayload();
            RosNavigationStatusDTO msg = objectMapper.readValue(payload, RosNavigationStatusDTO.class);

            // ROS 도착 완료
            rosService.onArrivedFromROS();

            // 확인용 로그 출력
            log.info("==============================");
            log.info("ROS 로부터 도착 완료 메시지 수신");
            log.info("type: {}", msg.getType());
            log.info("status: {}", msg.getStatus());
            log.info("==============================");
        } catch (Exception e) {
            log.error("[HANDLER - RosMessageHandler] RosMessageDTO 파싱 실패", e);
        }
    }
}
