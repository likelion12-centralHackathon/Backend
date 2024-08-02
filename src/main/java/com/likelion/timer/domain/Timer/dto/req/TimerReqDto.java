package com.likelion.timer.domain.Timer.dto.req;

import static com.likelion.timer.domain.Timer.constants.TimerConstants.*;

import java.util.List;

import com.likelion.timer.global.validation.MaxListSize;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TimerReqDto {

	String name;

	@NotNull
	Boolean isPermanent;

	@NotNull
	@DecimalMin(value = "0.0", message = "값은 0.0 이상이어야 합니다.")
	@DecimalMax(value = "5.0", message = "값은 5.0 이하이어야 합니다.")
	private float cycle;

	@NotNull
	Boolean isSettingByUser;

	@MaxListSize(max = MAX_STRETCHING_TIMES)
	List<PartReqDto> parts;
}