package com.likelion.timer.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.likelion.timer.user.model.BodyPartSatisfaction;

public interface BodyPartSatisfactionRepository extends JpaRepository<BodyPartSatisfaction, Long> {
}