package com.likelion.timer.challenge.controller;

import com.likelion.timer.challenge.DTO.ChallengeCertificationDto;
import com.likelion.timer.challenge.DTO.ChallengeOverviewDto;
import com.likelion.timer.challenge.DTO.ChallengeViewStep2Dto;
import com.likelion.timer.challenge.DTO.ChallengeCreateStep1Dto;
import com.likelion.timer.challenge.DTO.ChallengeCreateStep2Dto;
import com.likelion.timer.challenge.domain.entity.Challenge;
import com.likelion.timer.challenge.service.ChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/challenge")
public class ChallengeController {

    @Autowired
    private ChallengeService challengeService;

    // Step 1: 이미지와 카테고리 저장
    @PostMapping("/create/step1")
    public Challenge createChallengeStep1(@RequestBody ChallengeCreateStep1Dto step1Dto) {
        return challengeService.saveStep1(step1Dto);
    }

    // Step 2: 제목, 내용 등 나머지 정보 저장
    @PostMapping("/create/step2/{challengeId}")
    public Challenge createChallengeStep2(@PathVariable Long challengeId,
                                          @RequestBody ChallengeCreateStep2Dto step2Dto) throws Exception {
        return challengeService.saveStep2(challengeId, step2Dto);
    }

    // 현재 사용자의 진행 중인 챌린지들 조회
    @GetMapping("/inprogress/{userId}")
    public List<ChallengeOverviewDto> getInProgressChallenges(@PathVariable Long userId) {
        return challengeService.getInProgressChallenges(userId);
    }

    // 특정 카테고리의 챌린지들 조회
    @GetMapping("/category/{category}")
    public List<ChallengeOverviewDto> getChallengesByCategory(@PathVariable String category) {
        return challengeService.getChallengesByCategory(category);
    }

    // 완료한 챌린지들 조회
    @GetMapping("/completed/{userId}")
    public List<ChallengeOverviewDto> getCompletedChallenges(@PathVariable Long userId) {
        return challengeService.getCompletedChallenges(userId);
    }

    // 챌린지 상세 보기
    @GetMapping("/view/{challengeId}")
    public ChallengeViewStep2Dto viewChallengeDetails(@PathVariable Long challengeId) {
        return challengeService.getChallengeDetails(challengeId);
    }

    // 챌린지 인증 사진 업로드
    @PostMapping("/certification/upload")
    public void uploadCertification(@RequestBody ChallengeCertificationDto certificationDto) {
        challengeService.uploadCertification(certificationDto);
    }
}
