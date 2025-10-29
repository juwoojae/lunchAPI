package com.example.springjwt.dto.menu;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetTodayMenuResponse {
    private Long id;
    private String name;
    private String type; // 예: KOREAN, JAPANESE
    private int voteCount;
    private boolean isVotedByMe;
}

