package com.gilbut.llmService.WebSocketClient.Adapters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gilbut.llmService.DTO.RosMessageDTO.RosBridgeMessageDTO.RosArrivedMessageStatus;
import com.gilbut.llmService.DTO.RosMessageDTO.RosBridgeMessageDTO.RosBridgeMessageDTO;
import com.gilbut.llmService.DTO.RosMessageDTO.RosBridgeMessageDTO.RosGoalStatusDTO;
import com.gilbut.llmService.Properties.RosProperties;
import com.gilbut.llmService.Service.Navigation.RosService;
import com.gilbut.llmService.WebSocketClient.BaseWebSocketClient;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class RosWebSocketClient extends BaseWebSocketClient {
    private Runnable arrivedCallback = () -> {};
    private final ObjectMapper objectMapper;

    public RosWebSocketClient(RosProperties rosProperties, ObjectMapper objectMapper) {
        super(createUri(rosProperties));
        this.objectMapper = objectMapper;
    }

    private static URI createUri(RosProperties rosProperties) {
        try {
            return new URI(rosProperties.getUri());
        } catch (Exception e) {
            throw new IllegalStateException("[RosWebSocketClient] 존재하지 않는 URI: " + rosProperties.getUri(), e);
        }
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        super.onOpen(handshakedata);
        log.info("[RosWebSocketClient] ROS websocket 연결 성공");
        subscribeMoveBaseStatus();
    }

    private void subscribeMoveBaseStatus() {
        try {
            Map<String, Object> subscribeMessage = Map.of(
                    "op", "subscribe",
                    "topic", "/move_base/status"
            );

            String json = objectMapper.writeValueAsString(subscribeMessage);

            send(json);

            log.info("==============================");
            log.info("ROS subscribe 요청 전송");
            log.info("topic: /move_base/status");
            log.info("==============================");

        } catch (Exception e) {
            log.error("[RosWebSocketClient - subscribeMoveBaseStatus()] subscribe 전송 실패", e);
        }
    }

    public void setArrivedCallback(Runnable callback) {
        this.arrivedCallback = callback;
    }

    // 일단은 status 만 확인하도록 구현
    @Override
    public void onMessage(String message) {
        try {

            RosBridgeMessageDTO dto = objectMapper.readValue(
                    message,
                    RosBridgeMessageDTO.class
            );

            if (!"publish".equals(dto.getOp()))
                return;

            if (!"/move_base/status".equals(dto.getTopic()))
                return;

            List<RosGoalStatusDTO> statusList = dto.getMsg().getStatusList();

            if (statusList == null || statusList.isEmpty())
                return;

            RosGoalStatusDTO statusDTO = statusList.get(0);

            log.info("==============================");
            log.info("ROS 도착 완료 알림 수신");
            log.info("message: {}", statusDTO.getStatus());
            log.info("text: {}", statusDTO.getText());
            log.info("==============================");

            if (statusDTO.getStatus() != RosArrivedMessageStatus.SUCCEEDED)
                return;

            // ROS 도착 완료
            arrivedCallback.run();

        } catch (Exception e) {
            log.error("[RosWebSocketClient - onMessage()] ROS 도착 완료 알림 처리 실패", e);
        }
    }
}
