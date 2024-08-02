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
import com.likelion.timer.domain.Timer.dto.req.PartReqDto;
import com.likelion.timer.domain.Timer.dto.req.TimerReqDto;
import com.likelion.timer.domain.Timer.dto.req.TimerUpdateReqDto;
import com.likelion.timer.domain.Timer.dto.res.TimerListResDto;
import com.likelion.timer.domain.Timer.dto.res.TimerResDto;
import com.likelion.timer.domain.Timer.error.TimerErrorCode;
import com.likelion.timer.domain.User.domain.T_User;
import com.likelion.timer.domain.User.domain.T_UserRepository;
import com.likelion.timer.domain.model.TimerStateTypeEnum;
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

	@Transactional(readOnly = true)
	public TimerResDto getTimer(Long timerId) {
		// TODO user 연결
		Timer timer = timerRepository.findById(timerId)
			.orElseThrow(() -> new AppException(TimerErrorCode.TIMER_NOT_FOUND));

		return TimerResDto.fromEntity(timer);
	}

	@Transactional(readOnly = true)
	public List<TimerListResDto> getTimerList() {
		// TODO user 연결
		return timerRepository.findPermanentTimersNameAndIdByUserId(1L);
	}

	@Transactional
	public void updateTimerState(Long timerId, TimerStateTypeEnum timerState) {
		// 사용자 찾기
		T_User user = T_User.builder().name("name").build();
		userRepository.save(user);

		Timer timer = timerRepository.findById(timerId)
			.orElseThrow(() -> new AppException(TimerErrorCode.TIMER_NOT_FOUND));

		timer.changedTimerState(timerState);

		if (!timer.getIsPermanent() && timerState.equals(TimerStateTypeEnum.TIMER_DESTROY)) {
			timerRepository.delete(timer);
		} else {
			timerRepository.save(timer);
		}
	}

	@Transactional
	public void deleteTimer(Long timerId) {
		Timer timer = timerRepository.findById(timerId)
			.orElseThrow(() -> new AppException(TimerErrorCode.TIMER_NOT_FOUND));
		timerRepository.delete(timer);
	}

	@Transactional
	public TimerResDto updateTimer(Long timerId, TimerUpdateReqDto timerUpdateReqDto) {
		// 사용자 찾기
		T_User user = T_User.builder().name("name").build();
		userRepository.save(user);

		Timer timer = timerRepository.findById(timerId)
			.orElseThrow(() -> new AppException(TimerErrorCode.TIMER_NOT_FOUND));

		// name 중복 체크
		if (!timer.getName().equals(timerUpdateReqDto.getName())) {
			checkUniqueName(user.getId(), timerUpdateReqDto.getName());
		}

		// partList 설정
		List<PartList> partLists = checkPartLists(timerUpdateReqDto.getIsSettingByUser(), timerUpdateReqDto.getParts());

		// timer 객체 생성
		timer.updateTimer(timerUpdateReqDto, partLists);

		// timer 저장
		timerRepository.save(timer);

		return TimerResDto.fromEntity(timer);
	}

	@Transactional
	public TimerResDto addTimer(TimerReqDto timerReqDto) {
		// 사용자 찾기
		T_User user = T_User.builder().name("name").build();
		userRepository.save(user);

		// name 설정
		String name = checkName(timerReqDto.getIsPermanent(), user.getId(), timerReqDto.getName());

		// partList 설정
		List<PartList> partLists = checkPartLists(timerReqDto.getIsSettingByUser(), timerReqDto.getParts());

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

	private List<PartList> checkPartLists(Boolean isSettingByUser, List<PartReqDto> parts) {
		List<PartList> partLists;

		if (isSettingByUser) { // 직접 설정인 경우
			if (parts == null) { // 스틑레칭 부위가 안 온 경우
				throw new AppException(TimerErrorCode.INVALID_PART_LIST_BY_USER);
			}

			partLists = partListService.saveParts(parts);

		} else { // 랜덤인 경우
			if (parts != null) { // 스틑레칭 부위가 온 경우
				throw new AppException(TimerErrorCode.INVALID_PART_LIST_BY_SYSTEM);
			}

			partLists = partListService.saveRandomUniqueParts(RANDOM.nextInt(MAX_STRETCHING_TIMES) + 1);
		}

		return partLists;
	}

	private String checkName(Boolean isPermanent, Long userId, String name) {
		if (isPermanent && name == null) {
			throw new AppException(TimerErrorCode.TIMER_NAME_REQUIRED);
		} else if (isPermanent && checkUniqueName(userId, name)) {
			return name;
		} else {
			return null;
		}
	}

	private Boolean checkUniqueName(Long userId, String name) {
		Optional<Timer> timer = timerRepository.findByUserIdAndName(userId, name);
		if (timer.isEmpty()) {
			return true;
		} else {
			throw new AppException(TimerErrorCode.INVALID_TIMER_NAME);
		}
	}

}
