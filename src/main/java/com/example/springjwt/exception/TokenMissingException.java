package com.example.springjwt.exception;

/**
 * 토큰 없음 403 Forbidden
 */
public class TokenMissingException extends RuntimeException {
    public TokenMissingException(String message) {
        super(message);
    }
}
