package com.likelion.timer.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.likelion.timer.login.model.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

	RefreshToken findByUserId(String userId);

	RefreshToken findByUserIdAndRefreshToken(String userId, String refreshToken);
}
