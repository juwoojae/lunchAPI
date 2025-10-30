package com.example.springjwt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 테스트용 컨트롤러
 */
@RequestMapping("/")
@RestController
public class HomeController {

    @GetMapping
    public String HomeController() {
        return "Hello controller";
    }
}
