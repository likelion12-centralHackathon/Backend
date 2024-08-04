package com.likelion.timer.challenge.controller;

import com.likelion.timer.challenge.DTO.BootchallengeCreateDTO;
import com.likelion.timer.challenge.DTO.BootchallengeDTO;
import com.likelion.timer.challenge.DTO.BootchallengeResponseDTO;
import com.likelion.timer.challenge.DTO.BootchallengeUpdateDTO;
import org.springframework.ui.Model;
import com.likelion.timer.challenge.domain.entity.Bootchallenge;
import com.likelion.timer.challenge.service.ChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Controller
@RequestMapping("/api/v1/challenge")
public class ChallengeController {

    @Autowired
    private ChallengeService challengeService;

    //처음 challenge 페이지
    @GetMapping
    public String challengeHome(@RequestParam(value = "category", defaultValue = "ALL") Bootchallenge.ChallengeCategory category, Model model) {
        List<BootchallengeDTO> challenges = challengeService.getChallengesByCategory(category);
        List<BootchallengeDTO> participatingChallenges = challengeService.getParticipatingChallenges();
        model.addAttribute("participatingChallenges", participatingChallenges);
        model.addAttribute("selectedCategory", category);
        return "challengehome";
    }

    //만드는 페이지
    @GetMapping("/create")
    public String challengeWriteForm() {
        return "challengewrite";
    }

    @PostMapping("/createpro")
    public String challengeCreatePro(BootchallengeCreateDTO dto, @RequestParam("file") MultipartFile file, Model model) throws Exception {
        challengeService.create(dto, file);
        model.addAttribute("message", "글 작성이 완료되었습니다.");
        model.addAttribute("searchUrl", "/api/v1/challenge");
        return "message";
    }

    //챌린지 1차 상세 설명
    @GetMapping("/view/brief/{id}")
    public String challengeBriefView(@PathVariable("id") Integer id, Model model) {
        BootchallengeResponseDTO bootchallenge = challengeService.getChallengeBrief(id);
        model.addAttribute("bootchallenge", bootchallenge);
        return "challengeBriefView";
    }

    //챌린지 2차 상세 설명
    @GetMapping("/view/detail/{id}")
    public String challengeDetailView(@PathVariable("id") Integer id, Model model) {
        BootchallengeResponseDTO bootchallenge = challengeService.getChallengeDetail(id);
        model.addAttribute("bootchallenge", bootchallenge);
        return "challengeDetailView";
    }
    //챌린지 상세 보기
    @GetMapping("/view/{id}")
    public String challengeView(@PathVariable("id") Integer id, Model model) {
        BootchallengeResponseDTO bootchallenge = challengeService.challengeView(id);
        model.addAttribute("bootchallenge", bootchallenge);
        return "challengeview";
    }

    //챌린지 삭제
    @GetMapping("/delete/{id}")
    public String challengeDelete(@PathVariable("id") Integer id) {
        challengeService.challengeDelete(id);
        return "redirect:/api/v1/challenge/list";
    }

    //챌린지 수정
    @PostMapping("/update/{id}")
    public String challengeUpdate(@PathVariable("id") Integer id, BootchallengeUpdateDTO dto, @RequestParam("file") MultipartFile file) throws Exception {
        challengeService.update(id, dto);
        return "redirect:/api/v1/challenge";
    }

    @PostMapping("/participate/{id}")
    public String participateChallenge(@PathVariable("id") Integer id) {
        challengeService.incrementParticipants(id);
        return "redirect:/api/v1/challenge/view/" + id;
    }

    //완료한 챌린지 보여주는 페이지
    @GetMapping("/list/completed")
    public String completedChallenges(Model model) {
        List<BootchallengeDTO> completedChallenges = challengeService.getCompletedChallenges();
        model.addAttribute("challenges", completedChallenges);
        return "participatingchallenges";
    }

    //인증하는 페이지
    @GetMapping("/participate/conform/{id}")
    public String challengeConformForm(@PathVariable("id") Integer id, Model model) {
        BootchallengeResponseDTO challenge = challengeService.challengeView(id);
        model.addAttribute("challenge", challenge);
        return "challengeConformForm"; //// 설명 및 사진을 첨부할 수 있는 폼을 보여주는 뷰
    }
    @PostMapping("/participate/conform")
    public String challengeConform(@RequestParam("id") Integer id, @RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2, @RequestParam("file3") MultipartFile file3, Model model) throws Exception {
        challengeService.conformChallenge(id, file1, file2, file3);
        model.addAttribute("message", "참여 내용이 성공적으로 저장되었습니다.");
        model.addAttribute("searchUrl", "/api/v1/challenge/list");
        return "message";
    }
}
