package com.likelion.timer.global.error.exception;

import com.likelion.timer.global.error.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AppException extends RuntimeException {
	private ErrorCode errorCode;
	private String message;

	public AppException(ErrorCode errorCode) {
		this.errorCode = errorCode;
		this.message = errorCode.getMessage();
	}
}
