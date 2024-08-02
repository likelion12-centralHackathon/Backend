package com.likelion.timer.challenge.repository;

import com.likelion.timer.challenge.entity.Bootchallenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChallengeRepository extends JpaRepository<Bootchallenge, Integer> {
    List<Bootchallenge> findByState(Bootchallenge.ChallengeState challengeState);
}
