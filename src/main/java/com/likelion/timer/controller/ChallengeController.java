package com.likelion.timer.controller;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import com.likelion.timer.challenge.entity.Bootchallenge;
import com.likelion.timer.challenge.service.ChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Controller
@RequestMapping("/challenge")
public class ChallengeController {

    @Autowired
    private ChallengeService challengeService;



    @GetMapping("/challenge/create")
    public String challengeWriteForm() {
        return "challengewrite";
    }

    @PostMapping("/challenge/createpro")
    public String challengeCreatePro(
            Bootchallenge bootchallenge,
            @RequestParam("file") MultipartFile file,
            Model model
    ) throws Exception {
        challengeService.create(bootchallenge, file);
        model.addAttribute("message", "글 작성이 완료되었습니다.");
        model.addAttribute("searchUrl", "/challenge/list");
        return "message";
    }

    @GetMapping("/challenge/view/{id}")
    public String challengeView(
            @PathVariable("id") Integer id,
            Model model
    ) {
        Bootchallenge bootchallenge = challengeService.challengeView(id);
        model.addAttribute("bootchallenge", bootchallenge);
        return "challengeview";
    }

    @GetMapping("/challenge/delete/{id}")
    public String challengeDelete(@PathVariable("id") Integer id) {
        challengeService.challengeDelete(id);
        return "redirect:/challenge/list";
    }

    @PostMapping("/challenge/update/{id}")
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

        return "redirect:/challenge/list";
    }

    @PostMapping("/challenge/participate/{id}")
    public String participateChallenge(@PathVariable("id") Integer id) {
        challengeService.incrementParticipants(id);
        return "redirect:/challenge/view/" + id;
    }

    @GetMapping("/challenge/list/participating")
    public String participatingChallenges(Model model) {
        List<Bootchallenge> participatingChallenges = challengeService.getParticipatingChallenges();
        model.addAttribute("challenges", participatingChallenges);
        return "participatingchallenges";
    }
}
