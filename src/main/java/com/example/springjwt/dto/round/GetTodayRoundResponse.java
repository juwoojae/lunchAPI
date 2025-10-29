package com.example.springjwt.dto.round;

import com.example.springjwt.dto.menu.GetTodayMenuResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class GetTodayRoundResponse {
    private Long id;
    private String userName;
    private LocalDate date;
    private List<GetTodayMenuResponse> menus;
    private int totalVotes;
}
