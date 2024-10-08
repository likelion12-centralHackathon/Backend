package com.likelion.timer.timer.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.likelion.timer.timer.domain.entity.Timer;
import com.likelion.timer.timer.dto.res.TimerListResDto;
import com.likelion.timer.timer.model.TimerStateTypeEnum;

@Repository
public interface TimerRepository extends JpaRepository<Timer, Long> {
	@Query("SELECT t FROM Timer t WHERE t.user.id = :userId AND t.id = :timerId")
	Optional<Timer> findByUserIdAndId(@Param("userId") String userId, @Param("timerId") Long timerId);

	Optional<Timer> findByUserIdAndName(String userId, String name);

	@Query("SELECT new com.likelion.timer.timer.dto.res.TimerListResDto(t.id, t.name) " +
		"FROM Timer t "
		+ "WHERE t.user.id = :userId AND t.isPermanent = true " +
		"ORDER BY t.regTime DESC")
	List<TimerListResDto> findPermanentTimersNameAndIdByUserId(@Param("userId") String userId);

	List<Timer> findByTimerState(TimerStateTypeEnum timerStateTypeEnum);
}
