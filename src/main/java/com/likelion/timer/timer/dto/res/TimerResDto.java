package com.likelion.timer.timer.dto.res;

import java.util.List;

import com.likelion.timer.timer.domain.entity.Timer;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TimerResDto {
	private Long timerId;
	private String name;
	private float cycle;
	private boolean isSettingByUser;
	private List<PartListResDto> parts;

	public static TimerResDto fromEntity(Timer timer) {
		float cycle = timer.getCycle() / 1800.0f;

		return TimerResDto.builder()
			.timerId(timer.getId())
			.name(timer.getName())
			.cycle(cycle)
			.isSettingByUser(timer.getIsSettingByUser())
			.parts(timer.getPartLists().stream()
				.map(PartListResDto::fromEntity)
				.toList())
			.build();
	}
}