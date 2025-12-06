package com.gilbut.llmService.Service;

import com.gilbut.llmService.DTO.DTOStatus.LlmStatusType;
import com.gilbut.llmService.DTO.DTOStatus.RosStatusType;
import com.gilbut.llmService.DTO.LlmMessageDTO;
import com.gilbut.llmService.DTO.RosMessageDTO;
import com.gilbut.llmService.Domain.Location;
import com.gilbut.llmService.Repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/*
 * llmMessageDTO 를 사용해 DB(Location 테이블)에서 데이터를 얻고,
 *                              ROSMessageDTO 를 생성하는 서비스
 */

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;

    // LLMMessageDTO 를 이용해 DB 탐색
    // 탐색된 정보 기반으로 ROSMessageDTO 생성 및 반환
    // DB 탐색에 문제가 있을 경우, ROSMessageDTO.status == ERROR
    // llmMessageDTO 의 type 이 CHAT, ERROR 일 경우는 일단 보류한다. << 이후 TTS 쪽으로 개발
    public RosMessageDTO getROSMessageDTO(LlmMessageDTO llmMessageDTO) {
        LlmStatusType llmType = llmMessageDTO.getType();
        String locName = llmMessageDTO.getLocation();

        if (llmType == LlmStatusType.NAVIGATION) {
            Optional<Location> locationOptional = locationRepository.findByName(locName);

            if (locationOptional.isPresent()) {
                Location location = locationOptional.get();
                return new RosMessageDTO(RosStatusType.SUCCESS,
                        location.getPos_x(), location.getPos_y(), location.getPos_z(),
                        location.getOri_x(), location.getOri_y(), location.getOri_z(), location.getOri_w());
            } else { // 위치 정보를 DB 에서 찾지 못한 경우
                return new RosMessageDTO(RosStatusType.ERROR,
                        null, null, null, null, null, null, null);
            }

        } else {// CHAT, ERROR
            // RosService 에 추가 설명이 있으니 참조
            return null;
        }
    }
}
