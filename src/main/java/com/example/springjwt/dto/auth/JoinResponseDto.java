package com.example.springjwt.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class JoinResponseDto {
    private final Long id;
    private final String email;
    private final String name;
    private final String role;
    private final LocalDateTime createdAt;
}
