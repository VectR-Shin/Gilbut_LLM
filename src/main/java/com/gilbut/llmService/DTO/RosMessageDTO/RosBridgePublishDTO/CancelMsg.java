package com.gilbut.llmService.DTO.RosMessageDTO.RosBridgePublishDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CancelMsg {
    private Stamp stamp;
    private String id;

    @Data
    @AllArgsConstructor
    public static class Stamp {
        private int secs;
        private int nsecs;
    }
}
