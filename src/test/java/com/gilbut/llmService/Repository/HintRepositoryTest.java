package com.gilbut.llmService.Repository;

import static org.junit.jupiter.api.Assertions.*;

import com.gilbut.llmService.Domain.Hint.Hint;
import com.gilbut.llmService.Domain.Location;
import com.gilbut.llmService.Log.LoggingTestExecutionOrderExtension;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@DataJpaTest
@Import(HintRepository.class)
@TestMethodOrder(MethodOrderer.Random.class)
@ActiveProfiles("test")
@ExtendWith(LoggingTestExecutionOrderExtension.class)
public class HintRepositoryTest {
    @Autowired
    private EntityManager em;

    @Autowired
    private HintRepository hintRepository;

    @Test
    void save_and_findById_test() {
        // given
        Location location = new Location(null, "AI_HALL", 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        em.persist(location);

        Hint hint = new Hint(null, location, "소웨");

        // when
        hint = hintRepository.save(hint);
        Hint foundHint = hintRepository.findById(hint.getId());

        // then
        assertNotNull(foundHint);
        assertEquals("소웨", foundHint.getKeyword());
        assertEquals("AI_HALL", foundHint.getLocation().getLocationCode());
    }

    @Test
    void findByLocation_test() {
        // given
        Location location = new Location(null, "VISION_TOWER", 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        em.persist(location);

        Hint hint1 = new Hint(null, location, "지하철");
        Hint hint2 = new Hint(null, location, "지하통로");

        em.persist(hint1);
        em.persist(hint2);

        // when
        List<Hint> hints = hintRepository.findByLocation(location);

        // then
        assertEquals(2, hints.size());

        List<String> expected = List.of("지하철", "지하통로");
        List<String> actual = hints.stream()
                .map(Hint::getKeyword)
                .toList();

        assertEquals(expected, actual);
    }

    @Test
    void findByKeyword_test() {
        // given
        Location location = new Location(null, "MAIN_LIBRARY", 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        em.persist(location);

        Hint hint = new Hint(null, location, "책");
        em.persist(hint);

        // when
        List<Hint> foundHint = hintRepository.findByKeyword("책");

        // then
        assertEquals(1, foundHint.size());
        assertEquals("MAIN_LIBRARY", foundHint.get(0).getLocation().getLocationCode());
    }
}
