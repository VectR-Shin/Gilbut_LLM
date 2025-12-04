package com.gilbut.llmService.DTO;

import com.gilbut.llmService.DTO.DTOStatus.STTStatusType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// STT 로부터 수신하는 JSON 을 담기 위한 DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
public class STTMessageDTO {
    private STTStatusType status;
    private String text;
    private long time;
}
