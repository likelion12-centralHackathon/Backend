package com.likelion.timer.user.constant;

import lombok.Getter;

@Getter
public enum UserStatus {
	NORMAL("normal"),
	DELETE("delete");

	private String name;

	UserStatus(String name) {
		this.name = name;
	}
}
