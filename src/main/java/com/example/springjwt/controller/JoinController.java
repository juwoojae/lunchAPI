package com.example.springjwt.controller;

import com.example.springjwt.dto.JoinDto;
import com.example.springjwt.service.JoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "JoinController")
@RestController
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;

    /**
     * 회원 가입처리 컨트롤러
     */
    @PostMapping("/join")
    public String joinProcess(@RequestBody JoinDto joinDto) {
        log.info("username: {}", joinDto.getUsername());
        log.info("password: {}", joinDto.getPassword());
        joinService.joinProcess(joinDto);
        return "ok";
    }
}
