package com.likelion.timer.challenge.error;

import org.springframework.http.HttpStatus;

import com.likelion.timer.global.error.ErrorCode;

import lombok.Getter;

@Getter
public enum ChallengeErrorCode implements ErrorCode {
	CHALLENGE_PARTICIPANT_NOT_EXIST(HttpStatus.NOT_FOUND, "해당 사용자가 참가신청을 하지 않은 챌린지입니다."),
	CHALLENGE_NOT_EXIST(HttpStatus.BAD_REQUEST, "존재하지 않는 챌린지입니다."),
	;

	private final HttpStatus httpStatus;
	private final String message;

	ChallengeErrorCode(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}
}
