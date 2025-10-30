package com.example.springjwt.exception;

/**
 * round 는 하루에 하나만 만들수있다.
 * 라는 도메인 정책에 위배된 경우 던지는 예외
 */
public class RoundAlreadyExistsException extends RuntimeException {
    public RoundAlreadyExistsException(String message) {
        super(message);
    }
}
