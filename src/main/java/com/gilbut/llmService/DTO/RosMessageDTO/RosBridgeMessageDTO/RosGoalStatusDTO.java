package com.gilbut.llmService.DTO.RosMessageDTO.RosBridgeMessageDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RosGoalStatusDTO {
    @JsonProperty("goal_id")
    private RosGoalIdDTO goalId;
    private RosArrivedMessageStatus status;
    private String text;
}
