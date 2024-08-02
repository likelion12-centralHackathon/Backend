package com.likelion.timer.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.likelion.timer.user.model.BodyPartSatisfaction;
import com.likelion.timer.user.service.BodyPartSatisfactionService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/satisfaction")
public class BodyPartSatisfactionController {

	private final BodyPartSatisfactionService partSatisfactionService;

	@PostMapping
	public ResponseEntity<BodyPartSatisfaction> createPartSatisfaction(
		@RequestParam Long partId,
		@RequestParam Integer satisfactionScore
	) {
		// 서비스 레이어를 호출하여 PartSatisfaction을 생성합니다.
		BodyPartSatisfaction partSatisfaction = partSatisfactionService.createPartSatisfaction(partId,
			satisfactionScore);
		// 생성된 PartSatisfaction 객체를 HTTP 상태 201(Created)로 응답합니다.
		return new ResponseEntity<>(partSatisfaction, HttpStatus.CREATED);
	}
}
