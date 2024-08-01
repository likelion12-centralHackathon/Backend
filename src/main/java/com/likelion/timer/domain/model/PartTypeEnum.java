package com.likelion.timer.domain.model;

import com.likelion.timer.domain.Timer.error.TimerErrorCode;
import com.likelion.timer.global.error.exception.AppException;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PartTypeEnum {
	EYES(1, "Eyes"),
	NECK(2, "Neck"),
	WAIST(3, "Waist"),
	LEG(4, "Leg"),
	ETC(5, "Etc"),
	;

	private final Integer key;
	private final String title;

	public static PartTypeEnum findByKey(Integer key) {
		return switch (key) {
			case 1 -> EYES;
			case 2 -> NECK;
			case 3 -> WAIST;
			case 4 -> LEG;
			case 5 -> ETC;
			default -> throw new AppException(TimerErrorCode.INVALID_PART_TYPE);
		};
	}
}
