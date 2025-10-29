package com.example.springjwt.exception;

/**
 * 토큰 만료 401 Unauthorized
 */
public class ExpiredException extends RuntimeException {
    public ExpiredException(String message) {
        super(message);
    }
}
