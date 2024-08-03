package com.likelion.timer.domain.model;

import com.likelion.timer.domain.Timer.error.TimerErrorCode;
import com.likelion.timer.global.error.exception.AppException;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RecordTimerStateTypeEnum {
	TIMER_START(0, "Timer Start"),
	TIMER_STOP(1, "Timer Stop"),
	TIMER_DESTROY(2, "Timer Destroy"),
	TIMER_RESTART(3, "Timer ReStart"),
	STRETCHING_START(4, "Stretching Start"),
	STRETCHING_DONE(5, "Stretching Done"),
	;

	private final Integer key;
	private final String title;

	public static RecordTimerStateTypeEnum findByKey(Integer key) {
		return switch (key) {
			case 0 -> TIMER_START;
			case 1 -> TIMER_STOP;
			case 2 -> TIMER_DESTROY;
			case 3 -> TIMER_RESTART;
			case 4 -> STRETCHING_START;
			case 5 -> STRETCHING_DONE;
			default -> throw new AppException(TimerErrorCode.INVALID_TIMER_STATE);
		};
	}
}
