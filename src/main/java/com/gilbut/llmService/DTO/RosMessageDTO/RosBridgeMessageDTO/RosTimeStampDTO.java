package com.gilbut.llmService.DTO.RosMessageDTO.RosBridgeMessageDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RosTimeStampDTO {
    private long secs;
    private long nsecs;
}
