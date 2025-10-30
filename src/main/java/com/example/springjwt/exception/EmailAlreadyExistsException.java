package com.example.springjwt.exception;

/**
 * 도메인 예외
 * 이메일이 같은 사용자는 중복등록 불가능
 */
public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
