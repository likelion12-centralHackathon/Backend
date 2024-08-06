package com.likelion.timer.challenge.controller;

import com.likelion.timer.challenge.DTO.BootchallengeCreateDTO;
import com.likelion.timer.challenge.DTO.BootchallengeDTO;
import com.likelion.timer.challenge.DTO.BootchallengeResponseDTO;
import com.likelion.timer.challenge.DTO.BootchallengeUpdateDTO;
import com.likelion.timer.challenge.domain.entity.Bootchallenge;
import com.likelion.timer.challenge.service.ChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/challenge")
public class ChallengeController {

    @Autowired
    private ChallengeService challengeService;

    //처음 challenge 페이지
    @GetMapping
    public List<BootchallengeDTO> challengeHome(@RequestParam(value = "category", defaultValue = "ALL") Bootchallenge.ChallengeCategory category) {
        List<BootchallengeDTO> challenges = challengeService.getChallengesByCategory(category);
        // List<BootchallengeDTO> participatingChallenges = challengeService.getParticipatingChallenges(); // 필요 시 사용
        return challenges;
    }

    //만드는 페이지
    @GetMapping("/create")
    public String challengeWriteForm() {
        return "Ready to create challenge";
    }

    @PostMapping("/createpro")
    public String challengeCreatePro(@ModelAttribute BootchallengeCreateDTO dto, @RequestParam("file") MultipartFile file) throws IOException {
        challengeService.create(dto, file);
        return "글 작성이 완료되었습니다.";
    }

    //챌린지 1차 상세 설명
    @GetMapping("/view/brief/{id}")
    public BootchallengeResponseDTO challengeBriefView(@PathVariable("id") Integer id) {
        return challengeService.getChallengeBrief(id);
    }

    //챌린지 2차 상세 설명
    @GetMapping("/view/detail/{id}")
    public BootchallengeResponseDTO challengeDetailView(@PathVariable("id") Integer id) {
        return challengeService.getChallengeDetail(id);
    }

    //챌린지 상세 보기
    @GetMapping("/view/{id}")
    public BootchallengeResponseDTO challengeView(@PathVariable("id") Integer id) {
        return challengeService.challengeView(id);
    }

    //챌린지 삭제
    @DeleteMapping("/delete/{id}")
    public String challengeDelete(@PathVariable("id") Integer id) {
        challengeService.challengeDelete(id);
        return "챌린지가 삭제되었습니다.";
    }

    //챌린지 수정
    @PutMapping("/update/{id}")
    public String challengeUpdate(@PathVariable("id") Integer id, @ModelAttribute BootchallengeUpdateDTO dto, @RequestParam("file") MultipartFile file) throws Exception {
        challengeService.update(id, dto);
        return "챌린지가 수정되었습니다.";
    }

    @PostMapping("/participate/{id}")
    public String participateChallenge(@PathVariable("id") Integer id) {
        challengeService.incrementParticipants(id);
        return "참여가 완료되었습니다.";
    }

    //완료한 챌린지 보여주는 페이지
    @GetMapping("/list/completed")
    public List<BootchallengeDTO> completedChallenges() {
        return challengeService.getCompletedChallenges();
    }

    //인증하는 페이지
    @GetMapping("/participate/conform/{id}")
    public BootchallengeResponseDTO challengeConformForm(@PathVariable("id") Integer id) {
        return challengeService.challengeView(id);
    }

    @PostMapping("/participate/conform")
    public String challengeConform(@RequestParam("id") Integer id, @RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2, @RequestParam("file3") MultipartFile file3) throws Exception {
        challengeService.conformChallenge(id, file1, file2, file3);
        return "참여 내용이 성공적으로 저장되었습니다.";
    }
}
