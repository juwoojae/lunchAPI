package com.example.springjwt.dto.auth;

import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class JoinRequestDto {

    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String name;
    @Size(min = 8)
    private String password;
    private String role;
}
