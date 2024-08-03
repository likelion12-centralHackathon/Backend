package com.likelion.timer.challenge.service;

import com.likelion.timer.challenge.domain.entity.Bootchallenge;
import com.likelion.timer.challenge.domain.repository.ChallengeRepository;
import com.likelion.timer.challenge.error.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Service
public class ChallengeService {

    @Autowired
    private ChallengeRepository challengeRepository;

    // 카테고리별 챌린지 가져오기
    public List<Bootchallenge> getChallengesByCategory(Bootchallenge.ChallengeCategory category) {
        if (category == Bootchallenge.ChallengeCategory.ALL) {
            return challengeRepository.findAll(); // ALL 카테고리 선택 시 모든 챌린지 반환
        }
        return challengeRepository.findByCategory(category);
    }

    //게시글 전체 불러오기
    public Bootchallenge challengeView(Integer id) {
        Bootchallenge bootchallenge = challengeRepository.findById(id).orElseThrow(null);
        if (bootchallenge != null) {
            // 조회수 증가
            bootchallenge.setHit(bootchallenge.getHit() + 1);
            challengeRepository.save(bootchallenge); // 업데이트된 조회수 저장
        }
        return bootchallenge;
    }

    // 특정 게시글 1차 불러오기
    public Bootchallenge getChallengeBrief(Integer id) {
        Bootchallenge bootchallenge = challengeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Challenge not found"));
        if (bootchallenge != null) {
            // 조회수 증가
            bootchallenge.setHit(bootchallenge.getHit() + 1);
            challengeRepository.save(bootchallenge); // 업데이트된 조회수 저장
        }
        return bootchallenge;
    }

    //특정 게시글 2차 불러오기
    public Bootchallenge getChallengeDetail(Integer id) {
        return challengeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Challenge not found"));
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
        Bootchallenge bootchallenge = challengeRepository.findById(id).orElseThrow(null);
        if (bootchallenge != null) {
            bootchallenge.setParticipants(bootchallenge.getParticipants() + 1);
            challengeRepository.save(bootchallenge); // 업데이트된 참여자 수 저장
        }
    }
    // 참여 완료 상태인 챌린지 목록 가져오기
    public List<Bootchallenge> getCompletedChallenges() {
        return challengeRepository.findByState(Bootchallenge.ChallengeState.PARTICIPATION_COMPLETED);
    }
    // 참여 상태인 챌린지 목록 가져오기
    public List<Bootchallenge> getParticipatingChallenges() {
        return challengeRepository.findByState(Bootchallenge.ChallengeState.PARTICIPATED);
    }

    //참여 인증하는 페이지
    public void conformChallenge(Integer id, MultipartFile file1, MultipartFile file2, MultipartFile file3) throws Exception {
        Bootchallenge bootchallenge = challengeRepository.findById(id).orElseThrow(null);
        if (bootchallenge != null) {

            // 파일 처리 로직 추가
            if (!file1.isEmpty() && !file2.isEmpty() && !file3.isEmpty()) {
                file1.transferTo(new File("/path/to/save/" + file1.getOriginalFilename()));
                file2.transferTo(new File("/path/to/save/" + file2.getOriginalFilename()));
                file3.transferTo(new File("/path/to/save/" + file3.getOriginalFilename()));
            }

            challengeRepository.save(bootchallenge);
        }
    }
}
