package com.likelion.timer.satisfatction;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SatisfactionResponse {
    private Long id;
    private int rating;
    private String comment;
    private PartType partType;
    private int year;
    private int month;
    private int day;
}