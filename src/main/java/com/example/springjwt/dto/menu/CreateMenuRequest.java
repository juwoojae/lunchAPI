package com.example.springjwt.dto.menu;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateMenuRequest {
    private String name;
    private String type;
    private int price;

}
