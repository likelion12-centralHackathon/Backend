package com.likelion.timer.domain.Timer.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.likelion.timer.domain.Timer.domain.entity.Part;
import com.likelion.timer.domain.Timer.domain.entity.RecordTimer;
import com.likelion.timer.domain.Timer.domain.entity.Timer;
import com.likelion.timer.domain.Timer.domain.repository.PartRepository;
import com.likelion.timer.domain.Timer.domain.repository.RecordTimerRepository;
import com.likelion.timer.domain.Timer.domain.repository.TimerRepository;
import com.likelion.timer.domain.Timer.dto.req.RecordTimerReqDto;
import com.likelion.timer.domain.Timer.dto.res.PartStaticResDto;
import com.likelion.timer.domain.Timer.dto.res.RecordTimerResDto;
import com.likelion.timer.domain.Timer.dto.res.TimerPartListResDto;
import com.likelion.timer.domain.Timer.error.RecordTimerErrorCode;
import com.likelion.timer.domain.Timer.error.TimerErrorCode;
import com.likelion.timer.domain.model.PartTypeEnum;
import com.likelion.timer.domain.model.RecordTimerStateTypeEnum;
import com.likelion.timer.domain.model.TimerStateTypeEnum;
import com.likelion.timer.global.error.GlobalErrorCode;
import com.likelion.timer.global.error.exception.AppException;
import com.likelion.timer.global.util.DateUtil;
import com.likelion.timer.user.model.User;
import com.likelion.timer.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class RecordTimerService {
	private final RecordTimerRepository recordTimerRepository;
	private final TimerRepository timerRepository;
	private final UserRepository userRepository;
	private final PartRepository partRepository;
	private final DateUtil dateUtil;

	@Transactional(readOnly = true)
	public List<TimerPartListResDto> getPartListsByDate(String userId, LocalDate date) {

		// 존재하는 유저인지 확인
		userRepository.findById(userId).orElseThrow(() -> new AppException(GlobalErrorCode.USER_NOT_FOUND));

		// 일자 별 사용 타이머 리스트
		List<Timer> timers = recordTimerRepository.findDistinctTimerIdsByUserIdAndRegTimeBetween(
			userId, date.atStartOfDay(), date.plusDays(1).atStartOfDay());

		return timers.stream()
			.map(TimerPartListResDto::fromEntity)
			.toList();
	}

	@Transactional(readOnly = true)
	public List<PartStaticResDto> getPartStaticsByDate(String userId, LocalDate date) {

		// 존재하는 유저인지 확인
		userRepository.findById(userId).orElseThrow(() -> new AppException(GlobalErrorCode.USER_NOT_FOUND));

		// 일자 별 사용 타이머 리스트
		List<PartStaticResDto> partStaticResDtos = recordTimerRepository.findPartStaticResDtosByUserAndRegTimeBetween(
			userId, date.atStartOfDay(), date.plusDays(1).atStartOfDay());

		return partStaticResDtos;
	}

	@Transactional
	public RecordTimerResDto addRecordTimer(String userId, RecordTimerReqDto recordTimerReqDto) {
		// 존재하는 타입을 준 것인지 검사
		RecordTimerStateTypeEnum recordTimerStateTypeEnum = RecordTimerStateTypeEnum.findByKey(
			recordTimerReqDto.getTimerStateType());

		// 타이머 있는지 검사
		Timer timer = timerRepository.findById(recordTimerReqDto.getTimerId())
			.orElseThrow(() -> new AppException(TimerErrorCode.TIMER_NOT_FOUND));

		// 유저 있는 유저인지 검사
		User user = userRepository.findById(userId).orElseThrow(() -> new AppException(GlobalErrorCode.USER_NOT_FOUND));

		// 이전에 기록된 timer 값
		RecordTimer beforeRecordTimer;

		// 응답값
		RecordTimerResDto recordTimerResDto;

		switch (recordTimerStateTypeEnum) {
			case TIMER_START:
				recordTimerResDto = handleTimerStart(timer, user);
				break;

			case TIMER_STOP:
				beforeRecordTimer = recordTimerRepository.findTopByTimerIdAndUserIdOrderByRegTimeDesc(
						recordTimerReqDto.getTimerId(), userId)
					.orElseThrow(() -> new AppException(TimerErrorCode.RECORD_TIMER_NOT_FOUND));

				recordTimerResDto = handleTimerStop(timer, user, beforeRecordTimer);
				break;

			case TIMER_DESTROY:
				beforeRecordTimer = recordTimerRepository.findTopByTimerIdAndUserIdOrderByRegTimeDesc(
						recordTimerReqDto.getTimerId(), userId)
					.orElseThrow(() -> new AppException(TimerErrorCode.RECORD_TIMER_NOT_FOUND));
				recordTimerResDto = handleTimerDestroy(timer, user, beforeRecordTimer);
				break;

			case TIMER_RESTART:
				beforeRecordTimer = findRecordTimerForStop(recordTimerReqDto.getTimerId(), userId);
				recordTimerResDto = handleTimerRestart(timer, user, beforeRecordTimer);
				break;

			case STRETCHING_DONE:
				beforeRecordTimer = recordTimerRepository.findTopByTimerIdAndUserIdOrderByRegTimeDesc(
						recordTimerReqDto.getTimerId(), userId)
					.orElseThrow(() -> new AppException(TimerErrorCode.RECORD_TIMER_NOT_FOUND));
				recordTimerResDto = handleStretchingDone(timer, user,
					PartTypeEnum.findByKey(recordTimerReqDto.getPartType()),
					beforeRecordTimer);
				break;

			case STRETCHING_START:
				beforeRecordTimer = recordTimerRepository.findTopByTimerIdAndUserIdOrderByRegTimeDesc(
						recordTimerReqDto.getTimerId(), userId)
					.orElseThrow(() -> new AppException(TimerErrorCode.RECORD_TIMER_NOT_FOUND));
				recordTimerResDto = handleStretchingStart(timer, user,
					PartTypeEnum.findByKey(recordTimerReqDto.getPartType()),
					beforeRecordTimer);
				break;

			default:
				throw new AppException(TimerErrorCode.INVALID_TIMER_STATE);
		}

		return recordTimerResDto;
	}

	private RecordTimerResDto handleTimerStart(Timer timer, User user) {
		// 이미 돌아가는 타이머인지 확인
		if (timer.getTimerState().equals(TimerStateTypeEnum.TIMER_RUN)) {
			throw new AppException(TimerErrorCode.INVALID_TIMER_TO_CHANGE_RUN);
		}

		// 타이머 시작 기록 생성
		RecordTimer recordTimer = recordTimerRepository.save(RecordTimer.builder()
			.user(user)
			.timer(timer)
			.developTime(0L)
			.stretchingTime(0L)
			.timerStateType(RecordTimerStateTypeEnum.TIMER_START)
			.part(null)
			.build());

		// 타이머 상태를 RUN으로 변경
		timer.changedTimerState(TimerStateTypeEnum.TIMER_RUN);

		timerRepository.save(timer);
		return RecordTimerResDto.fromEntity(recordTimer);
	}

	private RecordTimerResDto handleTimerStop(Timer timer, User user, RecordTimer beforeRecordTimer) {
		validateTimerRunning(timer.getTimerState());
		validateTimerStateIsStretchingStart(beforeRecordTimer.getTimerStateType());

		// 타이머 멈춤 기록 생성
		RecordTimer recordTimer = recordTimerRepository.save(RecordTimer.builder()
			.user(user)
			.timer(timer)
			.developTime(
				beforeRecordTimer.getDevelopTime() + dateUtil.getDurationBetweenNow(beforeRecordTimer.getRegTime()))
			.stretchingTime(0L)
			.timerStateType(RecordTimerStateTypeEnum.TIMER_STOP)
			.part(null)
			.build());

		return RecordTimerResDto.fromEntity(recordTimer);
	}

	private RecordTimerResDto handleTimerDestroy(Timer timer, User user, RecordTimer beforeRecordTimer) {
		validateTimerRunning(timer.getTimerState());
		validateTimerStateIsStretchingStart(beforeRecordTimer.getTimerStateType());

		// 개발 시간 체크
		Long developTime = checkRecordTimerStateIsStop(beforeRecordTimer.getTimerStateType(),
			beforeRecordTimer.getDevelopTime(), beforeRecordTimer.getRegTime());

		// 타이머 종료 기록 남기기
		RecordTimer recordTimer = recordTimerRepository.save(RecordTimer.builder()
			.user(user)
			.timer(timer)
			.developTime(developTime)
			.stretchingTime(0L)
			.timerStateType(RecordTimerStateTypeEnum.TIMER_DESTROY)
			.part(null)
			.build());

		// 타이머 상태 종료로 변경
		timer.changedTimerState(TimerStateTypeEnum.TIMER_NOT_RUN);

		// 임시 저장 타이머라면 삭제
		if (!timer.getIsPermanent()) {
			timerRepository.delete(timer);
		}

		return RecordTimerResDto.fromEntity(recordTimer);
	}

	private RecordTimerResDto handleTimerRestart(Timer timer, User user, RecordTimer beforeRecordTimer) {
		validateTimerRunning(timer.getTimerState());
		validateTimerStateIsStretchingStart(beforeRecordTimer.getTimerStateType());

		// 타이머 재시작 기록 생성
		RecordTimer recordTimer = recordTimerRepository.save(RecordTimer.builder()
			.user(user)
			.timer(timer)
			.developTime(beforeRecordTimer.getDevelopTime())
			.stretchingTime(0L)
			.timerStateType(RecordTimerStateTypeEnum.TIMER_RESTART)
			.part(null)
			.build());

		return RecordTimerResDto.fromEntity(recordTimer);
	}

	private RecordTimerResDto handleStretchingDone(Timer timer, User user, PartTypeEnum partType,
		RecordTimer beforeRecordTimer) {
		validateTimerRunning(timer.getTimerState());

		// todo partypeenum 데이터 있는 지 확인하기 > nullexception 날리기
		if (!beforeRecordTimer.getTimerStateType().equals(RecordTimerStateTypeEnum.STRETCHING_START)) {
			if (!beforeRecordTimer.getPart().equals(partType)) {
				throw new AppException(RecordTimerErrorCode.INVALID_STRETCHING_DONE_PART);
			}
			throw new AppException(RecordTimerErrorCode.INVALID_STRETCHING_DONE_STATE);
		}

		// 파트 찾아서 넣기
		Part part = partRepository.findByPartType(partType)
			.orElseThrow(() -> new AppException(TimerErrorCode.INVALID_PART_TYPE));

		// 스트레칭 완료 기록 생성
		RecordTimer recordTimer = recordTimerRepository.save(RecordTimer.builder()
			.user(user)
			.timer(timer)
			.developTime(beforeRecordTimer.getDevelopTime())
			.stretchingTime(
				beforeRecordTimer.getStretchingTime() + dateUtil.getDurationBetweenNow(beforeRecordTimer.getRegTime()))
			.timerStateType(RecordTimerStateTypeEnum.STRETCHING_DONE)
			.part(part)
			.build());

		return RecordTimerResDto.fromEntity(recordTimer);
	}

	private RecordTimerResDto handleStretchingStart(Timer timer, User user, PartTypeEnum partType,
		RecordTimer beforeRecordTimer) {
		validateTimerStateIsStretchingStart(beforeRecordTimer.getTimerStateType());

		// 개발 시간 체크
		Long developTime = checkRecordTimerStateIsStop(beforeRecordTimer.getTimerStateType(),
			beforeRecordTimer.getDevelopTime(), beforeRecordTimer.getRegTime());

		// 파트 찾아서 넣기
		Part part = partRepository.findByPartType(partType)
			.orElseThrow(() -> new AppException(TimerErrorCode.INVALID_PART_TYPE));

		// 스트레칭 시작 기록 생성
		RecordTimer recordTimer = recordTimerRepository.save(RecordTimer.builder()
			.user(user)
			.timer(timer)
			.developTime(developTime)
			.stretchingTime(0L)
			.timerStateType(RecordTimerStateTypeEnum.STRETCHING_START)
			.part(part)
			.build());

		return RecordTimerResDto.fromEntity(recordTimer);
	}

	private void validateTimerRunning(TimerStateTypeEnum timerStateTypeEnum) {
		if (!timerStateTypeEnum.equals(TimerStateTypeEnum.TIMER_RUN)) {
			throw new AppException(TimerErrorCode.TIMER_NOT_RUN);
		}
	}

	private void validateTimerStateIsStretchingStart(RecordTimerStateTypeEnum recordTimerStateType) {
		if (recordTimerStateType.equals(RecordTimerStateTypeEnum.STRETCHING_START)) {
			throw new AppException(TimerErrorCode.INVALID_STRETCHING_TO_TIMER);
		}
	}

	private Long checkRecordTimerStateIsStop(RecordTimerStateTypeEnum recordTimerStateType, Long beforeDevelopTime,
		LocalDateTime regTime) {
		if (recordTimerStateType.equals(RecordTimerStateTypeEnum.TIMER_STOP)) {
			return beforeDevelopTime;
		}
		return beforeDevelopTime + dateUtil.getDurationBetweenNow(regTime);
	}

	private RecordTimer findRecordTimerForStop(Long timerId, String userId) {
		// befor record 가져오기
		RecordTimer beforeRecordTimer = recordTimerRepository.findTopByTimerIdAndUserIdOrderByRegTimeDesc(
				timerId, userId)
			.orElseThrow(() -> new AppException(TimerErrorCode.RECORD_TIMER_NOT_FOUND));

		if (beforeRecordTimer.getTimerStateType().equals(RecordTimerStateTypeEnum.TIMER_STOP)) {
			return beforeRecordTimer;
		} else if (beforeRecordTimer.getTimerStateType().equals(RecordTimerStateTypeEnum.TIMER_START) ||
			beforeRecordTimer.getTimerStateType().equals(RecordTimerStateTypeEnum.TIMER_RESTART)) {
			throw new AppException(TimerErrorCode.INVALID_TIMER_RESTART);
		} else { // 이전에 멈춤 상태가 아니고, 재시작 상태도 아니라면
			// stop Record 가져오기
			RecordTimer stopRecord = recordTimerRepository.findTopByTimerIdAndUserIdAndTimerStateTypeOrderByRegTimeDesc(
				timerId, userId, RecordTimerStateTypeEnum.TIMER_STOP).orElse(null);

			// stop Record 가 있고
			if (stopRecord != null) {
				RecordTimer startRecord = recordTimerRepository.findTopByTimerIdAndUserIdAndTimerStateTypeOrderByRegTimeDesc(
					timerId, userId, RecordTimerStateTypeEnum.TIMER_START).orElse(null);

				// start Record 가 비어있지 않은 상태에서
				// start 의 기록 시간이 stop 이 기록된 시간보다 전일 때,
				// 그리고 restart도 기록 시간이 stop 보다 전일 때,
				if (startRecord != null && startRecord.getRegTime().isBefore(stopRecord.getRegTime())) {
					return stopRecord;
				} else {
					throw new AppException(RecordTimerErrorCode.INVALID_TIMER_RESTART);
				}
			} else {
				throw new AppException(RecordTimerErrorCode.INVALID_TIMER_RESTART);
			}
		}
	}
}
