package com.gilbut.llmService.Repository;

import static org.junit.jupiter.api.Assertions.*;

import com.gilbut.llmService.Domain.Location;
import com.gilbut.llmService.Log.LoggingTestExecutionOrderExtension;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@Import(LocationRepository.class)
@TestMethodOrder(MethodOrderer.Random.class)
@ActiveProfiles("test")
@ExtendWith(LoggingTestExecutionOrderExtension.class)
@Transactional
public class LocationRepositoryTest {
    @Autowired
    private LocationRepository locationRepository;

    @Test
    void save_findById_test() {
        // given
        Location newLocation = new Location(null, "NEW_LOCATION", 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        locationRepository.save(newLocation);

        // when
        Optional<Location> correct = locationRepository.findById(newLocation.getId());
        Optional<Location> wrong = locationRepository.findById(100000L);// 존재하지 않는 ID

        // then
        assertTrue(correct.isPresent());
        assertEquals("NEW_LOCATION", correct.get().getLocationCode());

        assertTrue(wrong.isEmpty());
    }

    @Test
    void findByCode_test() {
        // given
        Location newLocation = new Location(null, "NEW_LOCATION", 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        locationRepository.save(newLocation);

        // when
        Optional<Location> correct = locationRepository.findByCode(newLocation.getLocationCode());
        Optional<Location> wrong = locationRepository.findByCode("HOME");// 존재하지 않는 Location

        // then
        assertTrue(correct.isPresent());
        assertEquals(newLocation.getLocationCode(), correct.get().getLocationCode());

        assertTrue(wrong.isEmpty());
    }

    @Test
    void findAll_test() {
        // given
        Location newLocation1 = new Location(null, "NEW_LOCATION1", 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        Location newLocation2 = new Location(null, "NEW_LOCATION2", 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);

        locationRepository.save(newLocation1);
        locationRepository.save(newLocation2);

        // when
        List<Location> allLocation = locationRepository.findAll();

        // then
        assertEquals(2, allLocation.size());
    }

    @Test
    void delete_test() {
        // given
        Location newLocation1 = new Location(null, "NEW_LOCATION1", 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        Location newLocation2 = new Location(null, "NEW_LOCATION2", 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);

        locationRepository.save(newLocation1);
        locationRepository.save(newLocation2);

        // when
        locationRepository.delete(newLocation2.getId());

        Optional<Location> correct = locationRepository.findById(newLocation1.getId());
        Optional<Location> wrong = locationRepository.findById(newLocation2.getId());

        // then
        assertTrue(correct.isPresent());
        assertEquals("NEW_LOCATION1", correct.get().getLocationCode());

        assertTrue(wrong.isEmpty());
    }
}
