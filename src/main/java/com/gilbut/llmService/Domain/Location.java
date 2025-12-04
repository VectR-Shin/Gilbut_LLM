package com.gilbut.llmService.Domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
 * 위치 정보를 기록하는
 */

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String location;

    private double pos_x;
    private double pos_y;
    private double pos_z;

    private double ori_x;
    private double ori_y;
    private double ori_z;
    private double ori_w;
}
