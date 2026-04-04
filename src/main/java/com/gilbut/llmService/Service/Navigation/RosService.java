package com.gilbut.llmService.Service.Navigation;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gilbut.llmService.DTO.RosMessageDTO.RosBridgePublishDTO.CancelMsg;
import com.gilbut.llmService.DTO.RosMessageDTO.RosBridgePublishDTO.CmdVelMsg;
import com.gilbut.llmService.DTO.RosMessageDTO.RosBridgePublishDTO.GoalMsg;
import com.gilbut.llmService.DTO.RosMessageDTO.RosBridgePublishDTO.RosBridgePublishDTO;
import com.gilbut.llmService.DTO.RosMessageDTO.RosMessageDTO;
import com.gilbut.llmService.Properties.RosProperties;
import com.gilbut.llmService.WebSocketClient.Adapters.RosWebSocketClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
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
    private final RosWebSocketClient client;

    // 비동기 연결 상태 추적
    private final AtomicBoolean connected = new AtomicBoolean(false);

    // 재연결 확인 플래그
    private final AtomicBoolean reconnecting = new AtomicBoolean(false);

    // 안정화 로직 관련
    private static final long MAX_RECONNECT_DELAY_MS = 60_000L;
    private static final long BASE_RECONNECT_DELAY_MS = 1000L;

    private final AtomicInteger reconnectAttempts = new AtomicInteger(0);

    // tryReconnection() 의 별도 스레드 동작 지원
    private final ExecutorService reconnectExecutor =
            Executors.newSingleThreadExecutor();

    // ROS 도착 완료 이벤트 목록
    private final List<Runnable> arrivedListeners = new CopyOnWriteArrayList<>();

    // ROS 도착 완료 이벤트 리스너 등록/해제
    public void addArrivedListener(Runnable listener) {
        arrivedListeners.add(listener);
    }

    public void removeArrivedListener(Runnable listener) {
        arrivedListeners.remove(listener);
    }

    // ROS 도착 완료 이벤트 전파
    public void onArrivedFromROS() {
        log.info("[RosService - onArrivedFromROS] ROS 도착 완료 이벤트 수신");

        for (Runnable listener : arrivedListeners) {
            try {
                listener.run();
            } catch (Exception e) {
                log.error("[RosService - onArrivedFromROS] ROS 도착 완료 이벤트 리스터 실행 실패", e);
            }
        }
    }

    @PreDestroy
    public void shutdown() {
        reconnectExecutor.shutdown();
    }

    @PostConstruct
    public void init() {
        // WebSocket 이벤트 처리
        client.setOnOpen(() -> {
            connected.set(true);
            reconnectAttempts.set(0);
            log.info("[RosService - init] 연결 성공: {}", rosProperties.getUri());
        });
        client.setOnClose(code -> {
            connected.set(false);

            if (code == 1000) {
                log.info("[RosService - init] 연결 종료");
                return;
            }

            log.warn("[RosService - init] 비정상 종료 (Code: {}). 재연결 시도...", code);
            tryReconnectionAsync();
        });
        client.setOnError(ex -> {
            log.error("[RosService - init] 연결 오류 발생", ex);
            connected.set(false);
        });

        try {
            // ROS 블로킹 연결
            this.client.connect();
        } catch (Exception e) {
            log.error("[RosService - init] 초기 연결 실패... 재시도...", e);
            tryReconnectionAsync();
        }
    }

    private void sendInternal(RosBridgePublishDTO<?> payLoad) {
        if (payLoad == null)
            return;

        if (!connected.get()) {
            log.warn("[RosService - sendInternal] 연결 없음. 메시지 폐기");
            return;
        }

        try {
            String rosJson = objectMapper.writeValueAsString(payLoad);
            client.send(rosJson);
            log.info("[ROS - RosService] [{}] 메시지 전송: {}", payLoad.getTopic(), rosJson);
        } catch (JacksonException e) {
            log.error("[ROS - RosService] 메시지 전송 실패 >> Json 변환 실패", e);
        } catch (Exception e) {
            log.error("[ROS - RosService] 메시지 전송 실패 >> 전송 오류", e);
        }
    }

    public void sendMove(RosMessageDTO dto) {
        GoalMsg msg = new GoalMsg(
                new GoalMsg.Header("map", new GoalMsg.Stamp(0, 0)),
                new GoalMsg.Pose(
                        new GoalMsg.Position(dto.getPos_x(), dto.getPos_y(), dto.getPos_z()),
                        new GoalMsg.Orientation(dto.getOri_x(), dto.getOri_y(), dto.getOri_z(), dto.getOri_w())
                )
        );

        RosBridgePublishDTO<GoalMsg> request =
                new RosBridgePublishDTO<>("publish", "/move_base_simple/goal", msg);

        sendInternal(request);
    }

    public void sendCancel() {
        CancelMsg msg = new CancelMsg(
                new CancelMsg.Stamp(0, 0),
                ""
        );

        RosBridgePublishDTO<CancelMsg> request =
                new RosBridgePublishDTO<>("publish", "/move_base/cancel", msg);

        sendInternal(request);
    }

    public void sendStop() {
        CmdVelMsg msg = new CmdVelMsg(
                new CmdVelMsg.Vector3(0.0, 0.0, 0.0),
                new CmdVelMsg.Vector3(0.0, 0.0, 0.0)
        );

        RosBridgePublishDTO<CmdVelMsg> request =
                new RosBridgePublishDTO<>("publish", "/cmd_vel", msg);

        sendInternal(request);
    }

    // ROS 연결 재시도 메서드
    private void tryReconnection() {
        // 현재
        if (!reconnecting.compareAndSet(false, true)) {
            return;
        }

        int attempt = reconnectAttempts.incrementAndGet();

        long exp = Math.min(attempt - 1, 10);
        long delay = Math.min(
                BASE_RECONNECT_DELAY_MS * (1L << exp),
                MAX_RECONNECT_DELAY_MS
        );

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
