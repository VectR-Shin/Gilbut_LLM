package com.gilbut.llmService.Service;

import com.gilbut.llmService.DTO.LlmMessageDTO.NavigationDescribeDTO;
import com.gilbut.llmService.Domain.Hint.Hint;
import com.gilbut.llmService.Domain.Location;
import com.gilbut.llmService.Repository.HintRepository;
import com.gilbut.llmService.Repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HintService {

    private final HintRepository hintRepository;
    private final LocationRepository locationRepository;

    // 두 키워드 간의 유사도 계산용
    private static final LevenshteinDistance levenshteinDistance = LevenshteinDistance.getDefaultInstance();

    /*
     *  NavigationDescribeDTO 의 키워드를 기반으로 가장 적합한 Location 을 추론한다.
     *  Levenshtein distance 를 사용해 유사 키워드도 점수에 반영한다.
     *
     * 각 지역의 특징이 긍정 키워드와 매칭되면 (+)
     * 각 지역의 특징이 부정 키워드와 매칭되면 (-)
     * 점수 책정 방식은 (Exact, Partial, Similar) 순서대로 3, 2, 1 점이다.
     */
    public Optional<Location> inferLocation(NavigationDescribeDTO dto) {
        // 사용자 요청의 긍정/부정 키워드(힌트)
        List<String> positiveKeywords = dto.getPositiveKeywords();
        List<String> negativeKeywords = dto.getNegativeKeywords();

        // 모든 Location 정보
        List<Location> allLocations = locationRepository.findAll();

        // 각 Location 의 가중치 맵
        Map<Location, Integer> scoreMap = new HashMap<>();

        for (Location location : allLocations) {
            int score = 0;

            // Location 의 힌트 가져오기
            List<Hint> hints = hintRepository.findByLocation(location);

            for (Hint hint : hints) {
                String hintKeyword = hint.getKeyword();

                // Positive 키워드 스코어링
                for (String pk : positiveKeywords) {
                    if (isExactMatch(pk, hintKeyword)) score += 3;
                    else if (isPartialMatch(pk, hintKeyword)) score += 2;
                    else if (isSimilar(pk, hintKeyword)) score += 1;
                }

                // Negative 키워드 스코어링
                for (String nk : negativeKeywords) {
                    if (isExactMatch(nk, hintKeyword)) score -= 3;
                    else if (isPartialMatch(nk, hintKeyword)) score -= 2;
                    else if (isSimilar(nk, hintKeyword)) score -= 1;
                }
            }

            scoreMap.put(location, score);
        }

        // 가장 높은 점수의 location 반환
        // 동일 점수에 대해서는 알파벳 순으로 반환
        // 모든 점수가 0 이하라면, empty()
        return scoreMap.entrySet().stream()
                .filter(e -> e.getValue() > 0)
                .sorted((e1, e2) -> {
                    int scoreCompare = e2.getValue().compareTo(e1.getValue());
                    if (scoreCompare != 0) {
                        return scoreCompare;
                    } else {
                        return e1.getKey().getLocationCode().compareTo(e2.getKey().getLocationCode());
                    }
                })
                .map(Map.Entry::getKey)
                .findFirst();
    }

    // 이 아래는 매칭 알고리즘
    private boolean isExactMatch(String keyword, String hintKeyword) {
        return keyword.equalsIgnoreCase(hintKeyword);
    }

    private boolean isPartialMatch(String keyword, String hintKeyword) {
        // 띄어쓰기 단위로 분리한 뒤, 하나라도 일치한다면 true 반환
        Set<String> keywordTokens = new HashSet<>(Arrays.asList(keyword.split("\\s+")));
        Set<String> hintKeywordTokens = new HashSet<>(Arrays.asList(hintKeyword.split("\\s+")));

        keywordTokens.retainAll(hintKeywordTokens);

        return !keywordTokens.isEmpty();
    }

    private boolean isSimilar(String keyword, String hintKeyword) {
        int dist = levenshteinDistance.apply(keyword, hintKeyword);
        double similarity = (double) dist / Math.max(keyword.length(), hintKeyword.length());

        // 단어가 짧은 경우.
        if (keyword.length() < 2 || hintKeyword.length() < 2)
            return similarity < 0.5;

        return similarity < 0.3;
    }
}
