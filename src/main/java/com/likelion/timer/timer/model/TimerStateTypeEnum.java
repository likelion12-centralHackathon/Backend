package com.likelion.timer.timer.model;

import com.likelion.timer.global.error.exception.AppException;
import com.likelion.timer.timer.error.TimerErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TimerStateTypeEnum {
	TIMER_NOT_RUN(0, "Timer Not Run"),
	TIMER_RUN(1, "Timer Run"),
	;

	private final Integer key;
	private final String title;

	public static TimerStateTypeEnum findByKey(Integer key) {
		return switch (key) {
			case 0 -> TIMER_NOT_RUN;
			case 1 -> TIMER_RUN;
			default -> throw new AppException(TimerErrorCode.INVALID_TIMER_STATE);
		};
	}
}
