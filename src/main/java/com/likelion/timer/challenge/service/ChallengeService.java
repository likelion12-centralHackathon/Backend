package com.likelion.timer.challenge.service;

import com.likelion.timer.challenge.DTO.BootchallengeCreateDTO;
import com.likelion.timer.challenge.DTO.BootchallengeDTO;
import com.likelion.timer.challenge.DTO.BootchallengeResponseDTO;
import com.likelion.timer.challenge.DTO.BootchallengeUpdateDTO;
import com.likelion.timer.challenge.domain.entity.Bootchallenge;
import com.likelion.timer.challenge.domain.repository.ChallengeRepository;
import com.likelion.timer.challenge.error.ResourceNotFoundException;
import com.likelion.timer.challenge.mapper.BootchallengeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ChallengeService {

    @Autowired
    private ChallengeRepository challengeRepository;

    private final String uploadDir = "src/main/resources/static/images/";

    // 카테고리별 챌린지 가져오기
    public List<BootchallengeDTO> getChallengesByCategory(Bootchallenge.ChallengeCategory category) {
        List<Bootchallenge> challenges;
        if (category == Bootchallenge.ChallengeCategory.ALL) {
            challenges = challengeRepository.findAll();
        } else {
            challenges = challengeRepository.findByCategory(category);
        }
        return challenges.stream().map(BootchallengeMapper::toDTO).toList();
    }

    // 특정 게시글 불러오기
    public BootchallengeResponseDTO challengeView(Integer id) {
        Bootchallenge bootchallenge = challengeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Challenge not found"));
        bootchallenge.setHit(bootchallenge.getHit() + 1);
        challengeRepository.save(bootchallenge);
        return BootchallengeMapper.toResponseDTO(bootchallenge);
    }

    // 특정 게시글 1차 불러오기
    public BootchallengeResponseDTO getChallengeBrief(Integer id) {
        Bootchallenge bootchallenge = challengeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Challenge not found"));
        bootchallenge.setHit(bootchallenge.getHit() + 1);
        challengeRepository.save(bootchallenge);
        return BootchallengeMapper.toResponseDTO(bootchallenge);
    }

    // 특정 게시글 2차 불러오기
    public BootchallengeResponseDTO getChallengeDetail(Integer id) {
        Bootchallenge bootchallenge = challengeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Challenge not found"));
        return BootchallengeMapper.toResponseDTO(bootchallenge);
    }

    // 특정 게시글 삭제
    public void challengeDelete(Integer id) {
        challengeRepository.deleteById(id);
    }

    // 게시글 생성
    public BootchallengeResponseDTO create(BootchallengeCreateDTO dto, MultipartFile file) throws IOException {
        Bootchallenge bootchallenge = BootchallengeMapper.toEntity(dto);

        // 파일 업로드 처리
        if (!file.isEmpty()) {
            String imageName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + imageName);
            Files.createDirectories(filePath.getParent()); // Ensure directory exists
            file.transferTo(filePath.toFile());
            bootchallenge.setImageUrl("/images/" + imageName);
        }

        challengeRepository.save(bootchallenge);
        return BootchallengeMapper.toResponseDTO(bootchallenge);
    }

    // 게시글 업데이트
    public BootchallengeResponseDTO update(Integer id, BootchallengeUpdateDTO dto) {
        Bootchallenge bootchallenge = challengeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Challenge not found"));
        bootchallenge = BootchallengeMapper.toEntity(dto, bootchallenge);
        challengeRepository.save(bootchallenge);
        return BootchallengeMapper.toResponseDTO(bootchallenge);
    }

    // 특정 게시글의 참여자 수 증가
    public void incrementParticipants(Integer id) {
        Bootchallenge bootchallenge = challengeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Challenge not found"));
        bootchallenge.setParticipants(bootchallenge.getParticipants() + 1);
        challengeRepository.save(bootchallenge);
    }

    // 참여 완료 상태인 챌린지 목록 가져오기
    public List<BootchallengeDTO> getCompletedChallenges() {
        return challengeRepository.findByState(Bootchallenge.ChallengeState.PARTICIPATION_COMPLETED).stream().map(BootchallengeMapper::toDTO).toList();
    }

    // 참여 상태인 챌린지 목록 가져오기
    public List<BootchallengeDTO> getParticipatingChallenges() {
        return challengeRepository.findByState(Bootchallenge.ChallengeState.PARTICIPATED).stream().map(BootchallengeMapper::toDTO).toList();
    }

    //참여 인증하는 페이지
    public void conformChallenge(Integer id, MultipartFile file1, MultipartFile file2, MultipartFile file3) throws Exception {
        Bootchallenge bootchallenge = challengeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Challenge not found"));

        // Ensure all files are present and save them
        if (!file1.isEmpty() && !file2.isEmpty() && !file3.isEmpty()) {
            saveFile(file1);
            saveFile(file2);
            saveFile(file3);
        }

        challengeRepository.save(bootchallenge);
    }

    private void saveFile(MultipartFile file) throws IOException {
        String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + filename);
        Files.createDirectories(filePath.getParent()); // Ensure directory exists
        file.transferTo(filePath.toFile());
    }
}

