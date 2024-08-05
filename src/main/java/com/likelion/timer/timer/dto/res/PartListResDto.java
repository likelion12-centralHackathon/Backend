package com.likelion.timer.timer.dto.res;

import java.util.List;
import java.util.stream.Collectors;

import com.likelion.timer.timer.domain.entity.PartList;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PartListResDto {

	private List<Integer> partIds;

	public static PartListResDto fromEntity(PartList partList) {
		return PartListResDto.builder()
			.partIds(partList.getParts().stream()
				.map(part -> part.getPartType().getKey())
				.collect(Collectors.toList()))
			.build();
	}
}