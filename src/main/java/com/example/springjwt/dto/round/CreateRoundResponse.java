package com.example.springjwt.dto.round;

import com.example.springjwt.dto.menu.CreateMenuResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import  java.util.List;

@Getter
@AllArgsConstructor
public class CreateRoundResponse {
    private Long id;
    private Long userId;
    private LocalDate date;
    private List<CreateMenuResponse> menus;
}
