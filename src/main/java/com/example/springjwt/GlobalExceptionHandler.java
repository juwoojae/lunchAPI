package com.example.springjwt;

import com.example.springjwt.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 전체 예외처리 컨트롤러
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 파싱에러
     * HttpMessageNotReadableException 검증
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> NotReadableErrors(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * 2차 서버 검증 - beanValidation 에 대한 400 를 포함한 HTTP 메세지 전달
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    /**
     * 토큰 만료 오류 401 Unauthorized
     */
    @ExceptionHandler(ExpiredException.class)
    public ResponseEntity<String> handleTokenExpiredErrors(ExpiredException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    /**
     * 토큰 위조, 손상 401 Unauthorized
     */
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<String> handleInvalidateTokenErrors(InvalidTokenException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    /**
     *  토큰 없음 403 Forbidden
     */
    @ExceptionHandler(TokenMissingException.class)
    public ResponseEntity<String> handleNotFoundTokenErrors(TokenMissingException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * 도메인 정책 위반 - Round 를 하루에 2개 이상 만들때 던지는예외
     */
    @ExceptionHandler (RoundAlreadyExistsException.class)
    public ResponseEntity<String> handleRoundDomainErrors(TokenMissingException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    /**
     * 도메인 정책 위반 - 같은 유저가 하나의 메뉴에 중복투표
     */
    @ExceptionHandler (DuplicateVoteException.class)
    public ResponseEntity<String> handleVoteDomainErrors(DuplicateVoteException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

}
