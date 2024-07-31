package com.likelion.timer.domain.Timer.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.likelion.timer.domain.Timer.domain.entity.Timer;

@Repository
public interface TimerRepository extends JpaRepository<Timer, Long> {

}
