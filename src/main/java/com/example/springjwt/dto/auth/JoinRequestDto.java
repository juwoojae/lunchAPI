package com.example.springjwt.dto.auth;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
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
