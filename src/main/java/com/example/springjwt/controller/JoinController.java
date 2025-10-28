package com.example.springjwt.controller;

import com.example.springjwt.dto.JoinRequestDto;
import com.example.springjwt.dto.JoinResponseDto;
import com.example.springjwt.jwt.JwtConst;
import com.example.springjwt.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.example.springjwt.jwt.JwtConst.*;

@Slf4j(topic = "JoinController")
@RestController
@RequiredArgsConstructor
public class JoinController {

    private final UserService userService;
    /**
     * 회원 가입처리 컨트롤러
     */
    @PostMapping("/join")
    public ResponseEntity<JoinResponseDto> joinProcess(@RequestBody JoinRequestDto joinRequestDto) {
        log.info("joinProcess {} {}", joinRequestDto.getEmail(), joinRequestDto.getPassword());
        JoinResponseDto result = userService.joinProcess(joinRequestDto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}
