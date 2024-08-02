package com.likelion.timer.challenge.service;

import com.likelion.timer.challenge.entity.Bootchallenge;
import com.likelion.timer.challenge.repository.ChallengeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ChallengeService {

    @Autowired
    private ChallengeRepository challengeRepository;

    // 특정 게시글 불러오기
    public Bootchallenge challengeView(Integer id) {
        Bootchallenge bootchallenge = challengeRepository.findById(id).orElse(null);
        if (bootchallenge != null) {
            // 조회수 증가
            bootchallenge.setHit(bootchallenge.getHit() + 1);
            challengeRepository.save(bootchallenge); // 업데이트된 조회수 저장
        }
        return bootchallenge;
    }

    // 특정 게시글 삭제
    public void challengeDelete(Integer id) {
        challengeRepository.deleteById(id);
    }

    // 게시글 생성
    public Bootchallenge create(Bootchallenge bootchallenge, MultipartFile file) {
        // 파일을 처리하는 코드 추가 (파일 처리 로직은 생략)
        return challengeRepository.save(bootchallenge);
    }

    // 게시글 수정
    public Bootchallenge update(Bootchallenge bootchallenge) {
        return challengeRepository.save(bootchallenge);
    }

    // 특정 게시글의 참여자 수 증가
    public void incrementParticipants(Integer id) {
        Bootchallenge bootchallenge = challengeRepository.findById(id).orElse(null);
        if (bootchallenge != null) {
            bootchallenge.setParticipants(bootchallenge.getParticipants() + 1);
            challengeRepository.save(bootchallenge); // 업데이트된 참여자 수 저장
        }
    }
    // 참여 상태인 챌린지 목록 가져오기
    public List<Bootchallenge> getParticipatingChallenges() {
        return challengeRepository.findByState(Bootchallenge.ChallengeState.PARTICIPATED);
    }
}
