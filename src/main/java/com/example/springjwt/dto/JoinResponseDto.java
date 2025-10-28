package com.example.springjwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@AllArgsConstructor
public class JoinResponseDto {
    private final Long id;
    private final String email;
    private final String name;
    private final String role;
    private final LocalDateTime createdAt;
}
