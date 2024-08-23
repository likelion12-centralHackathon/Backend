package com.likelion.timer.challenge.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class ChallengeOverviewDto {
    private Long id;
    private String title;
    private String imgUrl;
    private int participantCount;
    private Date endDate;
}
