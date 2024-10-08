package com.likelion.timer.login.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.likelion.timer.login.auth.JwtAuthenticationFilter;
import com.likelion.timer.login.auth.JwtProvider;
import com.likelion.timer.login.auth.TokenAccessDeniedHandler;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

	private final TokenAccessDeniedHandler tokenAccessDeniedHandler;
	private final JwtProvider jwtProvider;

	public static final String[] WHITELIST = {
		"/api/v1/users/login/**",
		"/api/v1/users/check-nickname",
		"/images/**",
		"/swagger-ui/**",
		"/v3/api-docs/**",
		"/error",
		"/test/**",
		"/api/v1/Youtube/keywordSearchData.do",
		"api/v1/challenge/top3"
	};

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.httpBasic(HttpBasicConfigurer::disable)
			.csrf(CsrfConfigurer::disable)
			.cors(Customizer.withDefaults())
			.formLogin(FormLoginConfigurer::disable)
			.sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers(WHITELIST).permitAll()
				.anyRequest().authenticated())
			.addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
