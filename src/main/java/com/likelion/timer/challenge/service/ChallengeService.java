package com.likelion.timer.challenge.service;

import static com.likelion.timer.challenge.error.ChallengeErrorCode.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.likelion.timer.challenge.DTO.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.likelion.timer.challenge.domain.entity.Category;
import com.likelion.timer.challenge.domain.entity.Challenge;
import com.likelion.timer.challenge.domain.entity.ChallengeParticipant;
import com.likelion.timer.challenge.domain.repository.ChallengeParticipantRepository;
import com.likelion.timer.challenge.domain.repository.ChallengeRepository;
import com.likelion.timer.challenge.error.ChallengeErrorCode;
import com.likelion.timer.global.error.GlobalErrorCode;
import com.likelion.timer.global.error.exception.AppException;
import com.likelion.timer.login.constant.AuthType;
import com.likelion.timer.user.model.User;
import com.likelion.timer.user.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ChallengeService {

	@Autowired
	private ChallengeRepository challengeRepository;

	@Autowired
	private ChallengeParticipantRepository challengeParticipantRepository;

	@Autowired
	private UserRepository userRepository;

	// 생성
	@Transactional
	public Challenge saveChallenge(String userId, ChallengeCreateDto challengeCreateDto) {
		// userId를 기준으로 사용자를 조회
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new AppException(GlobalErrorCode.USER_NOT_FOUND));

		Challenge challenge = new Challenge();
		challenge.setUser(user);
		challenge.setImgUrl(challengeCreateDto.getImgUrl());
		challenge.setCategory(Category.valueOf(challengeCreateDto.getCategory().toUpperCase()));
		challenge.setTitle(challengeCreateDto.getTitle());
		challenge.setContent(challengeCreateDto.getContent());
		challenge.setAuthMethod(challengeCreateDto.getAuthMethod());
		challenge.setEndDate(convertStringToDate(challengeCreateDto.getEndDate()));
		challenge.setNote(challengeCreateDto.getNote());
		return challengeRepository.save(challenge);
	}

	// 문자열을 Date로 변환하는 유틸리티 메서드
	private Date convertStringToDate(String dateString) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			return formatter.parse(dateString);
		} catch (ParseException e) {
			throw new AppException(GlobalErrorCode.INVALID_DATE_FORMAT);
		}
	}

	//챌린지의 조회수 1 증가
	public void incrementViewCount(Long challengeId) {
		Challenge challenge = challengeRepository.findById(challengeId)
				.orElseThrow(() -> new AppException(CHALLENGE_NOT_EXIST));

		challenge.setViewCount(challenge.getViewCount() + 1);
		challengeRepository.save(challenge);
	}



	// 챌린지 세부 정보 조회
	public ChallengeOverviewDto viewChallengeDetail(Long challengeId) {
		// challengeId를 기준으로 데이터베이스에서 챌린지 정보를 찾음
		Challenge challenge = challengeRepository.findById(challengeId)
				.orElseThrow(() -> new AppException(ChallengeErrorCode.CHALLENGE_NOT_EXIST));

		// ChallengeOverviewDto 객체를 생성하고 조회한 챌린지의 정보를 설정
		ChallengeOverviewDto dto = new ChallengeOverviewDto(
				challenge.getId(), // Long 타입 ID
				challenge.getTitle(), // String 타입 제목
				challenge.getImgUrl(), // String 타입 이미지 URL
				challenge.getParticipantCount(), // int 타입 참가자 수
				challenge.getEndDate(), // Date 타입 종료일
				challenge.getAuthMethod(), // String 타입 인증 방법
				challenge.getNote(), // String 타입 노트
				challenge.getCategory(),
				challenge.getViewCount()
		);

		return dto;
	}

	// 챌린지 인증 사진 업로드 및 상태 업데이트
	public void uploadCertification(String userId, ChallengeCertificationDto certificationDto) {

		// userId를 기준으로 사용자를 조회
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new AppException(GlobalErrorCode.USER_NOT_FOUND));

		Optional<ChallengeParticipant> participantOpt = challengeParticipantRepository.findByIdAndUser(
			certificationDto.getChallengeId(), user);

		if (participantOpt.isEmpty()) {
			throw new AppException(ChallengeErrorCode.CHALLENGE_PARTICIPANT_NOT_EXIST);
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
	public List<ChallengeOverviewDto> getInProgressChallenges(String userId) {

		// userId를 기준으로 사용자를 조회
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new AppException(GlobalErrorCode.USER_NOT_FOUND));

		return challengeParticipantRepository.findByUserAndState(user, ChallengeParticipant.State.IN_PROGRESS)
			.stream()
			.map(participant -> {
				Challenge challenge = participant.getChallenge();
				return new ChallengeOverviewDto(challenge.getId(), challenge.getTitle(), challenge.getImgUrl(),
					challenge.getParticipantCount(), challenge.getEndDate(), challenge.getAuthMethod(), challenge.getNote(),challenge.getCategory(), challenge.getViewCount());
			})
			.collect(Collectors.toList());
	}

	

	// 특정 카테고리의 챌린지들 조회
	public List<ChallengeOverviewDto> getChallengesByCategory(Category category) {
		return challengeRepository.findByCategory(category)
			.stream()
			.map(challenge -> new ChallengeOverviewDto(challenge.getId(), challenge.getTitle(),
				challenge.getImgUrl(),
				challenge.getParticipantCount(), challenge.getEndDate(), challenge.getAuthMethod(), challenge.getNote(), challenge.getCategory(), challenge.getViewCount()))
			.collect(Collectors.toList());
	}

	// 완료한 챌린지들 조회
	public List<ChallengeOverviewDto> getCompletedChallenges(String userId) {
		// userId를 기준으로 사용자를 조회
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new AppException(GlobalErrorCode.USER_NOT_FOUND));

		// 해당 사용자가 완료한 챌린지들을 조회
		return challengeParticipantRepository.findByUserAndState(user, ChallengeParticipant.State.COMPLETED)
				.stream()
				.map(participant -> {
					Challenge challenge = participant.getChallenge();
					return new ChallengeOverviewDto(
							challenge.getId(),
							challenge.getTitle(),
							challenge.getImgUrl(),
							challenge.getParticipantCount(),
							challenge.getEndDate(),
							challenge.getAuthMethod(),
							challenge.getNote(),
							challenge.getCategory(),
							challenge.getViewCount()
					);
				})
				.collect(Collectors.toList());
	}


	// 사용자가 챌린지 참가 신청
	public void saveChallengeParticipant(String userId, Long challengeId) {
		// userId를 기준으로 사용자를 조회
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new AppException(GlobalErrorCode.USER_NOT_FOUND));


		Challenge challenge = challengeRepository.findById(challengeId)
			.orElseThrow(() -> new AppException(CHALLENGE_NOT_EXIST));

		ChallengeParticipant challengeParticipant = new ChallengeParticipant();
		challengeParticipant.setChallenge(challenge);
		challengeParticipant.setUser(user);
		challengeParticipant.setState(ChallengeParticipant.State.IN_PROGRESS);
		challengeParticipantRepository.save(challengeParticipant);
	}

	//조회수 상위 3개
	public List<Challengetop3DTO> getTop3ChallengesByViewCount() {
		Pageable topThree = PageRequest.of(0, 3);
		return challengeRepository.findTopByOrderByViewCountDesc(topThree);
	}


	//챌린지 삭제
	@Transactional
	public void deleteChallenge(String userId, Long challengeId){
		userRepository.findById(userId)
				.orElseThrow(() -> new AppException(GlobalErrorCode.USER_NOT_FOUND));
		Challenge challenge = challengeRepository.findById(challengeId)
				.orElseThrow(() -> new AppException(CHALLENGE_NOT_EXIST));

		// 챌린지 작성자가 현재 로그인한 사용자인지 확인
		if (!challenge.getUser().getId().equals(userId)) {
			throw new AppException(GlobalErrorCode.UNAUTHORIZED_ACTION); // 권한 없는 사용자 예외 처리
		}
		challengeRepository.delete(challenge);
	}

	//챌린지 수정
	@Transactional
	public void updateChallenge(String userId, Long challengeId, ChallengeUpdateReqDto challengeUpdateReqDto) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new AppException(GlobalErrorCode.USER_NOT_FOUND));

		Challenge challenge = challengeRepository.findById(challengeId)
				.orElseThrow(() -> new AppException(CHALLENGE_NOT_EXIST));

		if (!challenge.getUser().getId().equals(userId)) {
			throw new AppException(GlobalErrorCode.UNAUTHORIZED_ACTION);
		}
		challenge.setTitle(challengeUpdateReqDto.getTitle());
		challenge.setContent(challengeUpdateReqDto.getContent());
		challenge.setAuthMethod(challengeUpdateReqDto.getAuthMethod());
		challenge.setEndDate(challengeUpdateReqDto.getEndDate());
		challenge.setCategory(challengeUpdateReqDto.getCategory());
		challenge.setNote(challengeUpdateReqDto.getNote());
		challenge.setImgUrl(challengeUpdateReqDto.getImgUrl());

		challengeRepository.save(challenge);
	}

	// 모든 챌린지 리스트 조회
	public List<ChallengeOverviewDto> getAllChallenges() {
		return challengeRepository.findAll()
				.stream()
				.map(challenge -> new ChallengeOverviewDto(
						challenge.getId(),
						challenge.getTitle(),
						challenge.getImgUrl(),
						challenge.getParticipantCount(),
						challenge.getEndDate(),
						challenge.getAuthMethod(),
						challenge.getNote(),
						challenge.getCategory(),
						challenge.getViewCount()))
				.collect(Collectors.toList());
	}

}
