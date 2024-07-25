package com.likelion.timer.global.common;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class DataResponseDto<T> extends ResponseDto {
	private final T data;

	private DataResponseDto(T data, Integer code) {
		super(code, HttpStatus.valueOf(code).getReasonPhrase());
		this.data = data;
	}

	private DataResponseDto(T data, Integer code, String message) {
		super(code, message);
		this.data = data;
	}

	public static <T> DataResponseDto<T> of(T data, Integer code) {
		return new DataResponseDto<>(data, code);
	}

	public static <T> DataResponseDto<T> of(T data, Integer code, String message) {
		return new DataResponseDto<>(data, code, message);
	}
}