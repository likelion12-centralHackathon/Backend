package com.likelion.timer.global.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

@Configuration
public class FCMConfig {

	@Bean
	FirebaseMessaging firebaseMessaging() throws IOException {
		String JSON_PATH = "/firebase/firebase_account_file.json";

		ClassPathResource resource = new ClassPathResource(JSON_PATH);
		InputStream refreshToken = resource.getInputStream();

		FirebaseApp firebaseApp = null;
		List<FirebaseApp> firebaseAppList = FirebaseApp.getApps();

		if (firebaseAppList != null && !firebaseAppList.isEmpty()) {
			for (FirebaseApp app : firebaseAppList) {
				if (app.getName().equals(FirebaseApp.DEFAULT_APP_NAME))
					firebaseApp = app;
			}
		} else {
			FirebaseOptions options = FirebaseOptions.builder()
				.setCredentials(GoogleCredentials.fromStream(refreshToken))
				.build();

			firebaseApp = FirebaseApp.initializeApp(options);
		}

		assert firebaseApp != null;
		return FirebaseMessaging.getInstance(firebaseApp);
	}
}
