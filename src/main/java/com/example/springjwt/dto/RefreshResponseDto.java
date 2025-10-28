package com.example.springjwt.dto;

import lombok.Getter;

@Getter
public class RefreshResponseDto {
    private final String AccessToken;
    private final String refreshToken;

    public RefreshResponseDto(String accessToken, String refreshToken) {
        AccessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
