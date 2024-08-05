package com.likelion.timer.satisfatction;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.likelion.timer.user.model.User;

public interface SatisfactionRepository extends JpaRepository<Satisfaction, Long> {
    List<Satisfaction> findByYearAndMonthAndDayAndUser(int year, int month, int day, User user);
}
