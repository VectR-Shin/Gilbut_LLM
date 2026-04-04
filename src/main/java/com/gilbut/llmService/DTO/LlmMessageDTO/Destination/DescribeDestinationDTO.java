package com.gilbut.llmService.DTO.LlmMessageDTO.Destination;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class DescribeDestinationDTO implements DestinationDTO {
    private final String subType = "DESCRIBE";

    private List<String> positiveKeywords;
    private List<String> negativeKeywords;
}
