package com.example.springjwt.dto.menu;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateMenuResponse {
    private Long id;
    private String name;
    private String type;
    private int price;
    private int voteCount;
}
