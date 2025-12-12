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
        Location ai_hall = locationRepository.save(new Location(null, "AI_HALL", 29.3, -92.7, -2.4, 0.0, 0.0, 0.707, 0.707));
        Location vision_tower = locationRepository.save(new Location(null, "VISION_TOWER", -14.9, -193.3, -2.4, 0.0, 0.0, 1.0, 1.0));
        Location gachon_hall = locationRepository.save(new Location(null, "GACHON_HALL", 83.4, -83.7, -2.4, 0.0, 0.0, 0.707, 0.707));
        Location main_library = locationRepository.save(new Location(null, "MAIN_LIBRARY", 138.3, -197.6, -2.4, 0.0, 0.0, 0.0, 1.0));
        Location global_center = locationRepository.save(new Location(null, "GLOBAL_CENTER", -31.2, -117.9, -2.4, 0.0, 0.0, 0.707, 0.707));

        // 키워드
            // AI_HALL
        hintRepository.save(new Hint(null, ai_hall, "공학"));
        hintRepository.save(new Hint(null, ai_hall, "공과대학"));
        hintRepository.save(new Hint(null, ai_hall, "IT융합대학"));
        hintRepository.save(new Hint(null, ai_hall, "창업대학"));
        hintRepository.save(new Hint(null, ai_hall, "소프트웨어학과"));
        hintRepository.save(new Hint(null, ai_hall, "AI학과"));
        hintRepository.save(new Hint(null, ai_hall, "AI"));
        hintRepository.save(new Hint(null, ai_hall, "인공지능학과"));
        hintRepository.save(new Hint(null, ai_hall, "컴퓨터공학과"));
        hintRepository.save(new Hint(null, ai_hall, "스마트보안학과"));
        hintRepository.save(new Hint(null, ai_hall, "스마트시티융합학과"));
        hintRepository.save(new Hint(null, ai_hall, "에너지IT학과"));
        hintRepository.save(new Hint(null, ai_hall, "반도체"));
        hintRepository.save(new Hint(null, ai_hall, "디지털"));
        hintRepository.save(new Hint(null, ai_hall, "프로그래밍"));
        hintRepository.save(new Hint(null, ai_hall, "컴퓨터실"));
        hintRepository.save(new Hint(null, ai_hall, "가천코코네스쿨"));
        hintRepository.save(new Hint(null, ai_hall, "스타트업칼리지"));
        hintRepository.save(new Hint(null, ai_hall, "천양현 홀"));
        hintRepository.save(new Hint(null, ai_hall, "제3기숙사"));
        hintRepository.save(new Hint(null, ai_hall, "종합운동장"));
        hintRepository.save(new Hint(null, ai_hall, "큐브"));
        hintRepository.save(new Hint(null, ai_hall, "최근에 지어진"));
        hintRepository.save(new Hint(null, ai_hall, "오르막길 위쪽"));

            // VISION_TOWER
        hintRepository.save(new Hint(null, vision_tower, "지하철"));
        hintRepository.save(new Hint(null, vision_tower, "가천대역"));
        hintRepository.save(new Hint(null, vision_tower, "연결통로"));
        hintRepository.save(new Hint(null, vision_tower, "캠퍼스 입구"));
        hintRepository.save(new Hint(null, vision_tower, "접근성"));
        hintRepository.save(new Hint(null, vision_tower, "대형 건물"));
        hintRepository.save(new Hint(null, vision_tower, "현대식"));
        hintRepository.save(new Hint(null, vision_tower, "유리"));
        hintRepository.save(new Hint(null, vision_tower, "편의시설"));
        hintRepository.save(new Hint(null, vision_tower, "학생서비스센터"));
        hintRepository.save(new Hint(null, vision_tower, "취업진로처"));
        hintRepository.save(new Hint(null, vision_tower, "학생복지처"));
        hintRepository.save(new Hint(null, vision_tower, "창업지원단"));
        hintRepository.save(new Hint(null, vision_tower, "메이커스페이스센터"));
        hintRepository.save(new Hint(null, vision_tower, "실내체육관"));
        hintRepository.save(new Hint(null, vision_tower, "가천컨벤션센터"));
        hintRepository.save(new Hint(null, vision_tower, "가천브랜드스토어"));
        hintRepository.save(new Hint(null, vision_tower, "스타덤광장"));
        hintRepository.save(new Hint(null, vision_tower, "프리덤광장"));
        hintRepository.save(new Hint(null, vision_tower, "하나은행"));
        hintRepository.save(new Hint(null, vision_tower, "리플커피"));
        hintRepository.save(new Hint(null, vision_tower, "투썸플레이스"));
        hintRepository.save(new Hint(null, vision_tower, "올리브영"));
        hintRepository.save(new Hint(null, vision_tower, "포밥인뉴욕"));
        hintRepository.save(new Hint(null, vision_tower, "봉구스밥버거"));
        hintRepository.save(new Hint(null, vision_tower, "하이도조"));
        hintRepository.save(new Hint(null, vision_tower, "1209"));
        hintRepository.save(new Hint(null, vision_tower, "햄버거"));
        hintRepository.save(new Hint(null, vision_tower, "제순식당"));
        hintRepository.save(new Hint(null, vision_tower, "주차장"));
        hintRepository.save(new Hint(null, vision_tower, "던킨도넛"));
        hintRepository.save(new Hint(null, vision_tower, "스타벅스"));
        hintRepository.save(new Hint(null, vision_tower, "카페"));
        hintRepository.save(new Hint(null, vision_tower, "문구점"));
        hintRepository.save(new Hint(null, vision_tower, "복사"));
        hintRepository.save(new Hint(null, vision_tower, "서점"));
        hintRepository.save(new Hint(null, vision_tower, "의자"));
        hintRepository.save(new Hint(null, vision_tower, "정문"));
        hintRepository.save(new Hint(null, vision_tower, "교양"));
        hintRepository.save(new Hint(null, vision_tower, "전자정보도서관"));
        hintRepository.save(new Hint(null, vision_tower, "학생식당"));
        hintRepository.save(new Hint(null, vision_tower, "법과대학"));
        hintRepository.save(new Hint(null, vision_tower, "아르테크네"));


            // GACHON_HALL
        hintRepository.save(new Hint(null, gachon_hall, "행정실"));
        hintRepository.save(new Hint(null, gachon_hall, "학과 사무실"));
        hintRepository.save(new Hint(null, gachon_hall, "경영대학"));
        hintRepository.save(new Hint(null, gachon_hall, "경영대학원"));
        hintRepository.save(new Hint(null, gachon_hall, "인문대학"));
        hintRepository.save(new Hint(null, gachon_hall, "사회과학대학"));
        hintRepository.save(new Hint(null, gachon_hall, "사회정책대학원"));
        hintRepository.save(new Hint(null, gachon_hall, "교수연구실"));
        hintRepository.save(new Hint(null, gachon_hall, "중앙 건물"));
        hintRepository.save(new Hint(null, gachon_hall, "캠퍼스 중심"));
        hintRepository.save(new Hint(null, gachon_hall, "학교 중심 건물"));
        hintRepository.save(new Hint(null, gachon_hall, "대학본부"));
        hintRepository.save(new Hint(null, gachon_hall, "보건진료소"));
        hintRepository.save(new Hint(null, gachon_hall, "LAGOM SPACE 카페"));
        hintRepository.save(new Hint(null, gachon_hall, "총장실"));
        hintRepository.save(new Hint(null, gachon_hall, "헬스장"));
        hintRepository.save(new Hint(null, gachon_hall, "아르테크네"));
        hintRepository.save(new Hint(null, gachon_hall, "카페 나무"));
        hintRepository.save(new Hint(null, gachon_hall, "세븐일레븐"));
        hintRepository.save(new Hint(null, gachon_hall, "편의점"));
        hintRepository.save(new Hint(null, gachon_hall, "아르테크네"));
        hintRepository.save(new Hint(null, gachon_hall, "그라찌에"));


            // MAIN_LIBRARY
        hintRepository.save(new Hint(null, main_library, "도서관"));
        hintRepository.save(new Hint(null, main_library, "열람실"));
        hintRepository.save(new Hint(null, main_library, "자료실"));
        hintRepository.save(new Hint(null, main_library, "독서실"));
        hintRepository.save(new Hint(null, main_library, "아르테크네"));
        hintRepository.save(new Hint(null, main_library, "아르테크네"));
        hintRepository.save(new Hint(null, main_library, "책"));
        hintRepository.save(new Hint(null, main_library, "공부"));
        hintRepository.save(new Hint(null, main_library, "조용한"));
        hintRepository.save(new Hint(null, main_library, "스터디"));
        hintRepository.save(new Hint(null, main_library, "학습 공간"));
        hintRepository.save(new Hint(null, main_library, "이마트24"));
        hintRepository.save(new Hint(null, main_library, "편의점"));
        hintRepository.save(new Hint(null, main_library, "유레카"));
        hintRepository.save(new Hint(null, main_library, "언덕 위"));
        hintRepository.save(new Hint(null, main_library, "오르막길 위"));


            // GLOBAL_CENTER
        hintRepository.save(new Hint(null, global_center, "국제어학원"));
        hintRepository.save(new Hint(null, global_center, "국제교류처"));
        hintRepository.save(new Hint(null, global_center, "입학처"));
        hintRepository.save(new Hint(null, global_center, "한국어교육센터"));
        hintRepository.save(new Hint(null, global_center, "콜리지 잉글리시"));
        hintRepository.save(new Hint(null, global_center, "콜잉"));
        hintRepository.save(new Hint(null, global_center, "College English"));
        hintRepository.save(new Hint(null, global_center, "주차장"));
        hintRepository.save(new Hint(null, global_center, "농구장"));
        hintRepository.save(new Hint(null, global_center, "테니스장"));
        hintRepository.save(new Hint(null, global_center, "글로벌"));
        hintRepository.save(new Hint(null, global_center, "센터"));
        hintRepository.save(new Hint(null, global_center, "발달심리센터"));

    }

}
