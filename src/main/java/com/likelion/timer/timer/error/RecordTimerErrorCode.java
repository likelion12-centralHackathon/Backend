package com.likelion.timer.timer.error;

import org.springframework.http.HttpStatus;

import com.likelion.timer.global.error.ErrorCode;

import lombok.Getter;

@Getter
public enum RecordTimerErrorCode implements ErrorCode {
	INVALID_NOTI_STATE(HttpStatus.BAD_REQUEST, "해당하는 알림 종류가 없습니다."),
	INVALID_TIMER_RESTART(HttpStatus.BAD_REQUEST, "중지한 적이 없는 경우, 재시작을 할 수 없습니다."),
	INVALID_STRETCHING_DONE_STATE(HttpStatus.BAD_REQUEST, "스트레칭을 시작해야 종료할 수 있습니다."),
	INVALID_STRETCHING_DONE_PART(HttpStatus.BAD_REQUEST, "시작한 스트레칭 부위와 일치하지 않습니다."),
	NOT_FOUND_TIMER_RECORD(HttpStatus.NOT_FOUND, "Timer 이전 기록을 찾지 못했습니다."),
	;

	private final HttpStatus httpStatus;
	private final String message;

	RecordTimerErrorCode(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}
}
