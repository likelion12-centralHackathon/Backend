package com.likelion.timer.satisfatction;

import com.likelion.timer.domain.model.PartTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Data;

//만족도 항목에 대한 응답 데이터
@Data
@AllArgsConstructor
public class SatisfactionResponse {
    private Long id;
    private int rating;
    private String comment;
    private PartTypeEnum partType;
    private int year;
    private int month;
    private int day;
}