package com.likelion.timer.global.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum GlobalErrorCode implements ErrorCode {
	USER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "사용자를 찾지 못했습니다."),
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "제공되지 않는 URL을 요청하셨습니다."),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 에러입니다.")
	// 글로벌 에러 추가
	;

	private final HttpStatus httpStatus;
	private final String message;

	GlobalErrorCode(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}
}
