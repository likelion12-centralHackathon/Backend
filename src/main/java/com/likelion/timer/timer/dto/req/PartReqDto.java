package com.likelion.timer.timer.dto.req;

import java.util.List;

import com.likelion.timer.global.validation.MaxListSize;
import com.likelion.timer.timer.constants.TimerConstants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PartReqDto {

	@MaxListSize(max = TimerConstants.MAX_PARTS)
	List<Long> partIds;
}