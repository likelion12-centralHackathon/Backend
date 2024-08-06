package com.likelion.timer.challenge.DTO;

import com.likelion.timer.challenge.domain.entity.Bootchallenge;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BootchallengeCreateDTO {
    private Bootchallenge.ChallengeCategory category;
    private String title;
    private String content;
    private String authMethod;
    private LocalDate endDate;
    private String note;
    private String imageUrl;  // 사진을 받을 필드 추가
}
