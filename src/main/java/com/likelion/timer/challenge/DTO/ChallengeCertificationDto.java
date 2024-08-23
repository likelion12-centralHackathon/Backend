package com.likelion.timer.challenge.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChallengeCertificationDto {
    private Long userId;
    private Long challengeId;
    private String photoUrl1; // 첫 번째 사진
    private String photoUrl2; // 두 번째 사진
    private String photoUrl3; // 세 번째 사진
}
