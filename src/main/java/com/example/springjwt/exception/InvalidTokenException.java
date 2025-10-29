package com.example.springjwt.exception;

/**
 * 토큰 위조, 손상 401 Unauthorized
 */
public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
