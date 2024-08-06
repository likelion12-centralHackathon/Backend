package com.likelion.timer.timer.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TimerListResDto {
	private Long timerId;
	private String name;
}