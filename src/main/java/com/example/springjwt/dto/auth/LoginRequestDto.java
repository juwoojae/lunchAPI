package com.example.springjwt.dto.auth;

import lombok.Getter;

@Getter
public class LoginRequestDto {
    private String email;
    private String password;
}
