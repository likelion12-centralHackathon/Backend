package com.likelion.timer.domain.Timer.dto.req;

import static com.likelion.timer.domain.Timer.constants.TimerConstants.*;

import java.util.List;

import com.likelion.timer.global.validation.MaxListSize;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PartReqDto {

	@MaxListSize(max = MAX_PARTS)
	List<Long> partIds;
}