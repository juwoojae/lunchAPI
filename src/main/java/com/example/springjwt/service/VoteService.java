package com.example.springjwt.service;

import com.example.springjwt.dto.vote.CreateVoteRequest;
import com.example.springjwt.dto.vote.GetVoteResponse;
import com.example.springjwt.entity.MenuEntity;
import com.example.springjwt.entity.UserEntity;
import com.example.springjwt.entity.VoteEntity;
import com.example.springjwt.repository.MenuRepository;
import com.example.springjwt.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final MenuRepository menuRepository;

    /**
     * 한 사용자가 같은 메뉴에 중복 투표 불가능
     */
    @Transactional
    public void register(UserEntity user, CreateVoteRequest requestDto) {

        Long menuId = requestDto.getMenuId();
        MenuEntity menu = menuRepository.findById(menuId).orElseThrow(
                () -> new IllegalStateException("Menu Not Found")
        );
        // 중복 체크
        boolean alreadyVoted = voteRepository.existsByUserAndMenu(user, menu);
        if (alreadyVoted) {
            throw new IllegalStateException("이미 투표한 메뉴입니다.");
        }
        // 투표 저장
        VoteEntity vote = new VoteEntity(user, menu);
        voteRepository.save(vote);
    }

    @Transactional(readOnly = true)
    public List<GetVoteResponse> getMyVotes(UserEntity user, Long roundId) {

        List<VoteEntity> votes;
        if (roundId != null) {
            //특정 round 투표 내역 조회
            votes = voteRepository.findAllByUserAndMenu_Round_Id(user, roundId);
        } else {
            //roundId 가 null 인 경우 오늘의 라운드 조회
            LocalDate today = LocalDate.now();
            votes = voteRepository.findAllByUserAndMenu_Round_Date(user, today);
        }

        return votes.stream()
                .map(vote -> new GetVoteResponse(
                    vote.getId(),
                        vote.getMenu().getId(),
                        vote.getCreatedDate()
                )).toList();
    }

    @Transactional
    public void remove(Long voteId, UserEntity user) {
        if (!voteId.equals(user.getId())) {
            throw new IllegalArgumentException("id 가 일치하지 않는다");
        }
        menuRepository.deleteAllByRound_Id(voteId);
        voteRepository.deleteById(voteId);
    }
}
