package com.gilbut.llmService.DTO.RosMessageDTO.RosBridgeMessageDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RosMoveBaseStatusMessageDTO {
    @JsonProperty("status_list")
    private List<RosGoalStatusDTO> statusList;
}
