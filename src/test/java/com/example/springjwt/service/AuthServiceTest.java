package com.example.springjwt.service;

import com.example.springjwt.dto.auth.JoinRequestDto;
import com.example.springjwt.dto.auth.JoinResponseDto;
import com.example.springjwt.entity.UserEntity;
import com.example.springjwt.exception.EmailAlreadyExistsException;
import com.example.springjwt.exception.RoundAlreadyExistsException;
import com.example.springjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    AuthService authService;

    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;

    @Test
    void 회원_가입_테스트(){
        //given
        JoinRequestDto joinRequestDto = new JoinRequestDto("test@naver.com","주우재","1234","admin");
        UserEntity joinUser = new UserEntity("test@naver.com", "주우재", "1234", "admin");
        ReflectionTestUtils.setField(joinUser, "id", 1L); // Mocked DB ID

        given(userRepository.save(any(UserEntity.class))).willReturn(joinUser);
        given(passwordEncoder.encode(joinUser.getPassword())).willReturn("암호화된패스워드가생성되었습니다");
        //when&then
        JoinResponseDto joinResponseDto = authService.join(joinRequestDto);
        /**
         * 1. 내부 의존성 확인
         */
        verify(userRepository).save(any(UserEntity.class));
        verify(passwordEncoder).encode(any(String.class));
        /**
         * 2. 반환값 검증
         * 요청 Dto 의 값 - 응답 Dto 의 값이 같은지
         */
        assertEquals(joinRequestDto.getEmail(),joinResponseDto.getEmail());
        assertEquals(joinRequestDto.getRole(),joinResponseDto.getRole());
        /**
         * 3. 예외 검증
         */
        when(userRepository.existsByEmail(joinRequestDto.getEmail())).thenReturn(true); //같은 이메일이 존재한다면
        assertThrows(EmailAlreadyExistsException.class, () -> authService.join(joinRequestDto));
    }
}