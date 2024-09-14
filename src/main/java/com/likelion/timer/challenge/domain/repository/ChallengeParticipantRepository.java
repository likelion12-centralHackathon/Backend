package com.likelion.timer.challenge.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.likelion.timer.challenge.domain.entity.ChallengeParticipant;
import com.likelion.timer.user.model.User;

public interface ChallengeParticipantRepository extends JpaRepository<ChallengeParticipant, Long> {
	Optional<ChallengeParticipant> findByIdAndUser(Long challengeId, User user);

	List<ChallengeParticipant> findByUserAndState(User user, ChallengeParticipant.State state);
}
