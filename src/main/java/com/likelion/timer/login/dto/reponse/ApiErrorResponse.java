package com.likelion.timer.login.dto.reponse;

import com.likelion.timer.login.exception.ApiException;
import com.likelion.timer.login.exception.Error;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ApiErrorResponse {

	private Integer code;

	private String message;

	public ApiErrorResponse(ApiException error) {
		this.code = error.getCode();
		this.message = error.getMessage();
	}

	public ApiErrorResponse(Error error) {
		this.code = error.getCode();
		this.message = error.getMessage();
	}

	public ApiErrorResponse(String message) {
		this.code = Error.INVALID_DATA.getCode();
		this.message = message;
	}
}