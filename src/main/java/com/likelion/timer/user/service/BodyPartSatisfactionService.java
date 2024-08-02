package com.likelion.timer.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.likelion.timer.domain.Timer.domain.entity.Part;
import com.likelion.timer.domain.Timer.domain.repository.PartRepository;
import com.likelion.timer.domain.Timer.error.TimerErrorCode;
import com.likelion.timer.global.error.exception.AppException;
import com.likelion.timer.user.model.BodyPartSatisfaction;
import com.likelion.timer.user.repository.BodyPartSatisfactionRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BodyPartSatisfactionService {

	private final PartRepository partRepository;
	private final BodyPartSatisfactionRepository partSatisfactionRepository;

	@Transactional
	public BodyPartSatisfaction createPartSatisfaction(Long partId, Integer satisfactionScore) {
		// 주어진 partId로 Part 엔티티를 조회합니다.
		Part part = partRepository.findById(partId)
			.orElseThrow(() -> new AppException(TimerErrorCode.INVALID_PART_TYPE)); // Part가 없으면 예외를 던집니다.

		// PartSatisfaction 객체를 생성하고 값을 설정합니다.
		BodyPartSatisfaction partSatisfaction = new PartSatisfaction();
		partSatisfaction.setPart(part);
		partSatisfaction.setSatisfactionScore(satisfactionScore);

		return partSatisfactionRepository.save(partSatisfaction);
	}
}
