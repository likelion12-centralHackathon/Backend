package com.likelion.timer.login.dto.reponse;

import lombok.Data;


public class ApiResponse {

	private Object data;

	public ApiResponse(Object data) {
		this.data = data;
	}
}
