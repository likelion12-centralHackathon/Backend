package com.likelion.timer.satisfatction;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/satisfaction")
public class SatisfactionController {

    private final SatisfactionService satisfactionService;

    @PostMapping("/satisfaction") // 만족도 추가
    public ResponseEntity<ApiResponse<SatisfactionResponse>> addSatisfaction(
        Authentication authentication,
        @Valid @RequestBody SatisfactionRequest request
    ) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        SatisfactionResponse response = satisfactionService.addSatisfaction(request, userDetails.getUsername());
        ApiResponse<SatisfactionResponse> apiResponse = new ApiResponse<>(200, "Success", response);
        return ResponseEntity.status(201).body(apiResponse);
    }

    @GetMapping("/satisfactions") // 나의 일자별 만족도 목록 조회
    public ResponseEntity<ApiResponse<List<SatisfactionResponse>>> getSatisfactions(
        Authentication authentication,
        @RequestParam(required = false) Integer year,
        @RequestParam(required = false) Integer month,
        @RequestParam(required = false) Integer day
    ) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<SatisfactionResponse> responses = satisfactionService.getSatisfactions(year, month, day, userDetails.getUsername());
        ApiResponse<List<SatisfactionResponse>> apiResponse = new ApiResponse<>(200, "Success", responses);
        return ResponseEntity.ok(apiResponse);
    }
}