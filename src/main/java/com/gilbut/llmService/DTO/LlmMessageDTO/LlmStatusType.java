package com.gilbut.llmService.DTO.LlmMessageDTO;

/*
 * LLM 의 응답 타입
 *
 * NAVIGATION_EXACT: 명확한 장소 안내
 * NAVIGATION_DESCRIBE: 묘사된 장소의 키워드 제공
 * CHAT: 일상적인 대화 혹은 인사
 * ERROR: 애매한 경우
 *
 */
public enum LlmStatusType {
    NAVIGATION_EXACT,
    NAVIGATION_DESCRIBE,
    CHAT,
    ERROR
}
