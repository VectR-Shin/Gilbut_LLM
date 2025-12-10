package com.gilbut.llmService.Service;

import com.gilbut.llmService.DTO.LlmMessageDTO.LlmStatusType;
import com.gilbut.llmService.DTO.LlmMessageDTO.LlmMessageDTO;
import com.gilbut.llmService.DTO.RosMessageDTO.RosMessageDTO;
import com.gilbut.llmService.DTO.RosMessageDTO.RosStatusType;
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

    // LocationCode 를 이용해 DB 탐색하고, RosMessageDTO 매핑 후 반환
    // 탐색된 정보 기반으로 ROSMessageDTO 생성 및 반환
    // DB 탐색에 문제가 있을 경우, RosStatusType == ERROR
    public RosMessageDTO getROSMessageDTO(String locationCode) {

        Optional<Location> locationOptional = locationRepository.findByCode(locationCode);

        if (locationOptional.isPresent()) {
            Location location = locationOptional.get();
            return new RosMessageDTO(RosStatusType.SUCCESS,
                    location.getPos_x(), location.getPos_y(), location.getPos_z(),
                    location.getOri_x(), location.getOri_y(), location.getOri_z(), location.getOri_w());
        }

        // DB 의 위치 정보 탐색 실패
        return new RosMessageDTO(RosStatusType.ERROR, null, null, null, null, null, null, null);
    }
}
