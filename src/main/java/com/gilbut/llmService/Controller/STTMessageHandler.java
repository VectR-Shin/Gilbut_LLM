package com.gilbut.llmService.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gilbut.llmService.DTO.LLMMessageDTO;
import com.gilbut.llmService.DTO.ROSMessageDTO;
import com.gilbut.llmService.DTO.STTMessageDTO;
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
public class STTMessageHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper;
    private final RosService rosService;
    private final GeminiService geminiService;
    private final LocationService locationService;

    @Value("${gemini.prompt}")
    private String prompt;

    @Override
    public void handleTextMessage(@NonNull WebSocketSession session,
                                  @NonNull TextMessage message) {
        try {
            // STT 서버 메시지 파싱
            String payload = message.getPayload();
            STTMessageDTO msg = objectMapper.readValue(payload, STTMessageDTO.class);

            // 확인용 로그 출력
            log.info("==============================");
            log.info("Whisper 로부터 데이터 수신");
            log.info("status: {}", msg.getStatus());
            log.info("text: {}", msg.getText());
            log.info("time: {}", msg.getTime());
            log.info("==============================");

            // LLM 처리
            LLMMessageDTO llmMessageDTO = geminiService.ask(prompt);

            // geminiService.ask 가 정상 동작했다면
            if (llmMessageDTO != null) {
                // llmMessageDTO 의 정보를 기반으로 DB 검색 및 ROSMessageDTO 에 파싱
                ROSMessageDTO rosMessageDTO = locationService.getROSMessageDTO(llmMessageDTO);

                // ROS 로 응답
                // DB 탐색에 이상이 있는 경우, status.ERROR
                rosService.send(rosMessageDTO);
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


/*

할 일

구현 일정
완료. 내장 DB 추가
완료. Domain 의 @Entity 추가
완료. Domain 의 Repository 추가
완료. LLMMessageDTO 및 관련된 것들 변경
완료. ROSMessageDTO 및 관련된 것들 변경 (Double 의 경우, 래퍼 클래스 쓰기)
완료. Repository 를 사용하는 Service
완료. 사전 Prompt 지정하기 (노션 참조)
8. github 올리기

테스트 일정
1. DB 에 테스트 데이터 넣기
2. Repository 테스트
3. Service 테스트
4. Handler 테스트 (통합 테스트)
5. 우분투 환경 테스트


+ 나중에 DB 에 지정된 값 미리 넣어두기

 */