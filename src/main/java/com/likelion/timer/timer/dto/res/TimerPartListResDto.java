package com.likelion.timer.timer.dto.res;

import java.util.List;

import com.likelion.timer.timer.domain.entity.Timer;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TimerPartListResDto {
	private String name;
	private List<PartListResDto> parts;

	public static TimerPartListResDto fromEntity(Timer timer) {
		return TimerPartListResDto.builder()
			.name(timer.getName())
			.parts(timer.getPartLists().stream()
				.map(PartListResDto::fromEntity)
				.toList())
			.build();
	}
}