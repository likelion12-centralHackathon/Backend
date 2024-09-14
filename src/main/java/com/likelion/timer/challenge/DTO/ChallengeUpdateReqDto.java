package com.likelion.timer.challenge.DTO;

import com.google.firebase.database.annotations.NotNull;
import com.likelion.timer.challenge.domain.entity.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ChallengeUpdateReqDto {
    @NotNull
    private String title;

    @NotNull
    private String content;

    @NotNull
    private String authMethod;

    private String note;

    @NotNull
    private Date endDate;

    @NotNull
    private Category category;

    private String imgUrl;
}

