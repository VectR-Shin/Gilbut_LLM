package com.gilbut.llmService.Handler.SttMessageHandlerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gilbut.llmService.DTO.LlmMessageDTO.LlmStatusType;
import com.gilbut.llmService.DTO.LlmMessageDTO.LlmMessageDTO;
import com.gilbut.llmService.DTO.RosMessageDTO.RosMessageDTO;
import com.gilbut.llmService.DTO.SttMessageDTO.SttMessageDTO;
import com.gilbut.llmService.DTO.SttMessageDTO.SttStatusType;
import com.gilbut.llmService.Log.LoggingTestExecutionOrderExtension;
import com.gilbut.llmService.Service.GeminiService;
import com.gilbut.llmService.Service.LocationService;
import com.gilbut.llmService.Service.RosService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Optional;

import static org.mockito.Mockito.*;

/*
 * SttMessageHandler 의 Mocking 기반 유닛 테스트
 */
/*
@TestMethodOrder(MethodOrderer.Random.class)
@ActiveProfiles("test")
@ExtendWith({
        LoggingTestExecutionOrderExtension.class,
        MockitoExtension.class
})
public class SttMessageHandlerTest {
    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private RosService rosService;

    @Mock
    private GeminiService geminiService;

    @Mock
    private LocationService locationService;

    @Mock
    private WebSocketSession session;

    @InjectMocks
    private SttMessageHandler sttMessageHandler;

    @Test
    void handleTextMessage_success_navigation_test() throws Exception {
        // given
        // Mock 생성 및 설정
        String userPrompt = "에공관이 어디야?";

        String sttPayload = "{\"status\":\"SUCCESS\",\"text\":\"에공관이 어디야?\",\"time\":10000000}";
        SttMessageDTO sttMessageDTO = new SttMessageDTO();
        sttMessageDTO.setStatus(SttStatusType.SUCCESS);
        sttMessageDTO.setText("에공관이 어디야?");
        sttMessageDTO.setTime(10000000L);

        when(objectMapper.readValue(sttPayload, SttMessageDTO.class)).thenReturn(sttMessageDTO);

        LlmMessageDTO llmMessageDTO = new LlmMessageDTO();
        llmMessageDTO.setType(LlmStatusType.NAVIGATION);
        llmMessageDTO.setLocation("AI_HALL");
        llmMessageDTO.setTts_message("AI 공학관으로 안내할게요.");

        when(geminiService.ask(userPrompt)).thenReturn(Optional.of(llmMessageDTO));

        RosMessageDTO rosMessageDTO = new RosMessageDTO();
        when(locationService.getROSMessageDTO(llmMessageDTO)).thenReturn(rosMessageDTO);

        // when
        sttMessageHandler.handleTextMessage(session, new TextMessage(sttPayload));

        // then
        verify(rosService, times(1)).send(rosMessageDTO);
        verify(rosService, never()).sendErrorMessageToROS();
    }

    // @Test
    void handleTextMessage_success_chatAndError_test() throws Exception {
        // 아직 tts 기능 없음
    }

    @Test
    void handleTextMessage_sttError_test() throws Exception {
        // given
        // Mock 생성 및 설정
        String userPrompt = "에공관이 어디야?";

        String sttPayload = "{\"status\":\"ERROR\",\"text\":\"에공관이 어디야?\",\"time\":10000000}";
        SttMessageDTO sttMessageDTO = new SttMessageDTO();
        sttMessageDTO.setStatus(SttStatusType.ERROR);
        sttMessageDTO.setText("에공관이 어디야?");
        sttMessageDTO.setTime(10000000L);

        when(objectMapper.readValue(sttPayload, SttMessageDTO.class)).thenReturn(sttMessageDTO);

        // when
        sttMessageHandler.handleTextMessage(session, new TextMessage(sttPayload));

        // then
        verify(rosService, never()).send(any(RosMessageDTO.class));
        verify(rosService, never()).sendErrorMessageToROS();
    }

    @Test
    void handleTextMessage_llmEmpty_test() throws Exception {
        // given
        // Mock 생성 및 설정
        String userPrompt = "안녕? 너는 이름이 뭐야?";

        String sttPayload = "{\"status\":\"SUCCESS\",\"text\":\"안녕? 너는 이름이 뭐야?\",\"time\":10000000}";
        SttMessageDTO sttMessageDTO = new SttMessageDTO();
        sttMessageDTO.setStatus(SttStatusType.SUCCESS);
        sttMessageDTO.setText("안녕? 너는 이름이 뭐야?");
        sttMessageDTO.setTime(10000000L);

        when(objectMapper.readValue(sttPayload, SttMessageDTO.class)).thenReturn(sttMessageDTO);

        when(geminiService.ask(userPrompt)).thenReturn(Optional.empty());

        // when
        sttMessageHandler.handleTextMessage(session, new TextMessage(sttPayload));

        // then
        verify(rosService, times(1)).sendErrorMessageToROS();
        verify(rosService, never()).send(any(RosMessageDTO.class));
    }

    @Test
    void handleTextMessage_exception_test() throws Exception {
        // given
        // Mock 생성 및 설정
        String userPrompt = "안녕? 너는 이름이 뭐야?";
        String sttPayload = "{\"status\":\"SUCCESS\",\"text\":\"안녕? 너는 이름이 뭐야?\",\"time\":10000000}";

        when(objectMapper.readValue(userPrompt, SttMessageDTO.class))
                .thenThrow(new RuntimeException("[MOCK] 파싱 실패"));

        // when
        sttMessageHandler.handleTextMessage(session, new TextMessage(sttPayload));

        // then
        verify(rosService, times(1)).sendErrorMessageToROS();
        verify(rosService, never()).send(any(RosMessageDTO.class));
    }
}
*/