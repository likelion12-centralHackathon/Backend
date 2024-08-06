package com.likelion.timer.challenge.DTO;

import com.likelion.timer.challenge.domain.entity.Bootchallenge;
import com.likelion.timer.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data

public class BootchallengeDTO {
    private Integer id;
    private User user;
    private Bootchallenge.ChallengeCategory category;
    private String title;
    private String content;
    private String authMethod;
    private LocalDate endDate;
    private String note;
    private Bootchallenge.ChallengeState state;
    private Long hit;
    private Long participants;
    private String imageUrl;


    public BootchallengeDTO(Integer id, User user, Bootchallenge.ChallengeCategory category, String title, String content, String authMethod, LocalDate endDate, String note, Bootchallenge.ChallengeState state, Long hit, Long participants, String imageUrl) {
        this.id = id;
        this.user = user;
        this.category = category;
        this.title = title;
        this.content = content;
        this.authMethod = authMethod;
        this.endDate = endDate;
        this.note = note;
        this.state = state;
        this.hit = hit;
        this.participants = participants;
        this.imageUrl = imageUrl;

    }
}

