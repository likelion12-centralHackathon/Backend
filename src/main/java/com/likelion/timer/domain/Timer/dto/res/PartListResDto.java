package com.likelion.timer.domain.Timer.dto.res;

import java.util.List;
import java.util.stream.Collectors;

import com.likelion.timer.domain.Timer.domain.entity.PartList;
import com.likelion.timer.domain.model.PartTypeEnum;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PartListResDto {

	private List<PartTypeEnum> parts;

	public static PartListResDto fromEntity(PartList partList) {
		return PartListResDto.builder()
			.parts(partList.getParts().stream().map(part -> part.getPartType()).collect(Collectors.toList()))
			.build();
	}
}