package com.gilbut.llmService.Service;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gilbut.llmService.DTO.RosMessageDTO.RosMessageDTO;
import com.gilbut.llmService.DTO.RosMessageDTO.RosStatusType;
import com.gilbut.llmService.Properties.RosProperties;
import com.gilbut.llmService.WebSocketClient.RosClient;
import com.gilbut.llmService.WebSocketClient.RosWebSocketClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/*
 * ROS 로의 WebSocket 연결, 송신, 재연결 을 지원하는 서비스
 * 이후, 전송이 실패했을 경우의 안내 메시지를 전송하도록 구현하면 좋을 듯?
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

    // 재연결 확인 플래그
    private final AtomicBoolean reconnecting = new AtomicBoolean(false);

    // 안정화 로직 관련
    private static final int MAX_RECONNECT_ATTEMPTS = 5;
    private static final long BASE_RECONNECT_DELAY_MS = 1000L;

    private final AtomicInteger reconnectAttempts = new AtomicInteger(0);

    // tryReconnection() 의 별도 스레드 동작 지원
    private final ExecutorService reconnectExecutor =
            Executors.newSingleThreadExecutor();

    @PreDestroy
    public void shutdown() {
        reconnectExecutor.shutdown();
    }

    @PostConstruct
    public void init() {
        // WebSocket 이벤트 처리
        if (client instanceof RosWebSocketClient wsClient) {
            wsClient.setOnOpen(() -> {
                connected.set(true);
                reconnectAttempts.set(0);
                log.info("[ROS - RosService] 연결 성공: {}", rosProperties.getUri());
            });
            wsClient.setOnClose(code -> {
                connected.set(false);

                if (code == 1000) {
                    log.info("[ROS - RosService] 연결 종료");
                    return;
                }

                log.warn("[ROS - RosService] 비정상 종료 (Code: {}). 재연결 시도...", code);
                tryReconnectionAsync();
            });
            wsClient.setOnError(ex -> {
                log.error("[ROS - RosService] 연결 오류 발생", ex);
                connected.set(false);
            });
        }

        try {
            // ROS 블로킹 연결
            this.client.connect();
        } catch (Exception e) {
            log.error("[ROS - RosService] 초기 연결 실패... 재시도...", e);
            tryReconnectionAsync();
        }
    }

    public void send(RosMessageDTO dto) {
        // LocationService.getRosMessageDTO 는 LLM 응답의 type 이 CHAT, ERROR 인 경우, null 을 반환한다.
        // TTS 를 개발하기 전까지는 CHAT, ERROR 에 대한 ROS 응답을 생략한다.
        // LocationService 참조
        if (dto == null)
            return;

        if (!connected.get()) {
            log.warn("[ROS - RosService] 연결 없음. 메시지 폐기");
            return;
        }

        try {
            String rosJson = objectMapper.writeValueAsString(dto);
            client.send(rosJson);
            log.info("[ROS - RosService] 메시지 전송: {}", rosJson);
        } catch (JacksonException e) {
            log.error("[ROS - RosService] 메시지 전송 실패 >> Json 변환 실패", e);
        } catch (Exception e) {
            log.error("[ROS - RosService] 메시지 전송 실패 >> 전송 오류", e);
        }
    }

    // ROS 연결 재시도 메서드
    private void tryReconnection() {
        // 현재
        if (!reconnecting.compareAndSet(false, true)) {
            return;
        }

        int attempt = reconnectAttempts.incrementAndGet();

        if (attempt > MAX_RECONNECT_ATTEMPTS) {
            reconnecting.set(false);
            log.error("[ROS - RosService] 재연결 최대 시도 초과. 재연결 중단");
            return;
        }

        long delay = BASE_RECONNECT_DELAY_MS * (1L << (attempt - 1));
        log.warn("[ROS - RosService] ROS 재연결 {}ms 후 {}회 시도", delay, attempt);

        try {
            Thread.sleep(delay);
            client.reconnect();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error("[ROS - RosService] 연결 재시도 실패", e);
        } finally {
            reconnecting.set(false);
        }
    }

    private void tryReconnectionAsync() {
        reconnectExecutor.submit(this::tryReconnection);
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

    public void onConnectionLost(int code) {
        connected.set(false);

        if (code != 1000)
            tryReconnectionAsync();
    }
}
