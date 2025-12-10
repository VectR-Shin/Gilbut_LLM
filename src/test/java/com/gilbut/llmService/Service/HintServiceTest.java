package com.gilbut.llmService.Service;


import com.gilbut.llmService.DTO.LlmMessageDTO.NavigationDescribeDTO;
import com.gilbut.llmService.Domain.Hint.Hint;
import com.gilbut.llmService.Domain.Location;
import com.gilbut.llmService.Log.LoggingTestExecutionOrderExtension;
import com.gilbut.llmService.Repository.HintRepository;
import com.gilbut.llmService.Repository.LocationRepository;
import net.bytebuddy.build.ToStringPlugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/*
 * HintService 의 Mocking 기반 유닛 테스트
 */
@TestMethodOrder(MethodOrderer.Random.class)
@ActiveProfiles("test")
@ExtendWith({LoggingTestExecutionOrderExtension.class, MockitoExtension.class})
public class HintServiceTest {

    @Mock
    private HintRepository hintRepository;

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private HintService hintService;

    private Location location1;
    private Location location2;

    @BeforeEach
    void setUp() {
        location1 = new Location(1L, "VISION_TOWER", 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        location2 = new Location(2L, "MAIN_LIBRARY", 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);

        when(locationRepository.findAll()).thenReturn(Arrays.asList(location1, location2));
        when(hintRepository.findByLocation(location1))
                .thenReturn(Arrays.asList(
                        new Hint(1L, location1, "지하철"),
                        new Hint(2L, location1, "연결통로"),
                        new Hint(3L, location1, "던킨도넛"),
                        new Hint(4L, location1, "카페"),
                        new Hint(5L, location1, "스타벅스"),
                        new Hint(6L, location1, "크다")
                ));
        when(hintRepository.findByLocation(location2))
                .thenReturn(Arrays.asList(
                        new Hint(7L, location2, "책"),
                        new Hint(8L, location2, "열람실"),
                        new Hint(9L, location2, "조용한")
                ));
    }

    @Test
    void inferLocation_positiveMatch_test() {
        // given
        NavigationDescribeDTO dto = new NavigationDescribeDTO();
        dto.setPositiveKeywords(List.of("지하철 연결통로"));
        dto.setNegativeKeywords(List.of());

        // when
        Optional<Location> result = hintService.inferLocation(dto);

        // then
        assertTrue(result.isPresent());
        assertEquals(location1.getLocationCode(), result.get().getLocationCode());
    }

    @Test
    void inferLocation_noPositiveMatch_test() {
        // given
        NavigationDescribeDTO dto = new NavigationDescribeDTO();
        dto.setPositiveKeywords(List.of("엘레베이터"));
        dto.setNegativeKeywords(List.of());

        // when
        Optional<Location> result = hintService.inferLocation(dto);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void inferLocation_partialMatch_test() {
        // given
        NavigationDescribeDTO dto = new NavigationDescribeDTO();
        dto.setPositiveKeywords(List.of("지하철", "통로", "던킨"));
        dto.setNegativeKeywords(List.of());

        // when
        Optional<Location> result = hintService.inferLocation(dto);

        // then
        assertTrue(result.isPresent());
        assertEquals(location1.getLocationCode(), result.get().getLocationCode());
    }

    @Test
    void inferLocation_similarMatch_test() {
        // given
        NavigationDescribeDTO dto = new NavigationDescribeDTO();
        dto.setPositiveKeywords(List.of("지하철역"));
        dto.setNegativeKeywords(List.of());

        // when
        Optional<Location> result = hintService.inferLocation(dto);

        // then
        assertTrue(result.isPresent());
        assertEquals(location1.getLocationCode(), result.get().getLocationCode());
    }
}
