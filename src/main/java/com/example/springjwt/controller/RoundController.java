package com.example.springjwt.controller;

import com.example.springjwt.dto.CustomUserDetails;
import com.example.springjwt.dto.round.CreateRoundRequest;
import com.example.springjwt.dto.round.CreateRoundResponse;
import com.example.springjwt.dto.round.GetRoundResponse;
import com.example.springjwt.dto.round.GetTodayRoundResponse;
import com.example.springjwt.repository.UserRepository;
import com.example.springjwt.service.RoundService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/rounds")
@RequiredArgsConstructor
public class RoundController {

    private final UserRepository userRepository;
    private final RoundService roundService;

    @PostMapping
    public ResponseEntity<CreateRoundResponse> addRound(
            @AuthenticationPrincipal CustomUserDetails userDetails, // 현재 로그인한 유저
            @RequestBody CreateRoundRequest requestDto
    ) {
        CreateRoundResponse result = roundService.register(userDetails.getUserEntity(), requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping
    public ResponseEntity<List<GetRoundResponse>> rounds(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<GetRoundResponse> result = roundService.read(userDetails.getUserEntity());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/today")
    public ResponseEntity<GetTodayRoundResponse> todayRound(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        LocalDate today = LocalDate.now();
        GetTodayRoundResponse result = roundService.todayRead(userDetails.getUserEntity(), today);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping("/{roundId}")
    public ResponseEntity<Void> deleteRound(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long roundId) {
        roundService.remove(roundId, userDetails.getUserEntity());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}