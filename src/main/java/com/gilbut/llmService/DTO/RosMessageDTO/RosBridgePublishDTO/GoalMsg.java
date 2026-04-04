package com.gilbut.llmService.DTO.RosMessageDTO.RosBridgePublishDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GoalMsg {
    private Header header;
    private Pose pose;

    @Data
    @AllArgsConstructor
    public static class Header {
        private String frame_id;
        private Stamp stamp;
    }

    @Data
    @AllArgsConstructor
    public static class Stamp {
        private int secs;
        private int nsecs;
    }

    @Data
    @AllArgsConstructor
    public static class Pose {
        private Position position;
        private Orientation orientation;
    }

    @Data
    @AllArgsConstructor
    public static class Position {
        private double x;
        private double y;
        private double z;
    }

    @Data
    @AllArgsConstructor
    public static class Orientation {
        private double x;
        private double y;
        private double z;
        private double w;
    }
}
