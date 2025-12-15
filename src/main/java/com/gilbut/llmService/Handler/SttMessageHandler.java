package com.gilbut.llmService.Handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gilbut.llmService.DTO.LlmMessageDTO.LlmMessageDTO;
import com.gilbut.llmService.DTO.LlmMessageDTO.NavigationDescribeDTO;
import com.gilbut.llmService.DTO.LlmMessageDTO.NavigationExactDTO;
import com.gilbut.llmService.DTO.RosMessageDTO.RosMessageDTO;
import com.gilbut.llmService.DTO.SttMessageDTO.SttMessageDTO;
import com.gilbut.llmService.DTO.SttMessageDTO.SttStatusType;
import com.gilbut.llmService.Domain.Location;
import com.gilbut.llmService.Service.GeminiService;
import com.gilbut.llmService.Service.HintService;
import com.gilbut.llmService.Service.LocationService;
import com.gilbut.llmService.Service.RosService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final HintService hintService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("[HANDLER - SttMessageHandler] Whisper 연결: {}", session.getId());
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

                log.info("[HANDLER - SttMessageHandler] LLM 메시지 수신: {}", llmMessageDTO.toString());

                RosMessageDTO rosMessageDTO = null;
                String locationCode = null;

                // NAVIGATION_EXACT 처리
                if (llmMessageDTO instanceof NavigationExactDTO navExactDTO) {
                    locationCode = navExactDTO.getLocationCode();
                    rosMessageDTO = locationService.getROSMessageDTO(locationCode);
                    // TTS 처리
                }
                // NAVIGATION_DESCRIBE 처리
                else if (llmMessageDTO instanceof NavigationDescribeDTO navDescribeDTO) {
                    Optional<Location> optionalLocation = hintService.inferLocation(navDescribeDTO);
                    if (optionalLocation.isPresent()) {// 묘사의 대상을 선정했다면
                        Location location = optionalLocation.get();
                        locationCode = location.getLocationCode();
                        rosMessageDTO = locationService.getROSMessageDTO(locationCode);
                    } else {// 묘사의 대상을 제대로 선정하지 못했다면
                        // TTS 처리
                    }
                }
                // CHAT, ERROR 에 대한 처리는 아직 구현되지 않음
                else {
                    // rosMessageDTO 가 null 로 유지
                    // TTS 처리
                }

                // NAVIGATION_EXACT | NAVIGATION_DESCRIBE 요청이라면 ROS 로 메시지 송신 (RosStatusType == ERROR 도 송신)
                if (rosMessageDTO != null) {
                    if (locationCode != null) {
                        log.info("[HANDLER - SttMessageHandler] 장소 코드: {}의 정보를 ROS 로 전송합니다.", locationCode);
                    }
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