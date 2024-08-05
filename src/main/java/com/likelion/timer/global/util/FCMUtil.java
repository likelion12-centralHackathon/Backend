package com.likelion.timer.global.util;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.likelion.timer.timer.vo.MessageVo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class FCMUtil {
	private final FirebaseMessaging firebaseMessaging;

	public void sendMessage(MessageVo vo) {
		if (vo.getDeviceToken() == null || vo.getDeviceToken().isEmpty()) {
			log.error("Device token is null or empty");
			return;
		}

		Notification notification = Notification.builder()
			.setTitle(vo.getTitle())
			.setBody(vo.getBody())
			.build();

		Message message = Message.builder()
			.setToken(vo.getDeviceToken())
			.setNotification(notification)
			.build();

		try {
			firebaseMessaging.send(message);
			log.info("[FCM] Sending completed successfully at " + LocalDateTime.now());
			log.info("*** " + vo.getDeviceToken());
		} catch (FirebaseMessagingException e) {
			log.error("Error sending FCM message: " + e.getMessage());
		}
	}

}
