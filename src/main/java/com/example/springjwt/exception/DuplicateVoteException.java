package com.example.springjwt.exception;

/**
 * 도메인 예외
 * 한 사용자가 같은 메뉴에 중복투표 할경우 던지는 예외
 */
public class DuplicateVoteException  extends RuntimeException {
    public DuplicateVoteException(String message) {
        super(message);
    }
}
