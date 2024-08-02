package com.likelion.timer.login.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

import com.likelion.timer.login.auth.JwtProvider;
import com.likelion.timer.login.constant.AuthType;
import com.likelion.timer.login.dto.AuthTokenDto;
import com.likelion.timer.login.dto.TokenDto;
import com.likelion.timer.login.dto.reponse.KakaoAuthResponse;
import com.likelion.timer.login.dto.reponse.KakaoUserInfoResponse;
import com.likelion.timer.login.exception.ApiException;
import com.likelion.timer.login.exception.Error;
import com.likelion.timer.login.model.RefreshToken;
import com.likelion.timer.login.repository.RefreshTokenRepository;
import com.likelion.timer.user.model.User;
import com.likelion.timer.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LoginService {

	private final UserRepository userRepository;
	private final RefreshTokenRepository refreshTokenRepository;

	private final JwtProvider jwtProvider;

	private final RestClient restClient1 = RestClient.create("https://kauth.kakao.com");
	private final RestClient restClient2 = RestClient.create("https://kapi.kakao.com");
	

	@Value("${kakao.client-id}")
	private String clientId;

	@Value("${kakao.client-secret}")
	private String clientSecret;

	@Value("${kakao.redirect-uri}")
	private String redirectUri;

	public AuthTokenDto kakaoLogin(String code) {
		//authorizationCode로 kakao accessToken 요청
		
		MultiValueMap<String, String> authRequest = authRequest(code);
		KakaoAuthResponse authResponse = getAccessToken(authRequest);

		//accessToken로 유저정보 요청
		KakaoUserInfoResponse userInfo = getUserInfo(authResponse.getAccessToken());

		boolean isJoined = false;
		User user = userRepository.findBySnsId(userInfo.getId().toString());
		if (user == null) {
			User newUser = User.builder().authType(AuthType.KAKAO).snsId(userInfo.getId().toString()).build();
			user = userRepository.save(newUser);
		} else {
			if (user.getAuthType() != AuthType.KAKAO)
				throw new ApiException(Error.AUTH_TYPE_MISMATCH);
			if (!StringUtils.isEmpty(user.getNickname()))
				isJoined = true; // 닉네임 존재 여부로 회원가입 추가 정보 입력 화면으로 이동
		}

		//토큰 발급
		TokenDto accessTokenDto = jwtProvider.generateToken(user.getId());
		TokenDto refreshTokenDto = jwtProvider.generateRefreshToken();

		RefreshToken refreshToken = refreshTokenRepository.findByUserId(user.getId());
		if (refreshToken == null) {
			refreshToken = RefreshToken.builder()
				.userId(user.getId())
				.build();
		}

		refreshToken.setRefreshToken(refreshTokenDto.getToken());
		refreshToken.setExpiration(refreshTokenDto.getExpiration());

		refreshTokenRepository.save(refreshToken);

		return new AuthTokenDto(isJoined, accessTokenDto, refreshTokenDto);
	}

	private KakaoAuthResponse getAccessToken(MultiValueMap<String, String> request) {
		return restClient1.post()
				.uri("/oauth/token")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(request)
				.retrieve()
				.onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
					//TODO Custom Exception 으로 변경.
					throw new RuntimeException();
				})
				.toEntity(KakaoAuthResponse.class)
				.getBody();
	}

	private KakaoUserInfoResponse getUserInfo(String accessToken) {
		return restClient2.get()
			.uri("/v2/user/me")
			.header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
			.header("Authorization", "Bearer " + accessToken)
			.retrieve()
			.onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
				//TODO Custom Exception 으로 변경.
				throw new RuntimeException();
			})
			.toEntity(KakaoUserInfoResponse.class)
			.getBody();
	}

	private MultiValueMap<String, String> authRequest(String code) {
		MultiValueMap<String, String> authRequest = new LinkedMultiValueMap<>();
		authRequest.add("grant_type", "authorization_code");
		authRequest.add("client_id", clientId);
		authRequest.add("client_secret", clientSecret);
		authRequest.add("redirect_uri", redirectUri);
		authRequest.add("code", code);

		return authRequest;
	}
}
