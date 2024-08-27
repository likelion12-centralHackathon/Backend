package com.likelion.timer.login.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
public class TokenDto {

	private String token;

	private LocalDateTime expiration;

	@Builder
	public TokenDto(String token, Date expiration) {
		this.token = token;
		this.expiration = new Timestamp(expiration.getTime()).toLocalDateTime();
		;
	}
}
