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

    @Transactional // 쓰기 작업을 위한 트랜잭션 설정
    public SatisfactionResponse addSatisfaction(SatisfactionRequest request, String username) {
        LocalDateTime now = LocalDateTime.now();
        int year = request.getYear() != null ? request.getYear() : now.getYear(); // 요청에 년도가 있으면 사용하고, 없으면 현재 년도를 사용
        int month = request.getMonth() != null ? request.getMonth() : now.getMonthValue();
        int day = request.getDay() != null ? request.getDay() : now.getDayOfMonth();

        User user = userRepository.findByNickname(username);
        //User 객체가 없으면 USER_NOT_FOUND 예외 던짐
        if (user == null) {
            throw new ExceptionHandler.ApiException(ExceptionHandler.ErrorType.USER_NOT_FOUND, username);
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
            throw new ExceptionHandler.ApiException(ExceptionHandler.ErrorType.USER_NOT_FOUND, username);
        }

        // year, month, day, user를 기준으로 Satisfaction 객체 목록을 찾고, SatisfactionResponse 객체 목록으로 변환하여 반환
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

    @Transactional

    // ID로 Satisfaction 객체를 찾아서 존재하지 않으면 예외 던짐.
    public SatisfactionResponse editSatisfaction(Long id, SatisfactionRequest request, String username) {
        Satisfaction existingSatisfaction = satisfactionRepository.findById(id)
            .orElseThrow(() -> new ExceptionHandler.ApiException(ExceptionHandler.ErrorType.USER_NOT_FOUND));

        //사용자 닉네임으로 User 객체를 찾아서 존재하지 않으면 예외 던짐
        User user = userRepository.findByNickname(username);
        if (user == null) {
            throw new ExceptionHandler.ApiException(ExceptionHandler.ErrorType.USER_NOT_FOUND, username);
        }

        if (!existingSatisfaction.getUser().equals(user)) {
            throw new ExceptionHandler.ApiException(ExceptionHandler.ErrorType.USER_NOT_FOUND, username);
        }

        // 새로운 Satisfaction 객체 생성
        Satisfaction updatedSatisfaction = new Satisfaction(
            existingSatisfaction.getId(),
            request.getRating(),
            request.getComment(),
            request.getPartType(),
            request.getYear() != null ? request.getYear() : existingSatisfaction.getYear(),
            request.getMonth() != null ? request.getMonth() : existingSatisfaction.getMonth(),
            request.getDay() != null ? request.getDay() : existingSatisfaction.getDay(),
            user
        );

        satisfactionRepository.save(updatedSatisfaction);

        //업데이트된 Satisfaction 객체를 바탕으로 SatisfactionResponse 객체를 생성하여 반환
        return new SatisfactionResponse(
            updatedSatisfaction.getId(),
            updatedSatisfaction.getRating(),
            updatedSatisfaction.getComment(),
            updatedSatisfaction.getPartType(),
            updatedSatisfaction.getYear(),
            updatedSatisfaction.getMonth(),
            updatedSatisfaction.getDay()
        );
    }
}