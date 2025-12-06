package com.gilbut.llmService.Handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gilbut.llmService.DTO.DTOStatus.SttStatusType;
import com.gilbut.llmService.DTO.LlmMessageDTO;
import com.gilbut.llmService.DTO.RosMessageDTO;
import com.gilbut.llmService.DTO.SttMessageDTO;
import com.gilbut.llmService.Service.GeminiService;
import com.gilbut.llmService.Service.LocationService;
import com.gilbut.llmService.Service.RosService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Optional;

/*
 * 다음과 같은 기능을 수행하는 핸들러
 * 1. STT Server 로부터 데이터를 수신
 * 2. 수신한 텍스트를 LLM 처리
 * 3. LLM 처리한 데이터를 ROS 로 응답
 * 4. 예외 발생시 ROS 로 오류 메시지 전송
 *
 * ps. 아래 내용은 '노션' 반드시 참조
 *
 * ==========
 *
 * STT 서버로부터의 데이터 수신 관련
 * 프로토콜: WebSocket
 * 형식: JSON
 * 참조:
 *      DTO.DTOStatus.STTStatusType,
 *      DTO.STTMessageDTO
 *
 *
 * ==========
 *
 * ROS 로의 응답 관련
 * 프로토콜: WebSocket
 * 형식: JSON
 * 참조:
 *      DTO.DTOStatus.ROSStatusType,
 *      DTO.ROSMessageDTO
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class SttMessageHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper;
    private final RosService rosService;
    private final GeminiService geminiService;
    private final LocationService locationService;

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

            // STT 서버로부터 ERROR 가 온 경우
            if (msg.getStatus() == SttStatusType.ERROR) {
                log.info("[STT - SttMessageHandler] STT 서버의 ERROR 요청 수신");
                return;
            }

            String userPrompt = msg.getText();

            // LLM 처리
            Optional<LlmMessageDTO> llmMsgOptional = geminiService.ask(userPrompt);

            // geminiService.ask() 가 정상 동작했다면
            if (llmMsgOptional.isPresent()) {
                LlmMessageDTO llmMessageDTO = llmMsgOptional.get();

                // llmMessageDTO 의 정보를 기반으로 DB 검색 및 ROSMessageDTO 에 파싱
                RosMessageDTO rosMessageDTO = locationService.getROSMessageDTO(llmMessageDTO);

                // 프로토타입에서는 'NAVIGATION' 타입만 기능을 제공한다. (CHAT, ERROR 는 null 을 반환하도록 했다)
                if (rosMessageDTO != null) {
                    // ROS 로 응답
                    // DB 탐색에 이상이 있는 경우, status.ERROR
                    rosService.send(rosMessageDTO);
                }

                return;
            }

            // 오류 메시지 송신
            rosService.sendErrorMessageToROS();

        } catch (Exception e) {
            // 예외 처리
            log.error("[SST - SSTMessageHandler] STT 핸들러 오류", e);

            // 오류 메시지 송신
            rosService.sendErrorMessageToROS();
        }

    }
}