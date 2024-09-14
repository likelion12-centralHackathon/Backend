package com.likelion.timer.challenge.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Challengetop3DTO {
    private Long id;
    private String title;
    private String content;
    private int viewCount;
}
