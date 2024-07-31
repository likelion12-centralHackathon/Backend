package com.likelion.timer.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.likelion.timer.user.model.User;

public interface UserRepository extends JpaRepository<User, String> {

	User findByNickname(String nickname);

	User findBySnsId(String id);
}
