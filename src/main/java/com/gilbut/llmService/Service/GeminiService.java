package com.gilbut.llmService.Service;

import com.gilbut.llmService.DTO.LLMMessageDTO;
import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

/*
 * Gemini Client(GeminiConfig)를 이용하여 요청을 보내고, 응답을 받는 서비스.
 * Gemini 2.5 flash 를 이용한다.
 * Gemini 의 시스템 프롬프트는 prompt.yaml 참조
 * ask 에 제공되는 prompt 는 사용자 프롬프트이다.
 * Gemini 에게는 '시스템 프롬프트 + 사용자 프롬프트' 가 제공된다.
 * Gemini 의 응답: Json String 은 Jackson 을 이용해 LLMMessageDTO 로 파싱된다.
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class GeminiService {
    private final ObjectMapper objectMapper;
    private final Client geminiClient;

    @Value("${gemini.version}")
    private String GeminiVersion;

    // 시스템 프롬프트
    @Value("${gemini.prompt}")
    private String systemPrompt;

    // 파라미터로 들어오는 프롬프트는 '사용자 프롬프트' 이다.
    public LLMMessageDTO ask(String prompt) {
        // Gemini 의 응답을 JSON 타입으로 지정
        GenerateContentConfig config = GenerateContentConfig.builder()
                .responseMimeType("application/json")
                .build();

        // 시스템 프롬프트 + 사용자 프롬프트
        String finalPrompt = systemPrompt + "\n\n" + prompt;

        // prompt 에 대한 Gemini 의 응답 받기
        GenerateContentResponse response =
                geminiClient.models.generateContent(
                        GeminiVersion,
                        finalPrompt,
                        config
                );

        String jsonString = response.text();

        try {
            // JSON String -> DTO 매핑
            return objectMapper.readValue(jsonString, LLMMessageDTO.class);
        } catch (JacksonException e) {
            log.error("[LLM - GeminiService] JSON 파싱 실패. Gemini 응답: {}", jsonString, e);
            return null;
        }
    }
}
