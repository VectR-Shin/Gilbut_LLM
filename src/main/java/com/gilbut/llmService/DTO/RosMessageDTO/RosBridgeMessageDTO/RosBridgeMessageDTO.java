package com.gilbut.llmService.DTO.RosMessageDTO.RosBridgeMessageDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RosBridgeMessageDTO {
    private String op;
    private String topic;
    private RosMoveBaseStatusMessageDTO msg;
}
