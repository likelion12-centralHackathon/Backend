package com.likelion.timer.domain.Timer.error;

import org.springframework.http.HttpStatus;

import com.likelion.timer.global.error.ErrorCode;

import lombok.Getter;

@Getter
public enum TimerErrorCode implements ErrorCode {
	RECORD_TIMER_NOT_FOUND(HttpStatus.NOT_FOUND, "시작된 타이머가 아닙니다."),
	TIMER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 타이머가 없습니다."),
	TIMER_NOT_RUN(HttpStatus.BAD_REQUEST, "타이머가 돌아가는 상태가 아닙니다."),
	INVALID_TIMER_NOT_SAVED(HttpStatus.BAD_REQUEST, "저장된 타이머가 아닙니다."),
	INVALID_TIMER_TO_CHANGE_RUN(HttpStatus.BAD_REQUEST, "타이머가 돌아가는 상태에서 또 시작할 수 없습니다."),
	INVALID_TIMER_RESTART(HttpStatus.BAD_REQUEST, "시작된 상태에서 또 재시작을 할 수는 없습니다."),
	INVALID_STRETCHING_TO_TIMER(HttpStatus.BAD_REQUEST, "스트레칭이 시작된 상태에서 스트레칭 종료 외의 다른 설정을 할 수 없습니다."),
	INVALID_TIMER_STATE(HttpStatus.BAD_REQUEST, "해당하는 타이머 상태가 없습니다."),
	INVALID_PART_STATE_TYPE(HttpStatus.BAD_REQUEST, "해당하는 스트레칭 상태가 없습니다."),
	INVALID_PART_TYPE(HttpStatus.BAD_REQUEST, "해당하는 스트레칭 부위가 없습니다."),
	INVALID_TIMER_NAME(HttpStatus.BAD_REQUEST, "타이머 이름은 중복될 수 없습니다."),
	TIMER_NAME_REQUIRED(HttpStatus.BAD_REQUEST, "타이머 이름 설정이 필요합니다."),
	INVALID_PART_LIST_BY_USER(HttpStatus.BAD_REQUEST, "직접 설정인 경우, 스트레칭 부위 리스트 정보가 필요합니다."),
	INVALID_PART_LIST_BY_SYSTEM(HttpStatus.BAD_REQUEST, "랜덤 설정인 경우, 스트레칭 부위 리스트 정보가 필요하지 않습니다."),

	;

	private final HttpStatus httpStatus;
	private final String message;

	TimerErrorCode(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}
}
