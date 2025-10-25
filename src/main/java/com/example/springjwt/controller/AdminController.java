package com.example.springjwt.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    @PostMapping("/login")
    public String login(){return "Login Controller";}

    @Secured("ROLE_ADMIN")
    @GetMapping("/admin")
    public String adminP(){
        return "admin Controller";
    }
}
