package com.likelion.timer.domain.Timer.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RecordTimerReqDto {
	@NotNull
	Integer timerStateType;

	@NotNull
	Long timerId;

	Integer partType;
}
