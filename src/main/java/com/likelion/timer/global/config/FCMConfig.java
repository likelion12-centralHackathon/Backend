package com.likelion.timer.global.config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import com.google.api.client.util.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

@Configuration
public class FCMConfig {
	@Value("${firebase.type}")
	private String type;

	@Value("${firebase.project_id}")
	private String projectId;

	@Value("${firebase.private_key_id}")
	private String privateKeyId;

	@Value("${firebase.private_key}")
	private String privateKey;

	@Value("${firebase.client_email}")
	private String clientEmail;

	@Value("${firebase.client_id}")
	private String clientId;

	@Value("${firebase.auth_uri}")
	private String authUri;

	@Value("${firebase.token_uri}")
	private String tokenUri;

	@Value("${firebase.auth_provider_x509_cert_url}")
	private String authProviderX509CertUrl;

	@Value("${firebase.client_x509_cert_url}")
	private String clientX509CertUrl;


	@Bean
	FirebaseMessaging firebaseMessaging() throws IOException {
		FirebaseApp firebaseApp = null;
		List<FirebaseApp> firebaseAppList = FirebaseApp.getApps();

		if (firebaseAppList != null && !firebaseAppList.isEmpty()) {
			for (FirebaseApp app : firebaseAppList) {
				if (app.getName().equals(FirebaseApp.DEFAULT_APP_NAME))
					firebaseApp = app;
			}
		} else {
			Properties properties = new Properties();
			properties.put("type", type);
			properties.put("project_id", projectId);
			properties.put("private_key_id", privateKeyId);
			properties.put("private_key", privateKey);
			properties.put("client_email", clientEmail);
			properties.put("client_id", clientId);
			properties.put("auth_uri", authUri);
			properties.put("token_uri", tokenUri);
			properties.put("auth_provider_x509_cert_url", authProviderX509CertUrl);
			properties.put("client_x509_cert_url", clientX509CertUrl);

			FirebaseOptions options = FirebaseOptions.builder()
					.setCredentials(GoogleCredentials.fromStream(propertiesToInputStream(properties)))
					.build();

			firebaseApp = FirebaseApp.initializeApp(options);
		}

		assert firebaseApp != null;
		return FirebaseMessaging.getInstance(firebaseApp);
	}

	private InputStream propertiesToInputStream(Properties properties) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		properties.store(byteArrayOutputStream, null);
		return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
	}
}