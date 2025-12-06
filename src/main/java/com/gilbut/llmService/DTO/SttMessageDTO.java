package com.gilbut.llmService.DTO;

import com.gilbut.llmService.DTO.DTOStatus.SttStatusType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// STT 로부터 수신하는 JSON 을 담기 위한 DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SttMessageDTO {
    private SttStatusType status;
    private String text;
    private long time;
}
