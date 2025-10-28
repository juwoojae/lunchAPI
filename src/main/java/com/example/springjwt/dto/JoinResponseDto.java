package com.example.springjwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@AllArgsConstructor
public class JoinResponseDto {
    private Long id;
    private String email;
    private String name;
    private String role;
    private LocalDateTime createdAt;
}
