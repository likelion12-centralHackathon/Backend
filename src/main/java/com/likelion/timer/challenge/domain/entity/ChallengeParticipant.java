package com.likelion.timer.challenge.domain.entity;

import java.util.List;

import com.likelion.timer.user.model.User;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "challenge_participants")
@Getter
@Setter
public class ChallengeParticipant {

	@Id
	@Column(name = "challenge_participant_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "challenge_id", nullable = false)
	private Challenge challenge;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private State state;

	@ElementCollection
	@CollectionTable(name = "participant_photos", joinColumns = @JoinColumn(name = "participant_id"))
	@Column(name = "photo_url", length = 255)
	private List<String> photos;

	public enum State {
		PENDING,
		IN_PROGRESS,
		COMPLETED
	}
}

