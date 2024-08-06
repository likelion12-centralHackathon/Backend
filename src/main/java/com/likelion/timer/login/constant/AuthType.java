package com.likelion.timer.login.constant;

import lombok.Getter;

@Getter
public enum AuthType {

	CUSTOM("custom"),
	KAKAO("kakao");

	private String name;

	AuthType(String name) {
		this.name = name;
	}
}
