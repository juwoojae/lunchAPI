package com.example.springjwt.dto.round;

import com.example.springjwt.dto.menu.WinnerMenu;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class GetRoundResponse {
    private Long id;
    private Long userId;
    private LocalDate date; // LocalDate 써도 되지만 JSON용으로 String 처리 가능
    private int menuCount;
    private int totalVotes;
    private WinnerMenu winnerMenu;
}
