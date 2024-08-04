package com.likelion.timer.domain.Timer.dto.res;

import java.util.List;

import com.likelion.timer.domain.Timer.domain.entity.Timer;
import com.likelion.timer.domain.model.TimerStateTypeEnum;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TimerResDto {
	private Long timerId;
	private String name;
	private float cycle;
	private TimerStateTypeEnum timerState;
	private boolean isPermanent;
	private boolean isSettingByUser;
	private List<PartListResDto> parts;

	public static TimerResDto fromEntity(Timer timer) {
		float cycle = timer.getCycle() / 1800.0f;

		return TimerResDto.builder()
			.timerId(timer.getId())
			.name(timer.getName())
			.timerState(timer.getTimerState())
			.cycle(cycle)
			.isSettingByUser(timer.getIsSettingByUser())
			.isPermanent(timer.getIsPermanent())
			.isSettingByUser(timer.getIsSettingByUser())
			.parts(timer.getPartLists().stream()
				.map(PartListResDto::fromEntity)
				.toList())
			.build();
	}
}