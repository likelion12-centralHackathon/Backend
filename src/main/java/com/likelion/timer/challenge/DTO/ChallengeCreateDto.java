package com.likelion.timer.challenge.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChallengeCreateDto {
	private String imgUrl; // 선택한 이미지 URL
	private String category; // "DEVELOPMENT", "HEALTH", "FREE"
	private String title;
	private String content;
	private String authMethod;
	private String endDate; // "yyyy-MM-dd" 형식으로 입력받기
	private String note;
}
