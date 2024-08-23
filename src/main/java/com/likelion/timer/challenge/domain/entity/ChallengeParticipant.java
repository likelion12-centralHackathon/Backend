package com.likelion.timer.challenge.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "challenge_participants")
@Getter
@Setter
public class ChallengeParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    @Column(nullable = false)
    private Long userId;

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

