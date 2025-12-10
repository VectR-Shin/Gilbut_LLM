package com.gilbut.llmService.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.gilbut.llmService.DTO.LlmMessageDTO.LlmStatusType;
import com.gilbut.llmService.DTO.LlmMessageDTO.LlmMessageDTO;
import com.gilbut.llmService.DTO.RosMessageDTO.RosMessageDTO;
import com.gilbut.llmService.DTO.RosMessageDTO.RosStatusType;
import com.gilbut.llmService.Domain.Location;
import com.gilbut.llmService.Log.LoggingTestExecutionOrderExtension;
import com.gilbut.llmService.Repository.LocationRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/*
 * LocationService 의 Mocking 기반 유닛 테스트
 */
@TestMethodOrder(MethodOrderer.Random.class)
@ActiveProfiles("test")
@ExtendWith({LoggingTestExecutionOrderExtension.class, MockitoExtension.class})
public class LocationServiceTest {
    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private LocationService locationService;

    @Test
    void getROSMessageDTO_SUCCESS_test() {
        // given
        String code = "AI_HALL";

        Location location = new Location(1L, "AI_HALL", 1.1, 2.2, 3.3, 4.4, 5.5, 6.6, 7.7);

        when(locationRepository.findByCode(code)).thenReturn(Optional.of(location));

        // when
        RosMessageDTO result = locationService.getROSMessageDTO(code);

        // then
        assertEquals(RosStatusType.SUCCESS, result.getStatus());
        assertEquals(1.1, result.getPos_x());
        assertEquals(2.2, result.getPos_y());
        assertEquals(3.3, result.getPos_z());
    }

    @Test
    void getROSMessageDTO_ERROR_test() {
        // given
        String code = "UNKNOWN_LOCATION";

        when(locationRepository.findByCode(code)).thenReturn(Optional.empty());

        // when
        RosMessageDTO result = locationService.getROSMessageDTO(code);

        //then
        assertEquals(RosStatusType.ERROR, result.getStatus());
        assertNull(result.getPos_x());
        assertNull(result.getPos_y());
    }
}