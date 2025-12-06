package com.gilbut.llmService.Service.GeminiServiceTests;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gilbut.llmService.DTO.DTOStatus.LlmStatusType;
import com.gilbut.llmService.DTO.LlmMessageDTO;
import com.gilbut.llmService.Log.LoggingTestExecutionOrderExtension;
import com.gilbut.llmService.Service.GeminiService;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/*
 * GeminiService 의 Mocking 기반 유닛 테스트
 */

@TestMethodOrder(MethodOrderer.Random.class)
@ActiveProfiles("test")
@ExtendWith({
        LoggingTestExecutionOrderExtension.class,
        MockitoExtension.class
})
public class GeminiServiceTest {
    @Mock
    private Client geminiClient;

    @Mock
    private com.google.genai.Models models;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private GeminiService geminiService;

    @BeforeEach
    void setUp() {
        geminiService.setSystemPrompt("${gemini.prompt}");
        geminiService.setGeminiVersion("${gemini.version}");

        // models 가 final 설정으로 추정. 따라서, Reflection 으로 처리
        ReflectionTestUtils.setField(geminiClient, "models", models);
    }

    @Test
    void ask_success_test() throws Exception {
        // given
        String userPrompt = "안녕하세요";
        String jsonResponse = "{\"type\":\"CHAT\",\"location\":null,\"tts_message\":\"안녕하세요!\"}";

        // mock 생성 & 설정
        when(objectMapper.writeValueAsString(userPrompt)).thenReturn("\"안녕하세요\"");

        GenerateContentResponse mockResponse = mock(GenerateContentResponse.class);
        when(mockResponse.text()).thenReturn(jsonResponse);

        when(models.generateContent(
                anyString(),
                anyString(),
                any(GenerateContentConfig.class)
        )).thenReturn(mockResponse);

        LlmMessageDTO mockDTO = new LlmMessageDTO();
        mockDTO.setType(LlmStatusType.CHAT);
        mockDTO.setLocation(null);
        mockDTO.setTts_message("안녕하세요!");
        when(objectMapper.readValue(jsonResponse, LlmMessageDTO.class))
                .thenReturn(mockDTO);

        // when
        Optional<LlmMessageDTO> result = geminiService.ask(userPrompt);

        // then
        assertTrue(result.isPresent());
        assertEquals(LlmStatusType.CHAT, result.get().getType());
        assertNull(result.get().getLocation());
        assertEquals("안녕하세요!", result.get().getTts_message());
    }

    @Test
    void ask_jacksonFail_test() throws Exception {
        // given
        String userPrompt = "안녕하세요";

        // mock 생성 및 설정
        when(objectMapper.writeValueAsString(userPrompt)).thenReturn("\"안녕하세요\"");

        GenerateContentResponse mockResponse = mock(GenerateContentResponse.class);
        when(mockResponse.text()).thenReturn("INVALID_JSON_STRING");

        when(models.generateContent(
                anyString(),
                anyString(),
                any(GenerateContentConfig.class)
        )).thenReturn(mockResponse);

        // JsonException 발생하도록 설정
        when(objectMapper.readValue("INVALID_JSON_STRING", LlmMessageDTO.class))
                .thenThrow(new JsonProcessingException("[MOCK] 파싱 실패") {
                    @Override
                    public JsonLocation getLocation() {return null;}
                    @Override
                    public String getOriginalMessage() {return "";}
                    @Override
                    public Object getProcessor() {return null;}
                });

        // when
        Optional<LlmMessageDTO> result = geminiService.ask(userPrompt);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void ask_apiFail_test() throws Exception {
        // given
        String userPrompt = "안녕하세요";

        // mock 생성 및 설정
        when(objectMapper.writeValueAsString(userPrompt)).thenReturn("\"안녕하세요\"");

        when(models.generateContent(
                anyString(),
                anyString(),
                any(GenerateContentConfig.class)
        )).thenThrow(new RuntimeException("[MOCK] API 호출 실패"));

        // when
        Optional<LlmMessageDTO> result = geminiService.ask(userPrompt);

        // then
        assertTrue(result.isEmpty());
    }
}
