package com.gilbut.llmService.DTO.LlmMessageDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class NavigationDescribeDTO implements LlmMessageDTO {
    private final LlmStatusType type = LlmStatusType.NAVIGATION_DESCRIBE;

    private String message;

    private List<String> positiveKeywords;
    private List<String> negativeKeywords;
}
