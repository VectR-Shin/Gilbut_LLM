package com.gilbut.llmService.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gilbut.llmService.DTO.DTOStatus.LLMStatusType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

// LLM 에게 제공받는 데이터를 매핑하는 DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LLMMessageDTO {
    // LLM 의 응답 타입을 지정한다.
    // DTO.DTOStatus.LLMStatusType 참조
    private LLMStatusType type;

    // 장소명
    private String location;

    // 안내 메시지
    private String tts_message;
}
