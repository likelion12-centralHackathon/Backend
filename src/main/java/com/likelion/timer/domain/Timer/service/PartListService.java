package com.likelion.timer.domain.Timer.service;

import static com.likelion.timer.domain.Timer.constants.TimerConstants.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.likelion.timer.domain.Timer.domain.entity.Part;
import com.likelion.timer.domain.Timer.domain.entity.PartList;
import com.likelion.timer.domain.Timer.domain.repository.PartListRepository;
import com.likelion.timer.domain.Timer.dto.req.PartReqDto;
import com.likelion.timer.domain.model.PartTypeEnum;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PartListService {

	private final PartService partService;
	private final PartListRepository partListRepository;

	private static final Random RANDOM = new Random();

	public List<PartList> randomUniquePartsByPartListSize(int listSize) {
		// Set을 사용하여 중복된 PartList를 방지합니다.
		Set<PartList> uniquePartLists = new HashSet<>();

		while (uniquePartLists.size() < listSize) {
			// 중복되지 않는 PartTypeEnum 값을 가져옵니다.
			List<PartTypeEnum> uniquePartTypes = partService.getRandomUniquePartTypes(
				RANDOM.nextInt(MAX_PARTS) + 1);

			// Part 객체를 가져옵니다.
			List<Part> parts = partService.getPartsByPartTypeEnums(uniquePartTypes);

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

	public List<PartList> getPartsByPartReqList(List<PartReqDto> partReqDtoList) {
		return partReqDtoList.stream()
			.map(partReqDto -> {
				List<Part> parts = partService.getPartsByPartTypeEnums(partReqDto.getPartTypes());

				// 중복 체크 로직 추가
				Optional<PartList> existingPartListOpt = partListRepository.findByParts(parts);
				if (existingPartListOpt.isPresent()) {
					return existingPartListOpt.get();
				}

				PartList partList = PartList.builder().parts(parts).build();
				partListRepository.save(partList);
				return partList;
			})
			.collect(Collectors.toList());
	}
}