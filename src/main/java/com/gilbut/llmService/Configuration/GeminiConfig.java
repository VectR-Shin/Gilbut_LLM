package com.gilbut.llmService.Configuration;

import com.google.genai.Client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 * Gemini api 요청을 위한 Client
 * Gemini api-key 를 제공한 뒤에, 빈으로 등록
 */
@Configuration
public class GeminiConfig {
    @Value("${gemini.api-key}")
    private String apiKey;

    @Value("${gemini.prompt}")
    private String prompt;

    @Bean
    public Client geminiClient() {
        return Client.builder()
                .apiKey(apiKey)
                .build();
    }
}
