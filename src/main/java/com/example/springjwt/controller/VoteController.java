package com.example.springjwt.controller;

import com.example.springjwt.dto.CustomUserDetails;
import com.example.springjwt.dto.vote.CreateVoteRequest;
import com.example.springjwt.dto.vote.GetVoteResponse;
import com.example.springjwt.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/votes")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @PostMapping
    public ResponseEntity<Void> voteMenu(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody CreateVoteRequest createVoteRequest
    ) {
        voteService.register(customUserDetails.getUserEntity(),createVoteRequest );
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/my")
    public ResponseEntity<List<GetVoteResponse>> getMyVotes(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(required = false) Long roundId
    ) {
        List<GetVoteResponse> result = voteService.getMyVotes(customUserDetails.getUserEntity(), roundId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping("/{voteId}")
    public ResponseEntity<Void> deleteVote(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long voteId) {
        voteService.remove(voteId, userDetails.getUserEntity());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
