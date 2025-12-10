package com.gilbut.llmService.DTO.LlmMessageDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class NavigationExactDTO implements LlmMessageDTO {
    private final LlmStatusType type = LlmStatusType.NAVIGATION_EXACT;

    private String locationCode;

    private String message;
}
