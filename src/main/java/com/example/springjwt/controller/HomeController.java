package com.example.springjwt.controller;

import com.example.springjwt.dto.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    /**
     * 인가 Authorization 에서 이제
     * authentication 객체를 만들고, 이객체를 SecurityContextHolder 에 넣으면 요청마다
     * 일시적으로 세션을 생성한다
     */
    @GetMapping("/")
    public String homeP(@AuthenticationPrincipal CustomUserDetails userDetails) {

        //Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();

        return "Home Controller" + userDetails.getUsername() + userDetails.getRole();
    }
}
