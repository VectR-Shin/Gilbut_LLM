package com.gilbut.llmService.Service;

import static org.junit.jupiter.api.Assertions.*;

import com.gilbut.llmService.DTO.DTOStatus.LlmStatusType;
import com.gilbut.llmService.DTO.DTOStatus.RosStatusType;
import com.gilbut.llmService.DTO.LlmMessageDTO;
import com.gilbut.llmService.DTO.RosMessageDTO;
import com.gilbut.llmService.Domain.Location;
import com.gilbut.llmService.Log.LoggingTestExecutionOrderExtension;
import com.gilbut.llmService.Repository.LocationRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

/*
 * LocationService 의 Mocking 기반 유닛 테스트
 */

@SpringBootTest
@TestMethodOrder(MethodOrderer.Random.class)
@ActiveProfiles("test")
@ExtendWith(LoggingTestExecutionOrderExtension.class)
@Transactional
public class LocationServiceTest {
    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationService locationService;

    @Test
    void getROSMessageDTO_test() {
        // given
        Location location = new Location(null, "LOCATION", 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
        locationRepository.save(location);

        LlmMessageDTO navLLM = new LlmMessageDTO(LlmStatusType.NAVIGATION, location.getLocation(), "This is 'NAVIGATION' type.");

        LlmMessageDTO chatLLM = new LlmMessageDTO(LlmStatusType.CHAT, null, "This is 'CHAT' type.");
        LlmMessageDTO errorLLM = new LlmMessageDTO(LlmStatusType.ERROR, null, "This is 'ERROr' type.");

        LlmMessageDTO wrongNavLLM = new LlmMessageDTO(LlmStatusType.NAVIGATION, "NotExistLocation", "This is 'Navigation' type. But, location not found.");

        // when
        RosMessageDTO navROS = locationService.getROSMessageDTO(navLLM);

        RosMessageDTO chatROS = locationService.getROSMessageDTO(chatLLM);
        RosMessageDTO errorROS = locationService.getROSMessageDTO(errorLLM);

        RosMessageDTO wrongNavROS = locationService.getROSMessageDTO(wrongNavLLM);

        // then
        assertNotNull(navROS);
        assertEquals(RosStatusType.SUCCESS, navROS.getStatus());
        assertEquals(location.getPos_x(), navROS.getPos_x());
        assertEquals(location.getPos_y(), navROS.getPos_y());
        assertEquals(location.getPos_z(), navROS.getPos_z());
        assertEquals(location.getOri_x(), navROS.getOri_x());
        assertEquals(location.getOri_y(), navROS.getOri_y());
        assertEquals(location.getOri_z(), navROS.getOri_z());
        assertEquals(location.getOri_w(), navROS.getOri_w());

        assertNull(chatROS);
        assertNull(errorROS);

        assertNotNull(wrongNavROS);
        assertEquals(RosStatusType.ERROR, wrongNavROS.getStatus());
        assertNull(wrongNavROS.getPos_x());
        assertNull(wrongNavROS.getPos_y());
        assertNull(wrongNavROS.getPos_z());
        assertNull(wrongNavROS.getOri_x());
        assertNull(wrongNavROS.getOri_y());
        assertNull(wrongNavROS.getOri_z());
        assertNull(wrongNavROS.getOri_w());
    }
}
