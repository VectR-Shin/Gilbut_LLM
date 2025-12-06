package com.gilbut.llmService.Config;

import com.gilbut.llmService.Handler.SttMessageHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/*
 * WebSocket 에서 들어오는 메시지 처리
 * STTMessageHandler 를 의존성 주입(ID)
 * '/whisper' 경로로 들어오는 WebSocket 요청을 handler 가 처리
 *  CORS 정책에 대해 모든 출처를 허용한다.
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final SttMessageHandler handler;

    public WebSocketConfig(SttMessageHandler handler) {
        this.handler = handler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler((WebSocketHandler) handler, "/whisper")
                .setAllowedOrigins("*");
    }
}
