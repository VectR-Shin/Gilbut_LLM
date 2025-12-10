package com.gilbut.llmService.DTO.LlmMessageDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class ErrorDTO implements LlmMessageDTO {
    private final LlmStatusType type = LlmStatusType.ERROR;

    private String message;
}
