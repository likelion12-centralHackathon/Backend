package com.likelion.timer.satisfatction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.likelion.timer.user.model.User;
import com.likelion.timer.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SatisfactionService {
    private final SatisfactionRepository satisfactionRepository;
    private final UserRepository userRepository;

    @Transactional
    public SatisfactionResponse addSatisfaction(SatisfactionRequest request, String username) {
        LocalDateTime now = LocalDateTime.now();
        int year = request.getYear() != null ? request.getYear() : now.getYear();
        int month = request.getMonth() != null ? request.getMonth() : now.getMonthValue();
        int day = request.getDay() != null ? request.getDay() : now.getDayOfMonth();

        User user = userRepository.findByNickname(username);
        if (user == null) {
            throw new ApiExceptionHandler.ApiException(ApiExceptionHandler.ErrorType.USER_NOT_FOUND, username);
        }

        Satisfaction satisfaction = new Satisfaction(
                request.getRating(),
                request.getComment(),
                request.getPartType(),
                year,
                month,
                day,
                user
        );

        satisfactionRepository.save(satisfaction);

        return new SatisfactionResponse(
                satisfaction.getId(),
                satisfaction.getRating(),
                satisfaction.getComment(),
                satisfaction.getPartType(),
                satisfaction.getYear(),
                satisfaction.getMonth(),
                satisfaction.getDay()
        );
    }

    public List<SatisfactionResponse> getSatisfactions(Integer year, Integer month, Integer day, String username) {
        LocalDateTime now = LocalDateTime.now();
        year = year != null ? year : now.getYear();
        month = month != null ? month : now.getMonthValue();
        day = day != null ? day : now.getDayOfMonth();

        User user = userRepository.findByNickname(username);
        if (user == null) {
            throw new ApiExceptionHandler.ApiException(ApiExceptionHandler.ErrorType.USER_NOT_FOUND, username);
        }

        return satisfactionRepository.findByYearAndMonthAndDayAndUser(year, month, day, user).stream()
                .map(s -> new SatisfactionResponse(
                        s.getId(),
                        s.getRating(),
                        s.getComment(),
                        s.getPartType(),
                        s.getYear(),
                        s.getMonth(),
                        s.getDay()
                ))
                .collect(Collectors.toList());
    }
}