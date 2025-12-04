package com.gilbut.llmService.DTO;

import com.gilbut.llmService.DTO.DTOStatus.LLMStatusType;
import com.gilbut.llmService.DTO.DTOStatus.ROSStatusType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

// ROS 로 메시지를 전송하기 위한 DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ROSMessageDTO {
    // ROS 응답의 타입을 지정한다.
    private ROSStatusType status;

    // status 가 ERROR 일 경우, 이하의 값은 초기화하지 않는다.
    private Double pos_x;
    private Double pos_y;
    private Double pos_z;

    private Double ori_x;
    private Double ori_y;
    private Double ori_z;
    private Double ori_w;
}
