package com.example.springjwt.service;

import com.example.springjwt.dto.round.CreateRoundRequest;
import com.example.springjwt.entity.MenuEntity;
import com.example.springjwt.entity.UserEntity;
import com.example.springjwt.entity.VoteEntity;
import com.example.springjwt.repository.MenuRepository;
import com.example.springjwt.repository.RoundRepository;
import com.example.springjwt.repository.UserRepository;
import com.example.springjwt.repository.VoteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoundServiceTest {
    @InjectMocks
    RoundService roundService;

    @Mock
    RoundRepository roundRepository;
    @Mock
    MenuRepository menuRepository;

    /**
     * 검증해야 할것
     * 1. CreateRoundRequest 에서 CreateRoundResponse 변환이 제대로 되는지
     * 2. postRepository.save()가 호출되는지
     * 3. 반환되는 CreateRoundResponse 가 예상값과 일치하는지
     */
    @Test
    void registerTest() {
        CreateRoundRequest createRoundRequest = new CreateRoundRequest();
        CreateRoundRequest request = new CreateRoundRequest();
        request.setDate(LocalDate.of(2025, 10, 30));
        request.setMenus(List.of(
                new CreateMenuRequest("김치찌개", 8000, "KOREAN")
        ));
        //Mock 으로 구현
        VoteEntity saved = new VoteEntity(new UserEntity(),new MenuEntity());

    }
}