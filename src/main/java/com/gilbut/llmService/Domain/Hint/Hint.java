package com.gilbut.llmService.Domain.Hint;

import com.gilbut.llmService.Domain.Location;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
 * 키워드 기반 장소 추정을 위한 힌트 엔티티
 * 키워드는 최소 2글자 이상으로
 */

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Hint {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    // 힌트값
    private String keyword;
}
