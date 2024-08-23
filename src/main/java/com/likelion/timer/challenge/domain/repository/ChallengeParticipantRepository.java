package com.likelion.timer.challenge.domain.repository;


import com.likelion.timer.challenge.domain.entity.ChallengeParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChallengeParticipantRepository extends JpaRepository<ChallengeParticipant, Long> {
    Optional<ChallengeParticipant> findByUserIdAndChallengeId(Long userId, Long challengeId);
    List<ChallengeParticipant> findByUserIdAndState(Long userId, ChallengeParticipant.State state);
}
