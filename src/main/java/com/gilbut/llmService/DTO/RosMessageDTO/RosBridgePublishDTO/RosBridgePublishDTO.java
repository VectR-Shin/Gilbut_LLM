package com.gilbut.llmService.DTO.RosMessageDTO.RosBridgePublishDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RosBridgePublishDTO<T> {
    private String op;
    private String topic;
    private T msg;
}
