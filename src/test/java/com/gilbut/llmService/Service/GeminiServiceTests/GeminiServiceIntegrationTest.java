package com.gilbut.llmService.Service.GeminiServiceTests;

import static org.junit.jupiter.api.Assertions.*;

import com.gilbut.llmService.DTO.DTOStatus.LlmStatusType;
import com.gilbut.llmService.DTO.LlmMessageDTO;
import com.gilbut.llmService.Service.GeminiService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

/*
 * GeminiService 의 실제 외부 LLM 기반 통합 테스트
 *
 * !!!!! 아래의 주의사항 반드시 준수 !!!!!
 * 1. 반드시 메서드 단위로 테스트하세요. (클래스 단위 X)
 * 2. 테스트를 너무 많이 하지 마세요. (실제 외부 LLM 에 요청하는 겁니다.)
 *
 */

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
public class GeminiServiceIntegrationTest {
    @Autowired
    private GeminiService geminiService;

    @Test
    void ask_realLlmServer_navigation_test() throws Exception{
        // given
        String userPrompt = "가천관은 어디로 가야해?";

        Thread.sleep(1000);

        // when
        Optional<LlmMessageDTO> result = geminiService.ask(userPrompt);

        // then
        assertTrue(result.isPresent());

        log.info("[TEST - GeminiServiceIntegrationTest] LLM 응답(DTO): {}", result.get().toString());

        assertEquals(LlmStatusType.NAVIGATION, result.get().getType());
        assertEquals("GACHON_HALL", result.get().getLocation());
    }

    @Test
    void ask_realLlmServer_chat_test() throws Exception{
        // given
        String userPrompt = "안녕? 네 이름은 뭐야?";

        Thread.sleep(1000);

        // when
        Optional<LlmMessageDTO> result = geminiService.ask(userPrompt);

        // then
        assertTrue(result.isPresent());

        log.info("[TEST - GeminiServiceIntegrationTest] LLM 응답(DTO): {}", result.get().toString());

        assertEquals(LlmStatusType.CHAT, result.get().getType());
        assertNull(result.get().getLocation());
    }

    @Test
    void ask_realLlmServer_error_test() throws Exception {
        // given
        String userPrompt = "New-York City 로 날 데려가줘...!";

        Thread.sleep(1000);

        // when
        Optional<LlmMessageDTO> result = geminiService.ask(userPrompt);

        // then
        assertTrue(result.isPresent());

        log.info("[TEST - GeminiServiceIntegrationTest] LLM 응답(DTO): {}", result.get().toString());

        assertEquals(LlmStatusType.ERROR, result.get().getType());
        assertNull(result.get().getLocation());
    }
}
