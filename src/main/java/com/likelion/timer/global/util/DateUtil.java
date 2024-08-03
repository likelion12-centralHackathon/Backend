package com.likelion.timer.global.util;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DateUtil {

	// 받아온 시간과 현재 시간의 차이 계산
	public Long getDurationBetweenNow(LocalDateTime beforeTime) {
		LocalDateTime now = LocalDateTime.now();

		return Duration.between(beforeTime, now).getSeconds();
	}

}
