package com.likelion.timer.satisfatction;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiException.class) // 커스텀 예외
    public ResponseEntity<?> handleApiException(ApiException exception) {
        log.error("ApiException: ", exception);
        return new ResponseEntity<>(
                new ApiErrorResponse(exception.getCode(), exception.getMessage(), exception.getData()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class) // 메서드 인수의 타입 불일치
    public ResponseEntity<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        log.error("MethodArgumentTypeMismatchException: ", exception);
        return new ResponseEntity<>(
                new ApiErrorResponse(ErrorType.INVALID_REQUEST_PARAMETER.getCode(), ErrorType.INVALID_REQUEST_PARAMETER.getMessage(), exception.getName()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MissingServletRequestParameterException.class) // 필수 요청 파라미터 누락
    public ResponseEntity<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException exception) {
        log.error("MissingServletRequestParameterException: ", exception);
        return new ResponseEntity<>(
                new ApiErrorResponse(ErrorType.MISSING_REQUEST_PARAMETER.getCode(), ErrorType.MISSING_REQUEST_PARAMETER.getMessage(), exception.getParameterName()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MissingRequestHeaderException.class) // 필수 요청 헤더 누락
    public ResponseEntity<?> handleMissingRequestHeaderException(MissingRequestHeaderException exception) {
        log.error("MissingRequestHeaderException: ", exception);
        return new ResponseEntity<>(
                new ApiErrorResponse(ErrorType.MISSING_HEADER.getCode(), ErrorType.MISSING_HEADER.getMessage(), exception.getHeaderName()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class) // 요청 본문이 읽을 수 없는 경우 (예: JSON 파싱 오류)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        log.error("HttpMessageNotReadableException: ", exception);
        return new ResponseEntity<>(
                new ApiErrorResponse(ErrorType.INVALID_REQUEST_BODY.getCode(), ErrorType.INVALID_REQUEST_BODY.getMessage(), null),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Exception.class) // 기타 모든 예외
    public ResponseEntity<?> handleOtherExceptions(Exception exception) {
        log.error("Exception: ", exception);
        return new ResponseEntity<>(
                new ApiErrorResponse(ErrorType.UNEXPECTED_ERROR.getCode(), ErrorType.UNEXPECTED_ERROR.getMessage(), null),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErrorType {

        UNEXPECTED_ERROR("001", "예기치 않은 오류가 발생하였습니다."),
        MISSING_HEADER("002", "요청 헤더가 누락되었습니다."),
        INVALID_REQUEST_PARAMETER("003", "잘못된 요청 파라미터입니다."),
        MISSING_REQUEST_PARAMETER("004", "요청 파라미터가 누락되었습니다."),
        INVALID_REQUEST_BODY("005", "잘못된 요청 본문입니다."),
        USER_NOT_FOUND("006", "사용자를 찾을 수 없습니다."),
        VIDEO_NOT_FOUND("007", "영상을 찾을 수 없습니다.");

        private final String code;
        private final String message;
    }

    @Getter
    @RequiredArgsConstructor
    public static class ApiErrorResponse {
        private final String code;
        private final String message;
        private final Object data;
    }

    @Getter
    @Setter
    public static class ApiException extends RuntimeException {

        private final String code;
        private final String message;
        private final Object data;

        public ApiException(ErrorType errorType) {
            super(errorType.getMessage());
            this.code = errorType.getCode();
            this.message = errorType.getMessage();
            this.data = null;
        }

        public ApiException(ErrorType errorType, Object data) {
            super(errorType.getMessage());
            this.code = errorType.getCode();
            this.message = errorType.getMessage();
            this.data = data;
        }

        public ApiException(String code, String message) {
            super(message);
            this.code = code;
            this.message = message;
            this.data = null;
        }

        public ApiException(String code, String message, Object data) {
            super(message);
            this.code = code;
            this.message = message;
            this.data = data;
        }
    }
}