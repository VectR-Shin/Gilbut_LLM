package com.gilbut.llmService.DTO.RosMessageDTO.RosNavigationStatusDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RosNavigationStatusDTO {
    private String type;
    private RosNavigationStatus status;
}
