package com.likelion.timer.login.constant;

import lombok.Getter;

@Getter
public enum GrantType {
	CUSTOM("custom"),
	KAKAO("kakao");

	private String name;

	GrantType(String name) {
		this.name = name;
	}
}