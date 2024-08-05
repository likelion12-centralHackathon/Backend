package com.likelion.timer.timer.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.likelion.timer.global.common.DataResponseDto;
import com.likelion.timer.global.common.ResponseDto;
import com.likelion.timer.timer.dto.req.TimerReqDto;
import com.likelion.timer.timer.dto.req.TimerUpdateReqDto;
import com.likelion.timer.timer.dto.res.PartStaticResDto;
import com.likelion.timer.timer.dto.res.RecordTimerResDto;
import com.likelion.timer.timer.dto.res.TimerListResDto;
import com.likelion.timer.timer.dto.res.TimerPartListResDto;
import com.likelion.timer.timer.dto.res.TimerResDto;
import com.likelion.timer.timer.service.RecordTimerService;
import com.likelion.timer.timer.service.TimerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/timer")
public class TimerController {

	private final TimerService timerService;
	private final RecordTimerService recordTimerService;

	@GetMapping("/search/{date}")
	public ResponseEntity<ResponseDto> getPartListsByDate(Authentication authentication,
		@PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

		UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		List<TimerPartListResDto> timerPartListResDtos = recordTimerService.getPartListsByDate(
			userDetails.getUsername(), date);

		return ResponseEntity.status(200).body(DataResponseDto.of(timerPartListResDtos, 200));

	}

	@GetMapping("/static/{date}")
	public ResponseEntity<ResponseDto> getPartStaticsByDate(Authentication authentication,
		@PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		List<PartStaticResDto> partStaticResDtos = recordTimerService.getPartStaticsByDate(
			userDetails.getUsername(), date);

		return ResponseEntity.status(200).body(DataResponseDto.of(partStaticResDtos, 200));
	}

	@PostMapping
	public ResponseEntity<ResponseDto> addTimer(Authentication authentication,
		@RequestBody @Valid TimerReqDto timerReqDto) {
		UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		timerService.addTimer(userDetails.getUsername(), timerReqDto);

		return ResponseEntity.ok(ResponseDto.of(201));
	}

	@PostMapping("/state/{timerId}")
	public ResponseEntity<ResponseDto> updateTimerStateAndAddRecordTimer(Authentication authentication,
		@PathVariable(name = "timerId") Long timerId,
		@RequestParam(name = "timerState") Integer timerState,
		@RequestParam(name = "part") Integer partType) {
		UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		RecordTimerResDto recordTimerResDto = recordTimerService.addRecordTimer(userDetails.getUsername(), timerId,
			timerState, partType);
		return ResponseEntity.status(201).body(DataResponseDto.of(recordTimerResDto, 201));
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
		timerService.updateTimer(userDetails.getUsername(), timerId, timerUpdateReqDto);

		return ResponseEntity.ok(ResponseDto.of(200));
	}

	@GetMapping
	public ResponseEntity<ResponseDto> getTimerList(Authentication authentication) {
		UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		List<TimerListResDto> timerList = timerService.getTimerList(userDetails.getUsername());

		if (timerList.isEmpty()) {
			return ResponseEntity.ok(ResponseDto.of(204));
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

	@Scheduled(fixedRate = 1000)
	public void checkTimers() {
		timerService.checkAndSendNotifications();
	}
}
