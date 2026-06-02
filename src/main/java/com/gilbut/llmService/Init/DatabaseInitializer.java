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
        /*
            // AI 공학관 근처
        Location ai_eng_gate1 = locationRepository.save(new Location(null, "ai_eng_gate1", -7.626241683959961, -61.740943908691406, 0.0, 0.0, 0.0, 0.6137856677692887, 0.7894727063306295));
        Location ai_eng_gate2 = locationRepository.save(new Location(null, "ai_eng_gate2", 38.47935104370117, -62.13222885131836, 0.0, 0.0, 0.0, -0.09853650003399451, 0.995133437364583));
        Location ai_eng_gate3 = locationRepository.save(new Location(null, "ai_eng_gate3", -7.626241683959961, -61.740943908691406, 0.0, 0.0, 0.0, 0.6137856677692887, 0.7894727063306295));
        Location student_cafeteria = locationRepository.save(new Location(null, "student_cafeteria", 38.47935104370117, -62.13222885131836, 0.0, 0.0, 0.0, -0.09853650003399451, 0.995133437364583));
        Location smoking_area = locationRepository.save(new Location(null, "smoking_area", 12.165909767150879, 11.232402801513672, 0.0, 0.0, 0.0, 0.7071067966408575, 0.7071067657322372));
        Location convenience_store = locationRepository.save(new Location(null, "convenience_store", -7.626241683959961, -61.740943908691406, 0.0, 0.0, 0.0, 0.6137856677692887, 0.7894727063306295));
        Location dorm3_front = locationRepository.save(new Location(null, "dorm3_front", 12.165909767150879, 11.232402801513672, 0.0, 0.0, 0.0, 0.7071067966408575, 0.7071067657322372));
        */

            // 가천관 근처
        Location gachon_hall = locationRepository.save(new Location(null, "gachon_hall", -165.818, -179.357, 0.0, -0.015, 0.002, 0.662, 0.749));
        Location engineering_building_1 = locationRepository.save(new Location(null, "engineering_building_1", -19.942, -75.846, 0.0, -0.020, 0.015, 0.971, 0.238));
        Location engineering_building_2 = locationRepository.save(new Location(null, "engineering_building_2", -254.935, -88.911, 0.0, -0.014, -0.012, -0.043, 0.999));
        Location bio_nano_research_center = locationRepository.save(new Location(null, "bio_nano_research_center", -205.080, -43.506, 0.0, -0.034, 0.012, 0.706, 0.707));
        Location vision_tower = locationRepository.save(new Location(null, "vision_tower", -264.644, -0.155, 0.0, -0.017, -0.016, 0.059, 0.998));
        Location gachon_station_exit_1 = locationRepository.save(new Location(null, "gachon_station_exit_1", -254.254, 44.226, 0.0, -0.019, -0.025, -0.343, 0.939));
        Location starbucks = locationRepository.save(new Location(null, "starbucks", -224.137, 37.477, 0.0, -0.037, 0.012, 0.744, 0.667));
        Location gachon_convention_center = locationRepository.save(new Location(null, "gachon_convention_center", -165.872, 27.843, 0.0, -0.011, -0.010, -0.035, 0.999));
        Location semiconductor_college = locationRepository.save(new Location(null, "semiconductor_college", -83.307, 0.460, 0.0, 0.004, -0.020, -0.596, 0.802));
        Location electronic_information_library = locationRepository.save(new Location(null, "electronic_information_library", -146.744, -85.232, 0.0, 0.022, 0.055, 0.958, -0.282));
        Location global_center = locationRepository.save(new Location(null, "global_center", -0.519, -0.466, 0.0, -0.019, -0.018, -0.711, 0.703));



        // 키워드
        /*
        // AI 공학관 근처
            // ai_eng_gate1
        hintRepository.save(new Hint(null, ai_eng_gate1, "AI공학관"));
        hintRepository.save(new Hint(null, ai_eng_gate1, "에공관"));
        hintRepository.save(new Hint(null, ai_eng_gate1, "1번문"));
        hintRepository.save(new Hint(null, ai_eng_gate1, "1번 게이트"));

            // ai_eng_gate2
        hintRepository.save(new Hint(null, ai_eng_gate2, "AI공학관"));
        hintRepository.save(new Hint(null, ai_eng_gate2, "에공관"));
        hintRepository.save(new Hint(null, ai_eng_gate2, "2번문"));
        hintRepository.save(new Hint(null, ai_eng_gate2, "2번 게이트"));

            // ai_eng_gate3
        hintRepository.save(new Hint(null, ai_eng_gate3, "AI공학관"));
        hintRepository.save(new Hint(null, ai_eng_gate3, "에공관"));
        hintRepository.save(new Hint(null, ai_eng_gate3, "3번문"));
        hintRepository.save(new Hint(null, ai_eng_gate3, "3번 게이트"));

            // student_cafeteria
        hintRepository.save(new Hint(null, student_cafeteria, "학생식당"));
        hintRepository.save(new Hint(null, student_cafeteria, "학식"));
        hintRepository.save(new Hint(null, student_cafeteria, "식당"));

            // smoking_area
        hintRepository.save(new Hint(null, smoking_area, "흡연장"));
        hintRepository.save(new Hint(null, smoking_area, "흡구"));
        hintRepository.save(new Hint(null, smoking_area, "흡연구역"));
        hintRepository.save(new Hint(null, smoking_area, "흡연"));

            // convenience_store
        hintRepository.save(new Hint(null, convenience_store, "편의점"));
        hintRepository.save(new Hint(null, convenience_store, "이마트"));
        hintRepository.save(new Hint(null, convenience_store, "이마트24"));

            // dorm3_front
        hintRepository.save(new Hint(null, dorm3_front, "제3기숙사"));
        hintRepository.save(new Hint(null, dorm3_front, "3기숙사"));
        hintRepository.save(new Hint(null, dorm3_front, "기숙사"));
        hintRepository.save(new Hint(null, dorm3_front, "긱사"));
        hintRepository.save(new Hint(null, dorm3_front, "제3생활관"));
        hintRepository.save(new Hint(null, dorm3_front, "3생활관"));
        hintRepository.save(new Hint(null, dorm3_front, "생활관"));
        */



        // 가천관 근처
            // gachon_hall
        hintRepository.save(new Hint(null, gachon_hall, "가천관"));
        hintRepository.save(new Hint(null, gachon_hall, "무한대상"));

            // engineering_building_1
        hintRepository.save(new Hint(null, engineering_building_1, "공과대학2"));
        hintRepository.save(new Hint(null, engineering_building_1, "공대2"));
        hintRepository.save(new Hint(null, engineering_building_1, "공투"));

            // engineering_building_2
        hintRepository.save(new Hint(null, engineering_building_2, "공과대학1"));
        hintRepository.save(new Hint(null, engineering_building_2, "공대1"));
        hintRepository.save(new Hint(null, engineering_building_2, "공원"));

            // bio_nano_research_center
        hintRepository.save(new Hint(null, bio_nano_research_center, "바이오나노연구원"));
        hintRepository.save(new Hint(null, bio_nano_research_center, "바나연"));

            // vision_tower
        hintRepository.save(new Hint(null, vision_tower, "비전타워"));
        hintRepository.save(new Hint(null, vision_tower, "비전"));
        hintRepository.save(new Hint(null, vision_tower, "비타"));
        hintRepository.save(new Hint(null, vision_tower, "하나은행"));
        hintRepository.save(new Hint(null, vision_tower, "우체국"));

            // gachon_station_exit_1
        hintRepository.save(new Hint(null, gachon_station_exit_1, "가천대역"));
        hintRepository.save(new Hint(null, gachon_station_exit_1, "1번출구"));
        hintRepository.save(new Hint(null, gachon_station_exit_1, "일출"));

            // starbucks
        hintRepository.save(new Hint(null, starbucks, "스타벅스"));
        hintRepository.save(new Hint(null, starbucks, "스벅"));

            // gachon_convention_center
        hintRepository.save(new Hint(null, gachon_convention_center, "가천컨벤션센터"));
        hintRepository.save(new Hint(null, gachon_convention_center, "컨벤션센터"));
        hintRepository.save(new Hint(null, gachon_convention_center, "컨벤션"));

            // semiconductor_college
        hintRepository.save(new Hint(null, semiconductor_college, "반도체대학"));
        hintRepository.save(new Hint(null, semiconductor_college, "반단대"));
        hintRepository.save(new Hint(null, semiconductor_college, "무당이"));
        hintRepository.save(new Hint(null, semiconductor_college, "정류장"));

            // electronic_information_library
        hintRepository.save(new Hint(null, electronic_information_library, "전자정보도서관"));
        hintRepository.save(new Hint(null, electronic_information_library, "전정도"));
        hintRepository.save(new Hint(null, electronic_information_library, "도서관"));
        hintRepository.save(new Hint(null, electronic_information_library, "파스쿠찌"));

            // global_center
        hintRepository.save(new Hint(null, global_center, "글로벌센터"));
        hintRepository.save(new Hint(null, global_center, "글로벌"));
        hintRepository.save(new Hint(null, global_center, "글센"));
    }

}
