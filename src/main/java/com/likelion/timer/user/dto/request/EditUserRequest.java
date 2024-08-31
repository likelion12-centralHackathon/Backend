package com.likelion.timer.user.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
public class EditUserRequest {

	@Size(min = 3, message = "닉네임은 최소 3글자 이상 입력해야 합니다")
	private String nickname;
}
