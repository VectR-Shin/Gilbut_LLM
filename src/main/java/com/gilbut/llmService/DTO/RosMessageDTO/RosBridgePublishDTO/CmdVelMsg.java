package com.gilbut.llmService.DTO.RosMessageDTO.RosBridgePublishDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CmdVelMsg {
    private Vector3 linear;
    private Vector3 angular;

    @Data
    @AllArgsConstructor
    public static class Vector3 {
        private double x;
        private double y;
        private double z;
    }
}
