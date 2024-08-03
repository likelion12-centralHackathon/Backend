package com.likelion.timer.domain.Timer.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.likelion.timer.domain.Timer.dto.req.TimerReqDto;
import com.likelion.timer.domain.Timer.dto.req.TimerUpdateReqDto;
import com.likelion.timer.domain.Timer.dto.res.TimerListResDto;
import com.likelion.timer.domain.Timer.dto.res.TimerResDto;
import com.likelion.timer.domain.Timer.service.TimerService;
import com.likelion.timer.domain.model.TimerStateTypeEnum;
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
	public ResponseEntity<ResponseDto> addTimer(Authentication authentication,
		@RequestBody @Valid TimerReqDto timerReqDto) {
		UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		TimerResDto timerResDto = timerService.addTimer(userDetails.getUsername(), timerReqDto);

		return ResponseEntity.status(201).body(DataResponseDto.of(timerResDto, 201));
	}

	@PatchMapping("/{timerId}")
	public ResponseEntity<ResponseDto> updateTimerState(Authentication authentication,
		@PathVariable(name = "timerId") Long timerId, @RequestParam TimerStateTypeEnum timerStateType) {
		UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		timerService.updateTimerState(userDetails.getUsername(), timerId, timerStateType);

		return ResponseEntity.ok(ResponseDto.of(200));
	}

	@DeleteMapping("/{timerId}")
	public ResponseEntity<ResponseDto> deleteTimer(Authentication authentication,
		@PathVariable(name = "timerId") Long timerId) {
		UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		timerService.deleteTimer(userDetails.getUsername(), timerId);

		return ResponseEntity.ok(ResponseDto.of(200));
	}

	@PostMapping("/{timerId}")
	public ResponseEntity<ResponseDto> updateTimer(Authentication authentication,
		@PathVariable(name = "timerId") Long timerId, @RequestBody @Valid TimerUpdateReqDto timerUpdateReqDto) {
		UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		TimerResDto timerResDto = timerService.updateTimer(userDetails.getUsername(), timerId, timerUpdateReqDto);

		return ResponseEntity.status(200).body(DataResponseDto.of(timerResDto, 200));
	}

	@GetMapping
	public ResponseEntity<ResponseDto> getTimerList(Authentication authentication) {
		UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		List<TimerListResDto> timerList = timerService.getTimerList(userDetails.getUsername());

		if (timerList.isEmpty()) {
			return ResponseEntity.status(204).body(DataResponseDto.of("저장된 타이머가 없습니다.", 204));
		} else {
			return ResponseEntity.status(200).body(DataResponseDto.of(timerList, 200));
		}
	}

	@GetMapping("/{timerId}")
	public ResponseEntity<ResponseDto> getTimer(Authentication authentication,
		@PathVariable(name = "timerId") Long timerId) {
		UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		TimerResDto timerResDto = timerService.getTimer(userDetails.getUsername(), timerId);

		return ResponseEntity.status(200).body(DataResponseDto.of(timerResDto, 200));
	}
}
