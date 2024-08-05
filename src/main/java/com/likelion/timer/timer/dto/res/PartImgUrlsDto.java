package com.likelion.timer.timer.dto.res;

import java.util.List;
import java.util.stream.Collectors;

import com.likelion.timer.timer.domain.entity.PartImgUrl;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PartImgUrlsDto {

	private List<String> imgUrls;

	public static PartImgUrlsDto fromEntity(List<PartImgUrl> partImgUrls) {
		return PartImgUrlsDto.builder()
			.imgUrls(partImgUrls
				.stream().map(PartImgUrl::getImageUrl)
				.collect(Collectors.toList()))
			.build();
	}
}