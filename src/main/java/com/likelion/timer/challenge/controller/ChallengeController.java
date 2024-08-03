package com.likelion.timer.challenge.controller;

import org.springframework.ui.Model;
import com.likelion.timer.challenge.domain.entity.Bootchallenge;
import com.likelion.timer.challenge.service.ChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Controller
public class ChallengeController {

    @Autowired
    private ChallengeService challengeService;

    //처음 challenge 페이지
    @GetMapping("/api/v1/challenge")
    public String challengeHome(
            @RequestParam(value = "category", defaultValue = "ALL") Bootchallenge.ChallengeCategory category,
            Model model
    ) {
        List<Bootchallenge> challenges = challengeService.getChallengesByCategory(category);
        List<Bootchallenge> participatingChallenges = challengeService.getParticipatingChallenges();
        model.addAttribute("participatingChallenges", participatingChallenges);
        model.addAttribute("selectedCategory", category);
        return "challengehome";
    }


    //만드는 페이지
    @GetMapping("/api/v1/challenge/create")
    public String challengeWriteForm() {
        return "challengewrite";
    }

    @PostMapping("/api/v1/challenge/createpro")
    public String challengeCreatePro(
            Bootchallenge bootchallenge,
            @RequestParam("file") MultipartFile file,
            Model model
    ) throws Exception {
        challengeService.create(bootchallenge, file);
        model.addAttribute("message", "글 작성이 완료되었습니다.");
        model.addAttribute("searchUrl", "/api/v1/challenge");
        return "message";
    }

    //챌린지 1차 상세 설명
    @GetMapping("/api/v1/challenge/view/{id}")
    public String challengeBriefView(
            @PathVariable("id") Integer id,
            Model model
    ) {
        Bootchallenge bootchallenge = challengeService.getChallengeBrief(id);
        model.addAttribute("bootchallenge", bootchallenge);
        return "challengeBriefView";
    }

    //챌린지 2차 상세 설명
    @GetMapping("/api/v1/challenge/view/detail/{id}")
    public String challengeDetailView(
            @PathVariable("id") Integer id,
            Model model
    ) {
        Bootchallenge bootchallenge = challengeService.getChallengeDetail(id);
        model.addAttribute("bootchallenge", bootchallenge);
        return "challengeDetailView";
    }

    @GetMapping("/api/v1/challenge/view/{id}")
    public String challengeView(
            @PathVariable("id") Integer id,
            Model model
    ) {
        Bootchallenge bootchallenge = challengeService.challengeView(id);
        model.addAttribute("bootchallenge", bootchallenge);
        return "challengeview";
    }



    //챌린지 삭제
    @GetMapping("/api/v1/challenge/delete/{id}")
    public String challengeDelete(@PathVariable("id") Integer id) {
        challengeService.challengeDelete(id);
        return "redirect:/api/v1/challenge/list";
    }

    //챌린지 수정
    @PostMapping("/api/v1/challenge/update/{id}")
    public String challengeUpdate(
            @PathVariable("id") Integer id,
            Bootchallenge bootchallenge,
            @RequestParam("file") MultipartFile file
    ) throws Exception {
        // 기존 글을 가져옴
        Bootchallenge existingChallenge = challengeService.challengeView(id);
        existingChallenge.setTitle(bootchallenge.getTitle());
        existingChallenge.setContent(bootchallenge.getContent());

        // 수정한 내용 덮어씌움
        challengeService.update(existingChallenge);

        return "redirect:/api/v1/challenge";
    }

    //챌린지 참여시 참여수를 증가시킴
    @PostMapping("/api/v1/challenge/participate/{id}")
    public String participateChallenge(@PathVariable("id") Integer id) {
        challengeService.incrementParticipants(id);
        return "redirect:/api/v1/challenge/view/" + id;
    }

    //완료한 챌린지 보여주는 페이지
    @GetMapping("/api/v1/challenge/list/completed")
    public String completedChallenges(Model model) {
        List<Bootchallenge> completedChallenges = challengeService.getCompletedChallenges();
        model.addAttribute("challenges", completedChallenges);
        return "participatingchallenges";
    }

    @GetMapping("/api/v1/challenge/participate/conform/{id}")
    public String challengeConformForm(@PathVariable("id") Integer id, Model model) {
        Bootchallenge challenge = challengeService.challengeView(id);
        model.addAttribute("challenge", challenge);
        return "challengeConformForm"; // 설명 및 사진을 첨부할 수 있는 폼을 보여주는 뷰
    }

    @PostMapping("/api/v1/challenge/participate/conform")
    public String challengeConform(
            @RequestParam("id") Integer id,
            @RequestParam("file1") MultipartFile file1,
            @RequestParam("file2") MultipartFile file2,
            @RequestParam("file3") MultipartFile file3,
            Model model
    ) throws Exception {
        challengeService.conformChallenge(id, file1, file2, file3);
        model.addAttribute("message", "참여 내용이 성공적으로 저장되었습니다.");
        model.addAttribute("searchUrl", "/api/v1/challenge/list");
        return "message";
    }
}
