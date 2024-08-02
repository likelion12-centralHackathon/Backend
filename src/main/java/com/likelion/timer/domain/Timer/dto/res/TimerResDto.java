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
	private Long userId;
	private String name;
	private float cycle;
	private TimerStateTypeEnum timerState;
	private boolean isPermanent;
	private boolean isSettingByUser;
	private List<PartListResDto> parts;

	public static TimerResDto fromEntity(Timer timer) {
		return TimerResDto.builder()
			.timerId(timer.getId())
			.userId(timer.getUser().getId())
			.name(timer.getName())
			.timerState(timer.getTimerState())
			.cycle(timer.getCycle())
			.isSettingByUser(timer.getIsSettingByUser())
			.isPermanent(timer.getIsPermanent())
			.isSettingByUser(timer.getIsSettingByUser())
			.parts(timer.getPartLists().stream()
				.map(PartListResDto::fromEntity)
				.toList())
			.build();
	}
}