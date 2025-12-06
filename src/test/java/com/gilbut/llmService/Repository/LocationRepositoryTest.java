package com.gilbut.llmService.Repository;

import static org.junit.jupiter.api.Assertions.*;

import com.gilbut.llmService.Domain.Location;
import com.gilbut.llmService.Log.LoggingTestExecutionOrderExtension;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@SpringBootTest
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
        assertEquals(newLocation.getLocation(), correct.get().getLocation());

        assertTrue(wrong.isEmpty());
    }

    @Test
    void findByName_test() {
        // given
        Location newLocation = new Location(null, "NEW_LOCATION", 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        locationRepository.save(newLocation);

        // when
        Optional<Location> correct = locationRepository.findByName(newLocation.getLocation());
        Optional<Location> wrong = locationRepository.findByName("HOME");// 존재하지 않는 Location

        // then
        assertTrue(correct.isPresent());
        assertEquals(newLocation.getLocation(), correct.get().getLocation());

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
        assertEquals(newLocation1.getLocation(), correct.get().getLocation());

        assertTrue(wrong.isEmpty());
    }
}
