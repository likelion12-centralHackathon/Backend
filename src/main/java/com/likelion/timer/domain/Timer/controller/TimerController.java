package com.likelion.timer.domain.Timer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.likelion.timer.domain.Timer.dto.req.TimerReqDto;
import com.likelion.timer.domain.Timer.dto.res.TimerResDto;
import com.likelion.timer.domain.Timer.service.TimerService;
import com.likelion.timer.global.common.DataResponseDto;
import com.likelion.timer.global.common.ResponseDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/timer")
public class TimerController {

	private final TimerService timerService;

	@PostMapping
	public ResponseEntity<ResponseDto> addTimer(@RequestBody @Valid TimerReqDto timerReqDto) {
		TimerResDto timerResDto = timerService.addTimer(timerReqDto);

		return ResponseEntity.status(201).body(DataResponseDto.of(timerResDto, 201));
	}

}
