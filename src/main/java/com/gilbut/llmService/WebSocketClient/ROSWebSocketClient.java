package com.gilbut.llmService.WebSocketClient;

import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/*
 * ROS 에 응답을 보내기 위한 '웹 소켓 클라이언트'
 * ROS 와의 통신을 위해 'Java-WebSocket' dependency 를 사용
 */

@Slf4j
public class ROSWebSocketClient extends WebSocketClient {
    public ROSWebSocketClient(URI serverUri) throws Exception {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        log.info("[ROS - ROSWebSocketClient] 연결 완료");
    }

    @Override
    public void onMessage(String message) {
        log.info("[ROS - ROSWebSocketClient] 로부터 수신한 메시지: {}", message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        log.info("[ROS - ROSWebSocketClient] 연결 종료: {}", reason);
    }

    @Override
    public void onError(Exception ex) {
        log.error("[ROS - ROSWebSocketClient] 연결 오류: {}", ex.getMessage());
        ex.printStackTrace();
    }
}
