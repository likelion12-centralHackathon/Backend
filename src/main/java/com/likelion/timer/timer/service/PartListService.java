package com.likelion.timer.timer.service;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.likelion.timer.timer.constants.TimerConstants;
import com.likelion.timer.timer.domain.entity.Part;
import com.likelion.timer.timer.domain.entity.PartList;
import com.likelion.timer.timer.domain.repository.PartListRepository;
import com.likelion.timer.timer.dto.req.PartReqDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class PartListService {

	private final PartService partService;
	private final PartListRepository partListRepository;

	private static final Random RANDOM = new Random();

	// 랜덤하게 partList 설정하는 함수
	public List<PartList> saveRandomUniqueParts(int listSize) {
		// Set을 사용하여 중복된 PartList를 방지합니다.
		Set<PartList> uniquePartLists = new HashSet<>();

		while (uniquePartLists.size() < listSize) {
			// 중복되지 않는 PartTypeEnum 값을 가져옵니다.
			List<Long> uniquePartIds = partService.getRandomUniquePartIds(
				RANDOM.nextInt(TimerConstants.MAX_PARTS) + 1);

			// Part 객체를 가져옵니다.
			List<Part> parts = partService.changePartIdToPartList(uniquePartIds);

			// PartList를 생성합니다.
			PartList partList = PartList.builder()
				.parts(parts)
				.build();

			// Set을 사용하여 중복된 PartList를 방지합니다.
			uniquePartLists.add(partList);
		}

		// PartList를 데이터베이스에 저장합니다.
		return uniquePartLists.stream()
			.map(partList -> partListRepository.save(partList))
			.collect(Collectors.toList());
	}

	// 직접 설정한 partList를 저장하는 것
	public List<PartList> saveParts(List<PartReqDto> partReqDtoList) {
		return partReqDtoList.stream()
			.map(partReqDto -> {
				// 받은 partList의 Id를 part로 변경
				List<Part> parts = partService.changePartIdToPartList(partReqDto.getPartIds());

				// TODO 중복 검사 로직 생각하기
				/*
				// 받은 Part 조합으로 저장된 게 있는지 찾기
				Optional<PartList> existingPartListOpt = partListRepository.findByParts(parts, parts.size());

				// 이미 존재하는 조합인 경우, 해당 PartList 반환
				if (existingPartListOpt.isPresent()) {
					return existingPartListOpt.get();
				}
				 */

				// 새로운 PartList 생성 및 저장
				PartList partList = PartList.builder().parts(parts).build();
				partListRepository.save(partList);
				return partList;
			})
			.collect(Collectors.toList());
	}

}