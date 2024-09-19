package com.likelion.timer.challenge.controller;

import java.util.List;

import com.likelion.timer.challenge.DTO.*;
import com.likelion.timer.challenge.domain.entity.Category;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.likelion.timer.challenge.domain.entity.Challenge;
import com.likelion.timer.challenge.service.ChallengeService;
import com.likelion.timer.global.common.ResponseDto;

@RestController
@RequestMapping("/api/v1/challenge")
public class ChallengeController {

	@Autowired
	private ChallengeService challengeService;

	// 챌린지 생성
	@PostMapping("/create")
	public Challenge createChallenge(Authentication authentication, @RequestBody ChallengeCreateDto challengeCreateDto) {

		UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		return challengeService.saveChallenge(userDetails.getUsername(), challengeCreateDto);
	}

	// 현재 사용자의 진행 중인 챌린지들 조회
	@GetMapping("/inprogress")
	public List<ChallengeOverviewDto> getInProgressChallenges(Authentication authentication) {
		UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		return challengeService.getInProgressChallenges(userDetails.getUsername());
	}

	// 특정 카테고리의 챌린지들 조회
	@GetMapping("/category/{category}")
	public List<ChallengeOverviewDto> getChallengesByCategory(@PathVariable(name="category") Category category) {
		return challengeService.getChallengesByCategory(category);
	}

	// 완료한 챌린지들 조회
	@GetMapping("/completed")
	public List<ChallengeOverviewDto> getCompletedChallenges(Authentication authentication) {
		UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		return challengeService.getCompletedChallenges(userDetails.getUsername());
	}

	// 챌린지 상세 보기
	@GetMapping("/view/{challengeId}")
	public ChallengeOverviewDto viewChallengeDetail(@PathVariable(name="challengeId") Long challengeId) {
		// 조회수 증가
		challengeService.incrementViewCount(challengeId);

		// 챌린지 상세 정보 반환
		return challengeService.viewChallengeDetail(challengeId);
	}


	// 챌린지 참가 신청
	@GetMapping("/create/{challengeId}")
	public ResponseEntity<ResponseDto> saveChallengeParticipant(Authentication authentication, @PathVariable(name="challengeId") Long challengeId) {
		UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		challengeService.saveChallengeParticipant(userDetails.getUsername(),challengeId);
		return ResponseEntity.status(201).body(ResponseDto.of(201, "챌린지 참가 신청이 완료되었습니다."));
	}

	// 챌린지 인증 사진 업로드
	@PostMapping("/certification/upload")
	public ResponseEntity<ResponseDto> uploadCertification(@RequestBody ChallengeCertificationDto certificationDto, Authentication authentication) {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		challengeService.uploadCertification(userDetails.getUsername(), certificationDto);
		return ResponseEntity.status(201).body(ResponseDto.of(201, "챌린지 참여 완료"));
	}


	//조회수 상위 3개
	@GetMapping("/top3")
	public ResponseEntity<List<Challengetop3DTO>> getTop3Challenges(){
		List<Challengetop3DTO> top3Challenges=challengeService.getTop3ChallengesByViewCount();
		return ResponseEntity.status(200).body(top3Challenges);
	}

	//챌린지 삭제
	@DeleteMapping("/{challengeId}")
	public ResponseEntity<ResponseDto>deleteChallenge(Authentication authentication, @PathVariable(name="challengeId") Long challengeId){
		UserDetails userDetails=(UserDetails)authentication.getPrincipal();
		challengeService.deleteChallenge(userDetails.getUsername(), challengeId);
		return ResponseEntity.ok(ResponseDto.of(200, "삭제 성공"));
	}

	//챌린지 업데이트
	@PostMapping("/update/{challengeId}")
	public ResponseEntity<ResponseDto>updateChallenge(Authentication authentication, @PathVariable(name="challengeId") Long challengeId,@RequestBody @Valid ChallengeUpdateReqDto challengeUpdateReqDto){
		UserDetails userDetails=(UserDetails)authentication.getPrincipal();
		challengeService.updateChallenge(userDetails.getUsername(), challengeId, challengeUpdateReqDto);
		return ResponseEntity.ok(ResponseDto.of(200, "수정 성공"));
	}

	// 모든 챌린지 조회 API
	@GetMapping("/list")
	public List<ChallengeOverviewDto> getAllChallenges() {
		return challengeService.getAllChallenges();
	}



}
