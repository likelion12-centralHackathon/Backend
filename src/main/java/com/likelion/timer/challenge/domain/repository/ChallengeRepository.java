package com.likelion.timer.challenge.domain.repository;


import com.likelion.timer.challenge.domain.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    List<Challenge> findByCategory(String category);
}

