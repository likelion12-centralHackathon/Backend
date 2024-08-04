package com.likelion.timer.challenge.domain.entity;

import com.likelion.timer.user.model.User;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;

import static jakarta.persistence.FetchType.LAZY;

@Getter @Setter
@NoArgsConstructor(access= AccessLevel.PUBLIC)
@Entity
public class Bootchallenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challenge_Id")
    private Integer id;

    @ManyToOne(fetch=LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "ch_category")
    private ChallengeCategory category;

    //제목
    @Column(name = "ch_title", length = 100, nullable = false)
    private String title;

    //소개
    @Column(name = "ch_ctnt", length = 225, nullable = false)
    private String content;

    //인증 방법
    @Column(name = "auth_method", length = 100, nullable = false)
    private String authMethod;

    //진행 기간을 드롭 다운으로 날짜를 받아야할 듯
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    //주의사항
    @Column(name = "note", length = 225, nullable = false)
    private String note;

    //참여, 미참여, 대기, 참여완료
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
        PENDING,
        PARTICIPATION_COMPLETED
    }


    //ChallengeCategory enum 정의
    public enum ChallengeCategory {
        DEVELOPMENT,
        HEALTH,
        FREE,
        ALL
    }


}
