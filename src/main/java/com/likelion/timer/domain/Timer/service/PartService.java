package com.likelion.timer.domain.Timer.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.likelion.timer.domain.Timer.domain.entity.Part;
import com.likelion.timer.domain.Timer.domain.repository.PartRepository;
import com.likelion.timer.domain.Timer.error.TimerErrorCode;
import com.likelion.timer.domain.model.PartTypeEnum;
import com.likelion.timer.global.error.exception.AppException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PartService {

	private final PartRepository partRepository;
	private static final Random RANDOM = new Random();

	public List<Part> getPartsByPartTypeEnums(List<PartTypeEnum> parts) {
		return parts.stream()
			.map(partType -> partRepository.findByPartType(partType)
				.orElseThrow(() -> new AppException(TimerErrorCode.INVALID_PART_TYPE)))
			.collect(Collectors.toList());
	}

	public List<PartTypeEnum> getRandomUniquePartTypes(int n) {
		// 모든 PartTypeEnum 값들을 리스트로 변환
		List<PartTypeEnum> allPartTypes = new ArrayList<>(List.of(PartTypeEnum.values()));

		// 리스트를 무작위로 섞습니다
		Collections.shuffle(allPartTypes, RANDOM);

		// n이 리스트의 크기보다 크면 리스트 전체를 반환
		int limit = Math.min(n, allPartTypes.size());

		// 중복 없는 n개의 PartTypeEnum 값을 반환
		return allPartTypes.stream()
			.limit(limit)
			.collect(Collectors.toList());
	}
}