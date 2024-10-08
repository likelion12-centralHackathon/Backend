package com.likelion.timer.timer.dto.res;

import com.likelion.timer.timer.domain.entity.RecordTimer;
import com.likelion.timer.timer.model.PartTypeEnum;
import com.likelion.timer.timer.model.RecordTimerStateTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Builder
@AllArgsConstructor
public class RecordTimerResDto {
	Long timerId;
	RecordTimerStateTypeEnum recordTimerStateType;
	Float cycle;
	Long stretchingTime;
	Long developTime;
	PartTypeEnum partType;
	PartImgUrlsDto partListImageUrl;

	public static RecordTimerResDto fromEntity(RecordTimer recordTimer) {
		float cycle = recordTimer.getTimer().getCycle() / 1800.0f;

		if (recordTimer.getTimerStateType().equals(RecordTimerStateTypeEnum.TIMER_DESTROY) && !recordTimer.getTimer()
			.getIsPermanent()) {
			return RecordTimerResDto.builder()
				.recordTimerStateType(recordTimer.getTimerStateType())
				.cycle(cycle)
				.stretchingTime(recordTimer.getStretchingTime())
				.developTime(recordTimer.getDevelopTime())
				.build();
		} else if (recordTimer.getTimerStateType().equals(RecordTimerStateTypeEnum.STRETCHING_DONE) ||
			recordTimer.getTimerStateType().equals(RecordTimerStateTypeEnum.STRETCHING_START)) {
			return RecordTimerResDto.builder()
				.timerId(recordTimer.getTimer().getId())
				.recordTimerStateType(recordTimer.getTimerStateType())
				.cycle(cycle)
				.stretchingTime(recordTimer.getStretchingTime())
				.developTime(recordTimer.getDevelopTime())
				.partType(recordTimer.getPart().getPartType())
				.partListImageUrl(PartImgUrlsDto.fromEntity(recordTimer.getPart().getImgUrls()))
				.build();

		} else {
			return RecordTimerResDto.builder()
				.timerId(recordTimer.getTimer().getId())
				.recordTimerStateType(recordTimer.getTimerStateType())
				.cycle(cycle)
				.stretchingTime(recordTimer.getStretchingTime())
				.developTime(recordTimer.getDevelopTime())
				.build();
		}

	}
}
