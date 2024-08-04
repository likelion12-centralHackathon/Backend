package com.likelion.timer.domain.Timer.dto.res;

import com.likelion.timer.domain.model.PartTypeEnum;

import lombok.Getter;

@Getter
public class PartStaticResDto {
	private Integer partType;
	private Long stretchingTime;

	public PartStaticResDto(PartTypeEnum partTypeEnum, Long stretchingTime) {
		this.partType = partTypeEnum.getKey();
		this.stretchingTime = stretchingTime;
	}
}
