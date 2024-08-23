package com.likelion.timer.challenge.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "challenges")
@Getter
@Setter
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long challengeId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, length = 50)
    private String authMethod;

    @Column(columnDefinition = "TEXT")
    private String note;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    private int viewCount = 0;

    @Column(nullable = false)
    private int participantCount = 0;

    @Column(length = 255)
    private String imgUrl;


}


