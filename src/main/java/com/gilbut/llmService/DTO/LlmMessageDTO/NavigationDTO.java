package com.gilbut.llmService.DTO.LlmMessageDTO;

import com.gilbut.llmService.DTO.LlmMessageDTO.Destination.DestinationDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class NavigationDTO implements LlmMessageDTO {
    private final LlmStatusType type = LlmStatusType.NAVIGATION;

    private NavigationAction action;

    private String message;

    private List<DestinationDTO> destinations;
}
