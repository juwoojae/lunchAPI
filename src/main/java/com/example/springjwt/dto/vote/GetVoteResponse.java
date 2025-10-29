package com.example.springjwt.dto.vote;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
public class GetVoteResponse {
    private Long id;
    private Long menuId;
    private LocalDateTime createdDate;
}
