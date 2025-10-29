package com.example.springjwt.service;

import com.example.springjwt.dto.menu.CreateMenuRequest;
import com.example.springjwt.dto.menu.CreateMenuResponse;
import com.example.springjwt.dto.round.CreateRoundRequest;
import com.example.springjwt.dto.round.CreateRoundResponse;
import com.example.springjwt.dto.round.GetTodayRoundResponse;
import com.example.springjwt.entity.MenuEntity;
import com.example.springjwt.entity.RoundEntity;
import com.example.springjwt.entity.UserEntity;
import com.example.springjwt.repository.MenuRepository;
import com.example.springjwt.repository.RoundRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j(topic = "RoundService")
@Service
@AllArgsConstructor
public class RoundService {

    MenuRepository menuRepository;
    RoundRepository roundRepository;

    /**
     * 라운드는 하루에 하나만 생성할수 있다.
     */
    @Transactional
    public CreateRoundResponse create(UserEntity user, CreateRoundRequest requestDto) {
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

        // DB에서 저장된 메뉴 조회 (단방향 기준)
        List<CreateMenuResponse> menuResponses = menuRepository.findByRoundEntity_Id(saveRound.getId())
                .stream()
                .map(menu -> new CreateMenuResponse(
                        menu.getId(),
                        menu.getName(),
                        menu.getType(),
                        menu.getPrice(),
                        0 // 나중에 구현
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

//    /**
//     * 오늘의 라운드 조회
//     */
//    public GetTodayRoundResponse todayRead(UserEntity user, LocalDate date) {
//        Optional<RoundEntity> byDate = roundRepository.findByDate(date);
//    }
}
