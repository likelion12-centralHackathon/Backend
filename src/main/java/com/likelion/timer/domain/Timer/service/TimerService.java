package com.likelion.timer.domain.Timer.service;

import static com.likelion.timer.domain.Timer.constants.TimerConstants.*;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.likelion.timer.domain.Timer.domain.entity.PartList;
import com.likelion.timer.domain.Timer.domain.entity.Timer;
import com.likelion.timer.domain.Timer.domain.repository.TimerRepository;
import com.likelion.timer.domain.Timer.dto.req.TimerReqDto;
import com.likelion.timer.domain.Timer.dto.res.TimerResDto;
import com.likelion.timer.domain.Timer.error.TimerErrorCode;
import com.likelion.timer.domain.User.domain.T_User;
import com.likelion.timer.domain.User.domain.T_UserRepository;
import com.likelion.timer.global.error.exception.AppException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TimerService {
	private final T_UserRepository userRepository;
	private final TimerRepository timerRepository;
	private final PartListService partListService;

	private static final Random RANDOM = new Random();

	@Transactional
	public TimerResDto addTimer(TimerReqDto timerReqDto) {
		// 사용자 찾기
		T_User user = T_User.builder().name("name").build();
		userRepository.save(user);

		// partList 설정
		List<PartList> partLists;

		if (timerReqDto.getIsSettingByUser()) { // 직접 설정인 경우
			if (timerReqDto.getParts() == null) { // 스틑레칭 부위가 안 온 경우
				throw new AppException(TimerErrorCode.INVALID_PART_LIST_BY_USER);
			}

			partLists = partListService.saveParts(timerReqDto.getParts());

		} else { // 랜덤인 경우
			if (timerReqDto.getParts() != null) { // 스틑레칭 부위가 온 경우
				throw new AppException(TimerErrorCode.INVALID_PART_LIST_BY_SYSTEM);
			}

			partLists = partListService.saveRandomUniqueParts(RANDOM.nextInt(MAX_STRETCHING_TIMES) + 1);
		}

		// name 설정
		String name;
		if (timerReqDto.getIsPermanent() && timerReqDto.getName() == null) {
			throw new AppException(TimerErrorCode.TIMER_NAME_REQUIRED);
		} else if (!timerReqDto.getIsPermanent()) {
			name = null;
		} else {
			Optional<Timer> timer = timerRepository.findByName(timerReqDto.getName());
			if (timer.isEmpty()) {
				name = timerReqDto.getName();
			} else {
				throw new AppException(TimerErrorCode.INVALID_TIMER_NAME);
			}
		}

		// timer 객체 생성
		Timer timer = Timer.builder()
			.user(user)
			.name(name)
			.cycle(timerReqDto.getCycle())
			.isPermanent(timerReqDto.getIsPermanent())
			.isSettingByUser(timerReqDto.getIsSettingByUser())
			.partLists(partLists)
			.build();

		// timer 저장
		timerRepository.save(timer);

		return TimerResDto.fromEntity(timer);
	}

}
