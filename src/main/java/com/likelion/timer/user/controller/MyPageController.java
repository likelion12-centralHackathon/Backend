package com.likelion.timer.user.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.likelion.timer.login.dto.reponse.ApiResponse;
import com.likelion.timer.user.dto.UserDto;
import com.likelion.timer.user.dto.request.EditUserRequest;
import com.likelion.timer.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class MyPageController {

	private final UserService userService;

	@Operation(summary = "내 정보 조회",
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
	@GetMapping("/info")
	public ResponseEntity<ApiResponse> getMyInfo(Authentication authentication) {
		UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		UserDto dto = userService.getUser(userDetails.getUsername());

		return ResponseEntity.ok().body(new ApiResponse(dto));
	}

	@Operation(summary = "내 정보 수정",
		responses = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode = "200",
				description = "수정 성공",
				content = @Content(
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = UserDto.class)
				)
			)
		}
	)
	@PutMapping("/info/edit")
	public ResponseEntity<ApiResponse> editUserInfo(Authentication authentication,
		@RequestPart(required = false) @Valid EditUserRequest data,
		@RequestPart(value = "file", required = false) MultipartFile file) {
		UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		UserDto dto = userService.edit(userDetails.getUsername(), data, file);

		return ResponseEntity.ok().body(new ApiResponse(dto));
	}

	// 탈퇴 처리
	@Operation(summary = "회원 탈퇴")
	@DeleteMapping("/info/delete")
	public ResponseEntity<ApiResponse> deleteUser(Authentication authentication) {
		UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		userService.delete(userDetails.getUsername());

		return ResponseEntity.ok().body(new ApiResponse(true));
	}
}
