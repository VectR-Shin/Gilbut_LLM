package com.gilbut.llmService.Service.GeminiServiceTests;

import static org.junit.jupiter.api.Assertions.*;

import com.gilbut.llmService.DTO.LlmMessageDTO.*;
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

    /*
    @Test
    void ask_realLlmServer_navigationExact_test() throws Exception{
        // given
        String userPrompt = "에공관으로 가고 싶은데, 어디로 가야 해?";

        Thread.sleep(1000);

        // when
        Optional<LlmMessageDTO> result = geminiService.ask(userPrompt);

        // then
        assertTrue(result.isPresent());
        assertInstanceOf(NavigationExactDTO.class, result.get());
        assertEquals(LlmStatusType.NAVIGATION_EXACT, result.get().getType());

        NavigationExactDTO navigationExactDTO = (NavigationExactDTO) result.get();

        log.info("[TEST - GeminiServiceIntegrationTest] LLM 응답(DTO): {}", navigationExactDTO.toString());

        assertEquals("AI_HALL", navigationExactDTO.getLocationCode());
    }

    @Test
    void ask_realLlmServer_navigationDescribe_test() throws Exception {
        // given
        String userPrompt = "던킨도넛이 있는 건물로 가고 싶은데, 어디로 가야 해?";

        Thread.sleep(1000);

        // when
        Optional<LlmMessageDTO> result = geminiService.ask(userPrompt);

        // then
        assertTrue(result.isPresent());
        assertInstanceOf(NavigationDescribeDTO.class, result.get());
        assertEquals(LlmStatusType.NAVIGATION_DESCRIBE, result.get().getType());

        NavigationDescribeDTO navigationDescribeDTO = (NavigationDescribeDTO) result.get();

        log.info("[TEST - GeminiServiceIntegrationTest] LLM 응답(DTO): {}", navigationDescribeDTO.toString());
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
        assertInstanceOf(ChatDTO.class, result.get());
        assertEquals(LlmStatusType.CHAT, result.get().getType());

        ChatDTO chatDTO = (ChatDTO) result.get();

        log.info("[TEST - GeminiServiceIntegrationTest] LLM 응답(DTO): {}", chatDTO.toString());
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
        assertInstanceOf(ErrorDTO.class, result.get());
        assertEquals(LlmStatusType.ERROR, result.get().getType());

        ErrorDTO errorDTO = (ErrorDTO) result.get();

        log.info("[TEST - GeminiServiceIntegrationTest] LLM 응답(DTO): {}", result.get().toString());
    }*/
}