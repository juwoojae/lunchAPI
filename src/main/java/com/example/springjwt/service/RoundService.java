package com.example.springjwt.service;

import com.example.springjwt.dto.menu.CreateMenuRequest;
import com.example.springjwt.dto.menu.CreateMenuResponse;
import com.example.springjwt.dto.menu.GetTodayMenuResponse;
import com.example.springjwt.dto.menu.WinnerMenu;
import com.example.springjwt.dto.round.CreateRoundRequest;
import com.example.springjwt.dto.round.CreateRoundResponse;
import com.example.springjwt.dto.round.GetRoundResponse;
import com.example.springjwt.dto.round.GetTodayRoundResponse;
import com.example.springjwt.entity.MenuEntity;
import com.example.springjwt.entity.RoundEntity;
import com.example.springjwt.entity.UserEntity;
import com.example.springjwt.repository.MenuRepository;
import com.example.springjwt.repository.RoundRepository;
import com.example.springjwt.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j(topic = "RoundService")
@Service
@RequiredArgsConstructor
public class RoundService {

    private final MenuRepository menuRepository;
    private final RoundRepository roundRepository;
    private final VoteRepository voteRepository;

    /**
     * 라운드는 하루에 하나만 생성할수 있다.
     */
    @Transactional
    public CreateRoundResponse register(UserEntity user, CreateRoundRequest requestDto) {
        List<CreateMenuRequest> menus = requestDto.getMenus();
        //만약 round Table 안에 같은 날짜의 컬럼이 존재하는경우 예외 발생
        LocalDate date = requestDto.getDate();
        if (roundRepository.existsByDate(date)) {
            throw new IllegalArgumentException("Round already exists");
        }
        //저장할 엔티티 생성
        RoundEntity roundEntity = new RoundEntity(user, requestDto.getDate());
        RoundEntity saveRound = roundRepository.save(roundEntity);
        for (CreateMenuRequest menu : menus) {
            MenuEntity saveMenu = new MenuEntity(roundEntity, menu.getName(), menu.getPrice(), menu.getType());
            menuRepository.save(saveMenu);
        }

        // DB에서 저장된 메뉴 조회
        List<CreateMenuResponse> menuResponses = menuRepository.findByRound_Id(saveRound.getId())
                .stream()
                .map(menu -> new CreateMenuResponse(
                        menu.getId(),
                        menu.getName(),
                        menu.getType(),
                        menu.getPrice(),
                        voteRepository.countByMenu_Id(menu.getId())
                ))
                .toList();

        // CreateRoundResponse 생성
        return new CreateRoundResponse(
                saveRound.getId(),
                user.getId(),
                saveRound.getDate(),
                menuResponses
        );
    }

    /**
     * 오늘의 라운드 조회
     */
    @Transactional(readOnly = true)
    public GetTodayRoundResponse todayRead(UserEntity user, LocalDate date) {
        RoundEntity findRound = roundRepository.findByDate(date).orElseThrow(
                () -> new IllegalArgumentException("Round not found")
        );
        List<MenuEntity> menus = menuRepository.findByRound_Id(findRound.getId());
        List<GetTodayMenuResponse> menuResponses = new ArrayList<>();

        int totalVotes = 0;   //전체 Vote 누적합

        for (MenuEntity menu : menus) {
            int vote = voteRepository.countByMenu_Id(menu.getId());
            totalVotes += vote;

            menuResponses.add(new GetTodayMenuResponse(
                    menu.getId(),
                    menu.getName(),
                    menu.getType(),
                    totalVotes,
                    voteRepository.existsByUserAndMenu(user, menu) // menu 를 user 가 vote 했는지
            ));
        }
        // RoundResponseDto 생성
        return new GetTodayRoundResponse(
                findRound.getId(),
                user.getName(),        // userName
                findRound.getDate(),
                menuResponses,
                totalVotes
        );
    }

    /**
     * 라운드 전체 조회
     */
    @Transactional(readOnly = true)
    public List<GetRoundResponse> read(UserEntity user) {
        List<RoundEntity> response = roundRepository.findAll();
        List<GetRoundResponse> roundResponses = new ArrayList<>();

        for (RoundEntity round : response) {
            Long roundId = round.getId();
            List<MenuEntity> menus = menuRepository.findByRound_Id(roundId);

            int totalVote = 0;  //총 투표수
            WinnerMenu winnerMenu = null;
            int maxVote = 0;   //최다 투표 메뉴의 투표수

            for (MenuEntity menu : menus) {
                int vote = voteRepository.countByMenu_Id(menu.getId());
                totalVote += vote;
                if(maxVote < vote) {
                    maxVote = vote;
                    winnerMenu = new WinnerMenu(menu.getName(), maxVote);
                }
            }
            roundResponses.add(new GetRoundResponse(
                    roundId,
                    user.getId(),
                    round.getDate(),
                    menus.size(),
                    totalVote,
                    winnerMenu
            ));
        }
        return roundResponses;
    }

    /**
     * 라운드 삭제
     * 해당라운드를 생성한 유저만 삭제가 가능하다
     */
    @Transactional
    public void remove(Long roundId, UserEntity user) {
        if (!roundId.equals(user.getId())) {
            throw new IllegalArgumentException("id 가 일치하지 않는다");
        }
        menuRepository.deleteAllByRound_Id(roundId);
        roundRepository.deleteById(roundId);
    }
}
