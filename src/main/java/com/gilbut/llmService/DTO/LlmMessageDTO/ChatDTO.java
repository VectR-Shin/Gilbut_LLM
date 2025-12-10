package com.gilbut.llmService.DTO.LlmMessageDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class ChatDTO implements LlmMessageDTO {
    private final LlmStatusType type = LlmStatusType.CHAT;

    private String message;
}
