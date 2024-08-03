package com.likelion.timer.domain.Timer.domain.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.likelion.timer.domain.Timer.domain.entity.RecordTimer;
import com.likelion.timer.domain.Timer.domain.entity.Timer;
import com.likelion.timer.domain.Timer.dto.res.PartStaticResDto;
import com.likelion.timer.domain.model.RecordTimerStateTypeEnum;

@Repository
public interface RecordTimerRepository extends JpaRepository<RecordTimer, Long> {
	// 최근 기록된 record timer 타이머, 사용자에 따라 가져오기
	Optional<RecordTimer> findTopByTimerIdAndUserIdOrderByRegTimeDesc(Long timerId, String userId);

	// 최근 기록된 record timer 타이머에 따라 가져오기
	RecordTimer findTopByTimerIdOrderByRegTimeDesc(Long timerId);

	// 최근 기록된 record timer 타이머, 사용자, timerstate에 따라 가져오기
	Optional<RecordTimer> findTopByTimerIdAndUserIdAndTimerStateTypeOrderByRegTimeDesc(Long timerId, String userId,
		RecordTimerStateTypeEnum timerStateType);

	// 날짜별로 기록된 timer 가져오기
	@Query("SELECT DISTINCT r.timer FROM RecordTimer r "
		+ "WHERE r.user.id = :userId "
		+ "AND r.regTime BETWEEN :start AND :end")
	List<Timer> findDistinctTimerIdsByUserIdAndRegTimeBetween(
		@Param("userId") String userId,
		@Param("start") LocalDateTime start,
		@Param("end") LocalDateTime end
	);

	// 날짜별로 기록된 timer 부위 별 스트레칭 시간 가져오기
	@Query(
		"SELECT NEW com.likelion.timer.domain.Timer.dto.res.PartStaticResDto(r.part.partType, SUM(r.stretchingTime)) " +
			"FROM RecordTimer r " +
			"WHERE r.user.id = :userId AND r.regTime BETWEEN :start AND :end AND r.part IS NOT NULL " +
			"GROUP BY r.part")
	List<PartStaticResDto> findPartStaticResDtosByUserAndRegTimeBetween(
		@Param("userId") String userId,
		@Param("start") LocalDateTime start,
		@Param("end") LocalDateTime end
	);
}