package com.likelion.timer.challenge.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Bootchallenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challenge_Id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "ch_category")
    private ChallengeCategory category;

    //제목
    @Column(name = "ch_title", length = 100, nullable = false)
    private String title;

    //소개
    @Column(name = "ch_ctnt", length = 225)
    private String content;

    //인증 방법
    @Column(name = "auth_method", length = 100)
    private String authMethod;

    //진행 기간으로 드롭 다운으로 날짜를 받아야할 듯
    @Column(name = "end_date")
    private LocalDate endDate;

    //주의사항
    @Column(name = "note", length = 225)
    private String note;

    //참여, 미참여, 대기
    @Enumerated(EnumType.STRING)
    @Column(name = "ch_state")
    private ChallengeState state;

    //조회수
    @Column(name = "ch_hit")
    private Long hit = 0L;

    //참여자 수
    @Column(name = "ch_participants")
    private Long participants = 0L;

    // ChallengeState enum 정의
    public enum ChallengeState {
        PARTICIPATED,
        NOT_PARTICIPATED,
        PENDING
    }

    //ChallengeCategory enum 정의
    public enum ChallengeCategory {
        DEVELOPMENT,
        HEALTH,
        FREE,
        ALL
    }
}
