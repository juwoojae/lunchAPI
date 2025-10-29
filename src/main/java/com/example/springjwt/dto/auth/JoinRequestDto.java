package com.example.springjwt.dto.auth;

import lombok.Getter;

@Getter
public class JoinRequestDto {

    private String email;
    private String name;
    private String password;
    private String role;
}
