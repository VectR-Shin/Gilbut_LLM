package com.gilbut.llmService.Service;

import com.gilbut.llmService.DTO.LlmMessageDTO.*;
import com.gilbut.llmService.DTO.LlmMessageDTO.Destination.DestinationDTO;
import com.gilbut.llmService.DTO.RosMessageDTO.RosMessageDTO;
import com.gilbut.llmService.DTO.SttMessageDTO.SttMessageDTO;
import com.gilbut.llmService.DTO.SttMessageDTO.SttStatusType;
import com.gilbut.llmService.DTO.TtsMessageDTO.TtsMessageDTO;
import com.gilbut.llmService.DTO.TtsMessageDTO.TtsStatusType;
import com.gilbut.llmService.Service.Navigation.NavigationService;
import com.gilbut.llmService.Service.Navigation.RosService;
import com.gilbut.llmService.Service.Tts.PronunciationResolver;
import com.gilbut.llmService.Service.Tts.TtsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/*
 * 서비스들의 동작 분기 결정하는 오케스트레이션 서비스
 * 핸들러가 사용한다.
 *
 * 다음과 같은 기능을 수행
 * 1. 텍스트를 LLM 처리
 * 2. LLM 처리한 데이터를 타입에 따라 ROS, TTS 로 응답
 * 3. 예외 발생시 ROS, TTS 로그 출력
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class OrchestrationService {
    private final TtsService ttsService;
    private final GeminiService geminiService;
    private final HintService hintService;
    private final NavigationService navigationService;

    public void handle(SttMessageDTO sttMessageDTO) throws InterruptedException {
        // STT 서버로부터 ERROR 가 온 경우
        if (sttMessageDTO.getStatus() == SttStatusType.ERROR) {
            log.info("[APP - OrchestrationService] STT 서버의 ERROR 요청 수신");
            return;
        }

        String userPrompt = sttMessageDTO.getText();

        // LLM 처리
        Optional<LlmMessageDTO> llmMsgOptional = geminiService.ask(userPrompt);

        if (llmMsgOptional.isEmpty()) {
            // llm 응답에 문제가 있는 경우
            log.warn("[APP - OrchestrationService] LLM 응답 오류");
            return;
        }

        // geminiService.ask() 가 정상 동작했다면
        LlmMessageDTO llmMessageDTO = llmMsgOptional.get();

        log.info("[APP - OrchestrationService] LLM 메시지 수신: {}", llmMessageDTO.toString());

        // 최종 전송 데이터
        TtsMessageDTO ttsMessageDTO = null;
        List<RosMessageDTO> rosMessageDTOs = null;

        // LLM 응답 타입
        LlmStatusType llmResponseType = llmMessageDTO.getType();

        // NAVIGATION 처리
        if (LlmStatusType.NAVIGATION == llmResponseType) {
            NavigationDTO navigationDTO = (NavigationDTO) llmMessageDTO;
            NavigationAction action = navigationDTO.getAction();

            if (NavigationAction.NEW_ROUTE == action) {
                List<DestinationDTO> destList = navigationDTO.getDestinations();
                Optional<List<RosMessageDTO>> result = hintService.inferLocations(destList);

                if (result.isEmpty()) {
                    ttsMessageDTO = new TtsMessageDTO(TtsStatusType.SUCCESS, "말씀하신 장소의 탐색에 실패했습니다. 다시 말씀해주세요.");
                } else {
                    ttsMessageDTO = new TtsMessageDTO(TtsStatusType.SUCCESS, navigationDTO.getMessage());

                    rosMessageDTOs = result.get();
                    navigationService.navigate(rosMessageDTOs);
                }
            } else if (NavigationAction.ADD_WAYPOINT == action) {
                List<DestinationDTO> destList = navigationDTO.getDestinations();
                Optional<List<RosMessageDTO>> result = hintService.inferLocations(destList);

                if (result.isEmpty()) {
                    ttsMessageDTO = new TtsMessageDTO(TtsStatusType.SUCCESS, "말씀하신 장소의 탐색에 실패했습니다. 다시 말씀해주세요.");
                } else {
                    ttsMessageDTO = new TtsMessageDTO(TtsStatusType.SUCCESS, navigationDTO.getMessage());

                    rosMessageDTOs = result.get();
                    navigationService.addWaypoint(rosMessageDTOs);
                }
            } else {// CANCEL
                ttsMessageDTO = new TtsMessageDTO(TtsStatusType.SUCCESS, navigationDTO.getMessage());
                navigationService.cancel();
            }
        }
        // CHAT 처리
        else if (LlmStatusType.CHAT == llmResponseType) {
            // TTS 처리
            ChatDTO chatDTO = (ChatDTO) llmMessageDTO;
            ttsMessageDTO = new TtsMessageDTO(TtsStatusType.SUCCESS, chatDTO.getMessage());
        }
        // ERROR 처리
        else if (LlmStatusType.ERROR == llmResponseType){
            // TTS 처리
            ErrorDTO errorDTO = (ErrorDTO) llmMessageDTO;
            ttsMessageDTO = new TtsMessageDTO(TtsStatusType.SUCCESS, errorDTO.getMessage());
        }

        // TTS 송신
        sendTtsMessage(ttsMessageDTO);
    }

    // TTS 송신
    public void sendTtsMessage(TtsMessageDTO ttsMessageDTO) {
        if (ttsMessageDTO == null) {
            return;
        }

        log.info("[APP - OrchestrationService] TTS 메시지를 전송합니다.");
        ttsService.send(ttsMessageDTO);
    }
}
