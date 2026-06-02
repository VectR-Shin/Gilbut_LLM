package com.gilbut.llmService.DTO.RosMessageDTO.RosBridgeMessageDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RosGoalIdDTO {
    private RosTimeStampDTO stamp;
    private String id;
}
