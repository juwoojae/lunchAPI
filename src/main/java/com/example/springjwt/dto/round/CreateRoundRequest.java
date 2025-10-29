package com.example.springjwt.dto.round;

import com.example.springjwt.dto.menu.CreateMenuRequest;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class CreateRoundRequest {
    private LocalDate date;
    private List<CreateMenuRequest> menus;
}
