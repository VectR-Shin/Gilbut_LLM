package com.gilbut.llmService.DTO.RosMessageDTO.RosBridgeMessageDTO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum RosArrivedMessageStatus {
    PENDING(0),
    ACTIVE(1),
    PREEMPTED(2),
    SUCCEEDED(3),
    ABORTED(4),
    REJECTED(5),
    PREEMPTING(6),
    RECALLING(7),
    RECALLED(8),
    LOST(9);

    private final int code;

    public static RosArrivedMessageStatus from(int code) {
        return Arrays.stream(values())
                .filter(v -> v.code == code)
                .findFirst()
                .orElse(null);
    }
}
