package com.likelion.timer.domain.Timer.service;

import static com.likelion.timer.domain.Timer.constants.TimerConstants.*;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.likelion.timer.domain.Timer.domain.entity.PartList;
import com.likelion.timer.domain.Timer.domain.entity.RecordTimer;
import com.likelion.timer.domain.Timer.domain.entity.Timer;
import com.likelion.timer.domain.Timer.domain.repository.RecordTimerRepository;
import com.likelion.timer.domain.Timer.domain.repository.TimerRepository;
import com.likelion.timer.domain.Timer.dto.req.PartReqDto;
import com.likelion.timer.domain.Timer.dto.req.TimerReqDto;
import com.likelion.timer.domain.Timer.dto.req.TimerUpdateReqDto;
import com.likelion.timer.domain.Timer.dto.res.TimerListResDto;
import com.likelion.timer.domain.Timer.dto.res.TimerResDto;
import com.likelion.timer.domain.Timer.error.TimerErrorCode;
import com.likelion.timer.domain.Timer.vo.MessageVo;
import com.likelion.timer.domain.model.RecordTimerStateTypeEnum;
import com.likelion.timer.domain.model.TimerStateTypeEnum;
import com.likelion.timer.global.error.GlobalErrorCode;
import com.likelion.timer.global.error.exception.AppException;
import com.likelion.timer.global.util.DateUtil;
import com.likelion.timer.global.util.FCMUtil;
import com.likelion.timer.user.model.User;
import com.likelion.timer.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TimerService {
	private final UserRepository userRepository;
	private final TimerRepository timerRepository;
	private final RecordTimerRepository recordTimerRepository;
	private final PartListService partListService;

	private final DateUtil dateUtil;
	private final FCMUtil fcmUtil;

	private static final Random RANDOM = new Random();

	@Transactional
	public void checkAndSendNotifications() {
		List<Timer> runningTimers = timerRepository.findByTimerState(TimerStateTypeEnum.TIMER_RUN);

		for (Timer timer : runningTimers) {
			// 가장 최신의 기록 찾기
			RecordTimer latestRecord = recordTimerRepository.findTopByTimerIdOrderByRegTimeDesc(timer.getId());

			// TODO stop > stretching start 된 경우, 기획 상으로 일어나지 않지만, 혹시 모르니 대비책 생각해보기

			// 기록이 있고, destroy 상태가 아니라면
			if (latestRecord != null && !latestRecord.getTimerStateType()
				.equals(RecordTimerStateTypeEnum.TIMER_DESTROY)) {
				if (latestRecord.getTimerStateType().equals(RecordTimerStateTypeEnum.TIMER_RESTART) ||
					latestRecord.getTimerStateType().equals(RecordTimerStateTypeEnum.TIMER_START) ||
					latestRecord.getTimerStateType().equals(RecordTimerStateTypeEnum.STRETCHING_DONE)) {

					// 지금까지 기록된 개발 시간
					long developTimeInSeconds = latestRecord.getDevelopTime();

					// 현재 시간과 비교해서 추가된 시간
					long elapsedTimeInSeconds = dateUtil.getDurationBetweenNow(latestRecord.getRegTime());

					// 최종 개발 시간
					long totalDevelopTime = developTimeInSeconds + elapsedTimeInSeconds;

					// 총 개발 시간이 주기에 걸치는 지 확인
					if (totalDevelopTime % timer.getCycle() == 0) {
						List<PartList> partLists = timer.getPartLists();
						// 몇 번째 주기인지 확인하기 위한 것
						int quotient = (int)(totalDevelopTime / timer.getCycle()) % partLists.size();

						String title = timer.getName() + "타이머 알림";

						String partListString;
						if (quotient < partLists.size()) {
							partListString = partLists.get((int)quotient).toString();
						} else {
							partListString = "해당 주기에 대한 파트 리스트가 없습니다.";
						}

						fcmUtil.sendMessage(new MessageVo(
							timer.getDeviceToken(),
							title,
							partListString
						));

					}
				}
			}
		}
	}

	@Transactional(readOnly = true)
	public TimerResDto getTimer(String userId, Long timerId) {
		userRepository.findById(userId)
			.orElseThrow(() -> new AppException(GlobalErrorCode.USER_NOT_FOUND));

		Timer timer = timerRepository.findByUserIdAndId(userId, timerId)
			.orElseThrow(() -> new AppException(TimerErrorCode.TIMER_NOT_FOUND));

		return TimerResDto.fromEntity(timer);
	}

	@Transactional(readOnly = true)
	public List<TimerListResDto> getTimerList(String userId) {
		userRepository.findById(userId)
			.orElseThrow(() -> new AppException(GlobalErrorCode.USER_NOT_FOUND));

		return timerRepository.findPermanentTimersNameAndIdByUserId(userId);
	}

	@Transactional
	public void deleteTimer(String userId, Long timerId) {
		userRepository.findById(userId)
			.orElseThrow(() -> new AppException(GlobalErrorCode.USER_NOT_FOUND));

		Timer timer = timerRepository.findByUserIdAndId(userId, timerId)
			.orElseThrow(() -> new AppException(TimerErrorCode.TIMER_NOT_FOUND));
		timerRepository.delete(timer);
	}

	@Transactional
	public void updateTimer(String userId, Long timerId, TimerUpdateReqDto timerUpdateReqDto) {

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new AppException(GlobalErrorCode.USER_NOT_FOUND));

		Timer timer = timerRepository.findByUserIdAndId(userId, timerId)
			.orElseThrow(() -> new AppException(TimerErrorCode.TIMER_NOT_FOUND));

		// 이름이 비어있는 경우
		if (timer.getName() == null) {
			throw new AppException(TimerErrorCode.INVALID_TIMER_NOT_SAVED);
		}

		// name 중복 체크
		if (!timer.getName().equals(timerUpdateReqDto.getName())) {
			checkUniqueName(user.getId(), timerUpdateReqDto.getName());
		}

		// partList 설정
		List<PartList> partLists = checkPartLists(timerUpdateReqDto.getIsSettingByUser(), timerUpdateReqDto.getParts());

		// 타이머 주기 설정
		long cycleInSeconds = Math.round(timerUpdateReqDto.getCycle() * 1800);

		// timer 객체 생성
		timer.updateTimer(timerUpdateReqDto.getName(), cycleInSeconds,
			timerUpdateReqDto.getIsSettingByUser(), partLists);

		// timer 저장
		timerRepository.save(timer);
	}

	@Transactional
	public void addTimer(String userId, TimerReqDto timerReqDto) {
		// 사용자 찾기
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new AppException(GlobalErrorCode.USER_NOT_FOUND));

		// name 설정
		String name = checkName(timerReqDto.getIsPermanent(), user.getId(), timerReqDto.getName());

		// partList 설정
		List<PartList> partLists = checkPartLists(timerReqDto.getIsSettingByUser(), timerReqDto.getParts());

		// 타이머 주기 설정
		long cycleInSeconds = Math.round(timerReqDto.getCycle() * 1800);

		// timer 객체 생성
		Timer timer = Timer.builder()
			.user(user)
			.name(name)
			.cycle(cycleInSeconds)
			.deviceToken(timerReqDto.getDeviceToken())
			.isPermanent(timerReqDto.getIsPermanent())
			.isSettingByUser(timerReqDto.getIsSettingByUser())
			.partLists(partLists)
			.build();

		// timer 저장
		timerRepository.save(timer);
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

	private String checkName(Boolean isPermanent, String userId, String name) {
		if (isPermanent && name == null) {
			throw new AppException(TimerErrorCode.TIMER_NAME_REQUIRED);
		} else if (isPermanent && checkUniqueName(userId, name)) {
			return name;
		} else {
			return null;
		}
	}

	private Boolean checkUniqueName(String userId, String name) {
		Optional<Timer> timer = timerRepository.findByUserIdAndName(userId, name);
		if (timer.isPresent()) {
			throw new AppException(TimerErrorCode.INVALID_TIMER_NAME);
		} else {
			return true;
		}
	}

	// 주기적인 작업을 수행하는 TimerTask 클래스
	private class TimerTask extends Thread {
		private final Long timerId;
		private final long cycleInMillis;
		private int cycleCount = 0;

		public TimerTask(Long timerId, long cycleInMillis) {
			this.timerId = timerId;
			this.cycleInMillis = cycleInMillis;
		}

		// FCM 알림을 위한 메소드
		private void sendFCMNotification(String userId, List<PartList> partLists) {
			// FCM 메시지 생성
			Message message = Message.builder()
				.putData("title", "타이머 알림")
				.putData("body", "주기적인 알림입니다.")
				.putData("partLists", partLists.toString())
				.setToken(userId) // 사용자 토큰 (FCM 토큰)
				.build();

			try {
				// FCM 메시지 전송
				String response = FirebaseMessaging.getInstance().send(message);
				log.info("Successfully sent message: " + response);
			} catch (FirebaseMessagingException e) {
				log.error("Error sending FCM message", e);
				throw new RuntimeException(e);
			}
		}

		private void startTimerCycle(Timer timer) {
			long cycleInMillis = (long)(timer.getCycle() * 60 * 60 * 1000); // 주기 (시간 단위 -> 밀리초)
			new TimerTask(timer.getId(), cycleInMillis).start();
		}

		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(cycleInMillis);
					cycleCount++;
					Timer timer = timerRepository.findById(timerId)
						.orElseThrow(() -> new AppException(TimerErrorCode.TIMER_NOT_FOUND));
					sendFCMNotification(timer.getUser().getId(), timer.getPartLists());
					log.info("Cycle count: " + cycleCount);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					log.error("Timer interrupted", e);
					break;
				} catch (AppException e) {
					log.error("Timer not found", e);
					break;
				}
			}
		}
	}
}
