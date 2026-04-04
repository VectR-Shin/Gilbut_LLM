package com.gilbut.llmService.Init;

import com.gilbut.llmService.Domain.Hint.Hint;
import com.gilbut.llmService.Domain.Location;
import com.gilbut.llmService.Repository.HintRepository;
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

    private final HintRepository hintRepository;

    @Override
    public void run(String... args) {
        // 장소
        Location dorm3_front = locationRepository.save(new Location(null, "dorm3_front", 12.165909767150879, 11.232402801513672, 0.0, 0.0, 0.0, 0.7071067966408575, 0.7071067657322372));
        Location ai_eng_gate1 = locationRepository.save(new Location(null, "ai_eng_gate1", -7.626241683959961, -61.740943908691406, 0.0, 0.0, 0.0, 0.6137856677692887, 0.7894727063306295));
        Location ai_eng_gate2 = locationRepository.save(new Location(null, "ai_eng_gate2", 38.47935104370117, -62.13222885131836, 0.0, 0.0, 0.0, -0.09853650003399451, 0.995133437364583));

        // 키워드
            // dorm3_front
        hintRepository.save(new Hint(null, dorm3_front, "제3기숙사"));
        hintRepository.save(new Hint(null, dorm3_front, "3기숙사"));
        hintRepository.save(new Hint(null, dorm3_front, "기숙사"));
        hintRepository.save(new Hint(null, dorm3_front, "AI공학관"));
        hintRepository.save(new Hint(null, dorm3_front, "AI관"));


            // ai_eng_gate1
        hintRepository.save(new Hint(null, ai_eng_gate1, "1번"));
        hintRepository.save(new Hint(null, ai_eng_gate1, "게이트"));
        hintRepository.save(new Hint(null, ai_eng_gate1, "출구"));
        hintRepository.save(new Hint(null, ai_eng_gate1, "입구"));
        hintRepository.save(new Hint(null, ai_eng_gate1, "AI공학관"));
        hintRepository.save(new Hint(null, ai_eng_gate1, "AI관"));


            // ai_eng_gate2
        hintRepository.save(new Hint(null, ai_eng_gate2, "2번"));
        hintRepository.save(new Hint(null, ai_eng_gate2, "게이트"));
        hintRepository.save(new Hint(null, ai_eng_gate2, "출구"));
        hintRepository.save(new Hint(null, ai_eng_gate2, "입구"));
        hintRepository.save(new Hint(null, ai_eng_gate2, "AI공학관"));
        hintRepository.save(new Hint(null, ai_eng_gate2, "AI관"));

    }

}
