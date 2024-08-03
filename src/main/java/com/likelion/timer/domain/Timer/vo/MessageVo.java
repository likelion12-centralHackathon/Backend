package com.likelion.timer.domain.Timer.vo;

import lombok.Getter;

@Getter
public class MessageVo {
	private final String deviceToken;
	private final String title;
	private final String body;

	public MessageVo(String deviceToken, String title, String body) {
		this.deviceToken = deviceToken;
		this.title = title;
		this.body = body;
	}
}
