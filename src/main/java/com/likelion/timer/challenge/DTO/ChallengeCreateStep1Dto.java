package com.likelion.timer.challenge.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChallengeCreateStep1Dto {
    private String imgUrl; // 선택한 이미지 URL
    private String category; // "DEVELOPMENT", "HEALTH", "FREE"
}
