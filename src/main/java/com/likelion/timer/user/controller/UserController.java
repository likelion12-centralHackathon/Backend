package com.likelion.timer.user.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.likelion.timer.login.dto.reponse.ApiResponse;
import com.likelion.timer.user.dto.UserDto;
import com.likelion.timer.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class UserController {

	private final UserService userService;

	@GetMapping
	public ResponseEntity<ApiResponse> findByNickname(@RequestParam(value = "nickname") String nickname) {
		UserDto dto = userService.findByNickname(nickname);

		return ResponseEntity.ok().body(new ApiResponse(dto));
	}

	@Operation(summary = "닉네임 중복 체크")
	@GetMapping("/check-nickname")
	public ResponseEntity<ApiResponse> checkNickname(@RequestParam(value = "nickname") String nickname) {
		boolean existsNickname = userService.checkNickname(nickname);

		return ResponseEntity.ok().body(new ApiResponse(existsNickname));
	}

	@Operation(summary = "유저 정보 조회",
		responses = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode = "200",
				description = "조회 성공",
				content = @Content(
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = UserDto.class)
				)
			)
		}
	)
	@GetMapping("/info/{id}")
	public ResponseEntity<ApiResponse> getUser(@PathVariable String id) {
		UserDto dto = userService.getUser(id);

		return ResponseEntity.ok().body(new ApiResponse(dto));
	}
}



