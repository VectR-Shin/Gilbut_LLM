package com.gilbut.llmService.DTO.DTOStatus;

/*
 * LLM 의 응답 타입
 *
 * NAVIGATION: 장소 안내
 * CHAT: 일상적인 대화 혹은 인사
 * ERROR: 애매한 경우
 *
 */
public enum LLMStatusType {
    NAVIGATION,
    CHAT,
    ERROR
}
