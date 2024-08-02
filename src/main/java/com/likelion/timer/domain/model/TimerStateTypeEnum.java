package com.likelion.timer.domain.model;

import com.likelion.timer.domain.Timer.error.TimerErrorCode;
import com.likelion.timer.global.error.exception.AppException;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TimerStateTypeEnum {
	TIMER_START(0, "Timer Start"),
	TIMER_STOP(1, "Timer Stop"),
	TIMER_DESTROY(2, "Timer Destroy"),
	STRETCHING_START(3, "Stretching Start"),
	STRETCHING_DONE(4, "Stretching Done"),
	TIMER_NOT_START(5, "Timer Not Start"),
	;

	private final Integer key;
	private final String title;

	public static TimerStateTypeEnum findByKey(Integer key) {
		return switch (key) {
			case 0 -> TIMER_START;
			case 1 -> TIMER_STOP;
			case 2 -> TIMER_DESTROY;
			case 3 -> STRETCHING_START;
			case 4 -> STRETCHING_DONE;
			case 5 -> TIMER_NOT_START;
			default -> throw new AppException(TimerErrorCode.INVALID_TIMER_STATE);
		};
	}
}
