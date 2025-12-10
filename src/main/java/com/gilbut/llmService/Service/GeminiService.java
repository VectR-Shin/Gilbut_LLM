package com.gilbut.llmService.Service;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gilbut.llmService.DTO.LlmMessageDTO.LlmMessageDTO;
import com.google.genai.Client;
import com.google.genai.errors.ServerException;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;


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
    private String geminiVersion;

    // 시스템 프롬프트
    @Value("${gemini.prompt}")
    private String systemPrompt;

    private static final int MAX_ATTEMPTS = 3;
    private static final long BASE_BACKOFF_MS = 1000L;

    // 파라미터로 들어오는 프롬프트는 '사용자 프롬프트' 이다.
    public Optional<LlmMessageDTO> ask(String prompt) {

        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            try {
                return Optional.ofNullable(callGemini(prompt));
            } catch (ServerException e) {
                if (isRetryable(e) && attempt < MAX_ATTEMPTS) {
                    backoff(attempt);
                    continue;
                }

                log.error("[LLM - GeminiService] Gemini 서버 예외 (Code: {})", e.code(), e);
                return Optional.empty();
            } catch (JacksonException e) {
                log.error("[LLM - GeminiService] JSON 파싱 실패. 오류 메시지: {}", e.getMessage(), e);
                return Optional.empty();
            } catch (Exception e) {
                log.error("[LLM - GeminiService] Gemini API 호출 실패", e);
                return Optional.empty();
            }
        }

        return Optional.empty();
    }

    protected LlmMessageDTO callGemini(String prompt) throws Exception {
        // ObjectMapper 를 사용해서 사용자 요청의 특수 문자 등을 안전하게 처리
        String safeUserPrompt = objectMapper.writeValueAsString(prompt);

        // 시스템 프롬프트에 사용자 요청을 삽입
        String finalPrompt = systemPrompt.replace("{userPrompt}", safeUserPrompt);

        // Gemini 의 응답을 JSON 타입으로 지정
        GenerateContentConfig config = GenerateContentConfig.builder()
                .responseMimeType("application/json")
                .build();

        // Gemini API 호출
        GenerateContentResponse response =
                geminiClient.models.generateContent(
                        geminiVersion,
                        finalPrompt,
                        config
                );

        return objectMapper.readValue(response.text(), LlmMessageDTO.class);
    }

    protected boolean isRetryable(ServerException e) {
        return e.code() == 503;
    }

    protected void backoff(int attempt) {
        long delay = BASE_BACKOFF_MS * (1L << (attempt - 1));
        log.warn("[LLM - GeminiService] Gemini 호출 오류 발생 (Code: 503)... {}ms 후 {}회 재시도...", delay, attempt);

        try {
            Thread.sleep(delay);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    // 테스트용 메서드들
    public void setGeminiVersion(String geminiVersion) {
        this.geminiVersion = geminiVersion;
    }

    public void setSystemPrompt(String systemPrompt) {
        this.systemPrompt = systemPrompt;
    }
}
