package com.gilbut.llmService.Init;

import com.gilbut.llmService.Domain.Location;
import com.gilbut.llmService.Repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/*
 * 서버 시작과 동시에 DB 에 값을 넣어주는 이니셜라이저
 * 여기서는 Location 을 초기화해준다.
 */

@Component
@Profile("!test")
@Transactional
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {
    private final LocationRepository locationRepository;

    @Override
    public void run(String... args) {
        locationRepository.save(new Location(null, "AI_HALL", 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
        locationRepository.save(new Location(null, "VISION_TOWER", 1.1, 1.1, 1.1, 1.1, 1.1, 1.1, 1.1));
        locationRepository.save(new Location(null, "GACHON_HALL", 2.2, 2.2, 2.2, 2.2, 2.2, 2.2, 2.2));
        locationRepository.save(new Location(null, "MAIN_LIBRARY", 3.3, 3.3, 3.3, 3.3, 3.3, 3.3, 3.3));
        locationRepository.save(new Location(null, "GLOBAL_CENTER", 4.4, 4.4, 4.4, 4.4, 4.4, 4.4, 4.4));
    }

}
