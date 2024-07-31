package com.likelion.timer.domain.model;

import com.likelion.timer.domain.Timer.error.TimerErrorCode;
import com.likelion.timer.global.error.exception.AppException;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PartTypeEnum {
	EYES(0, "Eyes"),
	NECK(1, "Neck"),
	WAIST(2, "Waist"),
	LEG(3, "Leg"),
	ETC(4, "Etc"),
	;

	private final Integer key;
	private final String title;

	public static PartTypeEnum findByKey(Integer key) {
		return switch (key) {
			case 0 -> EYES;
			case 1 -> NECK;
			case 2 -> WAIST;
			case 3 -> LEG;
			case 4 -> ETC;
			default -> throw new AppException(TimerErrorCode.INVALID_PART_TYPE);
		};
	}
}
