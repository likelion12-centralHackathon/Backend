package com.likelion.timer.challenge.service;

import com.likelion.timer.challenge.DTO.*;
import com.likelion.timer.challenge.domain.entity.Category;
import com.likelion.timer.challenge.domain.entity.Challenge;
import com.likelion.timer.challenge.domain.entity.ChallengeParticipant;
import com.likelion.timer.challenge.domain.repository.ChallengeParticipantRepository;
import com.likelion.timer.challenge.domain.repository.ChallengeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChallengeService {

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private ChallengeParticipantRepository challengeParticipantRepository;

    // Step 1: 이미지와 카테고리 저장
    public Challenge saveStep1(ChallengeCreateStep1Dto step1Dto) {
        Challenge challenge = new Challenge();
        challenge.setImgUrl(step1Dto.getImgUrl());
        challenge.setCategory(Category.valueOf(step1Dto.getCategory().toUpperCase()));
        return challengeRepository.save(challenge);
    }

    // Step 2: 나머지 필드 저장
    public Challenge saveStep2(Long challengeId, ChallengeCreateStep2Dto step2Dto) throws ParseException {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid challenge ID"));

        challenge.setTitle(step2Dto.getTitle());
        challenge.setContent(step2Dto.getContent());
        challenge.setAuthMethod(step2Dto.getAuthMethod());
        challenge.setEndDate(convertStringToDate(step2Dto.getEndDate()));
        challenge.setNote(step2Dto.getNote());

        return challengeRepository.save(challenge);
    }

    // 문자열을 Date로 변환하는 유틸리티 메서드
    private Date convertStringToDate(String dateString) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.parse(dateString);
    }

    // 챌린지 세부 정보 조회
    public ChallengeViewStep2Dto getChallengeDetails(Long challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid challenge ID"));

        ChallengeViewStep2Dto dto = new ChallengeViewStep2Dto();
        dto.setTitle(challenge.getTitle());
        dto.setAuthMethod(challenge.getAuthMethod());
        dto.setEndDate(new SimpleDateFormat("yyyy-MM-dd").format(challenge.getEndDate()));
        dto.setNote(challenge.getNote());

        return dto;
    }

    // 챌린지 인증 사진 업로드 및 상태 업데이트
    public void uploadCertification(ChallengeCertificationDto certificationDto) {
        Optional<ChallengeParticipant> participantOpt = challengeParticipantRepository.findByUserIdAndChallengeId(
                certificationDto.getUserId(), certificationDto.getChallengeId());

        if (participantOpt.isEmpty()) {
            throw new IllegalArgumentException("User has not joined the challenge.");
        }

        ChallengeParticipant participant = participantOpt.get();

        // 필터링된 사진 목록 생성 (null 또는 빈 문자열이 아닌 것만)
        List<String> photos = List.of(
                certificationDto.getPhotoUrl1(),
                certificationDto.getPhotoUrl2(),
                certificationDto.getPhotoUrl3()
        ).stream().filter(photo -> photo != null && !photo.isEmpty()).collect(Collectors.toList());

        participant.setPhotos(photos);

        // 사진이 3장 모두 업로드되었으면 상태를 Completed로 변경
        if (photos.size() == 3) {
            participant.setState(ChallengeParticipant.State.COMPLETED);
        }

        challengeParticipantRepository.save(participant);
    }

    // 현재 사용자의 진행 중인 챌린지들 조회
    public List<ChallengeOverviewDto> getInProgressChallenges(Long userId) {
        return challengeParticipantRepository.findByUserIdAndState(userId, ChallengeParticipant.State.IN_PROGRESS)
                .stream()
                .map(participant -> {
                    Challenge challenge = participant.getChallenge();
                    return new ChallengeOverviewDto(challenge.getChallengeId(), challenge.getTitle(), challenge.getImgUrl(),
                            challenge.getParticipantCount(), challenge.getEndDate());
                })
                .collect(Collectors.toList());
    }

    // 특정 카테고리의 챌린지들 조회
    public List<ChallengeOverviewDto> getChallengesByCategory(String category) {
        return challengeRepository.findByCategory(category.toUpperCase())
                .stream()
                .map(challenge -> new ChallengeOverviewDto(challenge.getChallengeId(), challenge.getTitle(), challenge.getImgUrl(),
                        challenge.getParticipantCount(), challenge.getEndDate()))
                .collect(Collectors.toList());
    }

    // 완료한 챌린지들 조회
    public List<ChallengeOverviewDto> getCompletedChallenges(Long userId) {
        return challengeParticipantRepository.findByUserIdAndState(userId, ChallengeParticipant.State.COMPLETED)
                .stream()
                .map(participant -> {
                    Challenge challenge = participant.getChallenge();
                    return new ChallengeOverviewDto(challenge.getChallengeId(), challenge.getTitle(), challenge.getImgUrl(),
                            challenge.getParticipantCount(), challenge.getEndDate());
                })
                .collect(Collectors.toList());
    }
}
