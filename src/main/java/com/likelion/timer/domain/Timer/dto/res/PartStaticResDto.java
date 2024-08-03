package com.likelion.timer.domain.Timer.dto.res;

import com.likelion.timer.domain.model.PartTypeEnum;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PartStaticResDto {
	private PartTypeEnum partTypeEnum;
	private Long stretchingTime;

	public PartStaticResDto(PartTypeEnum partTypeEnum, Long stretchingTime) {
		this.partTypeEnum = partTypeEnum;
		this.stretchingTime = stretchingTime;
	}
}
