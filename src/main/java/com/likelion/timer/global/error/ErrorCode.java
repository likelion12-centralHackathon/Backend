package com.likelion.timer.global.error;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
	String name();

	HttpStatus getHttpStatus();

	String getMessage();
}