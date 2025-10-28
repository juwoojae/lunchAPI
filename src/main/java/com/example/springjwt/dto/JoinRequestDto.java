package com.example.springjwt.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class JoinRequestDto {

    private String email;
    private String name;
    private String password;
    private String role;
}
