package com.likelion.timer.challenge.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChallengeCreateStep2Dto {
    private String title;
    private String content;
    private String authMethod;
    private String endDate; // "yyyy-MM-dd" 형식으로 입력받기
    private String note;
}
