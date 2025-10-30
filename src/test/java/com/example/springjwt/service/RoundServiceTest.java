package com.example.springjwt.service;

import com.example.springjwt.dto.menu.CreateMenuRequest;
import com.example.springjwt.dto.round.CreateRoundRequest;
import com.example.springjwt.dto.round.CreateRoundResponse;
import com.example.springjwt.entity.MenuEntity;
import com.example.springjwt.entity.RoundEntity;
import com.example.springjwt.entity.UserEntity;
import com.example.springjwt.repository.MenuRepository;
import com.example.springjwt.repository.RoundRepository;
import com.example.springjwt.repository.VoteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * 1. RoundRepository 내부 메서드 사용(의존성 확인)
 * 2. 반환값이 맞는지
 * 3. 예외 발생 여부 체크
 */
@ExtendWith(MockitoExtension.class)
class RoundServiceTest {

    @InjectMocks
    RoundService roundService;

    @Mock
    MenuRepository menuRepository;
    @Mock
    RoundRepository roundRepository;
    @Mock
    VoteRepository voteRepository;

    @Test
    void createRound() {

        //given
        UserEntity user = new UserEntity("test@naver.com","주우재","1234","admin");

        CreateMenuRequest menu1 = new CreateMenuRequest("부대찌개", "KOREAN", 9000);
        CreateMenuRequest menu2 = new CreateMenuRequest("초밥세트", "JAPANESE", 12000);
        List<CreateMenuRequest> menus = List.of(menu1, menu2);
        CreateRoundRequest requestDto = new CreateRoundRequest(
                LocalDate.parse("2025-10-29"),
                menus
        );
        LocalDate date = LocalDate.parse("2025-10-29");
        RoundEntity savedRound = new RoundEntity(user, date);
        ReflectionTestUtils.setField(savedRound, "id", 1L);

        MenuEntity savedMenu = new MenuEntity(savedRound,"부대찌개",9000,"KOREAN");

        given(roundRepository.save(any(RoundEntity.class))).willReturn(savedRound);
        given(menuRepository.save(any(MenuEntity.class))).willReturn(savedMenu);

        //when
        CreateRoundResponse register = roundService.register(user, requestDto);

        //then
        verify(roundRepository).save(any(RoundEntity.class)); //RoundRepository 내부 메서드 사용(의존성 확인)
        verify(menuRepository, times(2)).save(any(MenuEntity.class));  //MenuRepository 내부 메서드 사용(의존성 확인)

        //반환 값 검증
        assertTrue(register.getId()==1L);

    }
}