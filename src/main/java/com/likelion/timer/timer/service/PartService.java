package com.likelion.timer.timer.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.likelion.timer.global.error.exception.AppException;
import com.likelion.timer.timer.domain.entity.Part;
import com.likelion.timer.timer.domain.repository.PartRepository;
import com.likelion.timer.timer.error.TimerErrorCode;
import com.likelion.timer.timer.model.PartTypeEnum;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PartService {

	private final PartRepository partRepository;
	private static final Random RANDOM = new Random();

	public List<Part> changePartIdToPartList(List<Long> partIds) {
		return partIds.stream()
			.map(partId -> partRepository.findById(partId)
				.orElseThrow(() -> new AppException(TimerErrorCode.INVALID_PART_TYPE)))
			.collect(Collectors.toList());
	}

	public List<Long> getRandomUniquePartIds(int n) {
		// 모든 PartTypeEnum 값들의 key 리스트 생성
		List<Long> allPartKeys = Arrays.stream(PartTypeEnum.values())
			.map(partTypeEnum -> partTypeEnum.getKey().longValue())
			.collect(Collectors.toList());

		// 리스트를 무작위로 섞기
		Collections.shuffle(allPartKeys, RANDOM);

		// n이 리스트의 크기보다 크면 리스트 전체를 반환
		int limit = Math.min(n, allPartKeys.size());

		// 중복 없는 n개의 key 값을 반환
		return allPartKeys.stream()
			.limit(limit)
			.collect(Collectors.toList());
	}
}