package com.likelion.timer.domain.Timer.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.likelion.timer.domain.Timer.domain.entity.Timer;
import com.likelion.timer.domain.Timer.dto.res.TimerListResDto;

@Repository
public interface TimerRepository extends JpaRepository<Timer, Long> {

	@Query("SELECT t FROM Timer t WHERE t.user.id = :userId AND t.name = :name")
	Optional<Timer> findByUserIdAndName(@Param("userId") Long userId, @Param("name") String name);

	@Query("SELECT new com.likelion.timer.domain.Timer.dto.res.TimerListResDto(t.id, t.name) " +
		"FROM Timer t "
		+ "WHERE t.user.id = :userId AND t.isPermanent = true " +
		"ORDER BY t.regTime DESC")
	List<TimerListResDto> findPermanentTimersNameAndIdByUserId(@Param("userId") Long userId);
}
