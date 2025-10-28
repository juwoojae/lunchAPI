package com.example.springjwt.exception;

public class TokenMissingException extends RuntimeException {
    public TokenMissingException(String message) {
        super(message);
    }
}
