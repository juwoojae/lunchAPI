package com.example.springjwt.controller;

import com.example.springjwt.dto.CustomUserDetails;
import com.example.springjwt.dto.round.CreateRoundRequest;
import com.example.springjwt.dto.round.CreateRoundResponse;
import com.example.springjwt.entity.UserEntity;
import com.example.springjwt.repository.UserRepository;
import com.example.springjwt.service.RoundService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rounds")
@RequiredArgsConstructor
public class RoundController {

    private final UserRepository userRepository;
    private final RoundService roundService;

    @PostMapping
    public ResponseEntity<CreateRoundResponse> createRound(
            @AuthenticationPrincipal CustomUserDetails userDetails, // 현재 로그인한 유저
            @RequestBody CreateRoundRequest requestDto
    ) {
        CreateRoundResponse result = roundService.create(userDetails.getUserEntity(), requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}