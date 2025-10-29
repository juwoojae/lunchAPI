package com.example.springjwt.dto.menu;

import lombok.Getter;

@Getter
public class CreateMenuRequest {
    private String name;
    private String type;
    private int price;

}
