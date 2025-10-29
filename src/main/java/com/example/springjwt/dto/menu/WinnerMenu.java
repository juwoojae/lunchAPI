package com.example.springjwt.dto.menu;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WinnerMenu {
    private String name;
    private int voteCount;
}
