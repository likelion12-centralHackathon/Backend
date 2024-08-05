package com.likelion.timer.timer.dto.res;

import com.likelion.timer.timer.model.PartTypeEnum;

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
