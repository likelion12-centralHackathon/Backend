package com.likelion.timer.login.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "카카오 인증 response")
@Data
public class AuthTokenDto {

	@Schema(description = "카카오 로그인 유저 추가 정보 입력 여부(=회원가입)", example = "false")
	private boolean isJoined;

	@Schema(example = "LbXsIdszBoNqua_rkfKL_EmVgxLNjQQwAAAAAQopyNkAAAGRBAtKYNEMsmlHt4Ko")
	private String accessToken;

	@Schema(description = "accessToken 만료 기간", example = "2024-07-28T17:43:37.116")
	private LocalDateTime accessTokenExpiration;

	@Schema(example = "LyOn8JetMsH3mY25yFIZa6A-4pWZOeGSAAAAAgopyNkAAAGRBAtKXdEMsmlHt4Ko")
	private String refreshToken;

	@Schema(description = "refreshToken 만료 기간", example = "2024-07-28T17:43:37.116")
	private LocalDateTime refreshTokenExpiration;

	@Builder
	public AuthTokenDto(boolean isJoined, TokenDto accessTokenDto, TokenDto refreshTokenDto) {
		this.isJoined = isJoined;
		this.accessToken = accessTokenDto.getToken();
		this.accessTokenExpiration = accessTokenDto.getExpiration();
		this.refreshToken = refreshTokenDto.getToken();
		this.refreshTokenExpiration = refreshTokenDto.getExpiration();
	}
}
