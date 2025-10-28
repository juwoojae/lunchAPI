package com.example.springjwt.service;

import com.example.springjwt.dto.JoinRequestDto;
import com.example.springjwt.dto.JoinResponseDto;
import com.example.springjwt.dto.RefreshResponseDto;
import com.example.springjwt.entity.UserEntity;
import com.example.springjwt.exception.ExpiredException;
import com.example.springjwt.exception.InvalidTokenException;
import com.example.springjwt.jwt.JwtUtil;
import com.example.springjwt.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.awt.*;

import static com.example.springjwt.jwt.JwtConst.*;

@Slf4j
@Service
@RequiredArgsConstructor
/**
 * 회원가입 서비스
 */
public class AuthService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public JoinResponseDto join(JoinRequestDto joinRequestDto) {
        String email = joinRequestDto.getEmail();
        String password = joinRequestDto.getPassword();
        String name = joinRequestDto.getName();
        String role = joinRequestDto.getRole();

        Boolean isExist = userRepository.existsByEmail(email);

        if (isExist) {  //해당 Email 이 존재하는 경우
            throw new DataIntegrityViolationException("이미 존재하는 이메일");
        }
        UserEntity data = new UserEntity(email, name, passwordEncoder.encode(password), role);
        userRepository.save(data);
        return new JoinResponseDto(data.getId(),
                data.getEmail(),
                data.getName(),
                data.getRole(),
                data.getCreatedDate());
    }

    /**
     * Refresh 토큰이 타당한지 검증하는 메서드
     * 1. 토큰이 null 인지
     * 2. refresh Rotate
     * 3. replay 공격 방지 -> db에 일시적으로 세션만들기 , refreshToken 관리
     */
    public RefreshResponseDto reissueToken(String refreshToken) {
        //refresh Token 검증로직
        if (validateToken(refreshToken)) throw new InvalidTokenException("타당하지 않은 토큰");
        if (jwtUtil.isExpired(refreshToken)) throw new ExpiredException("토큰이 만료되었습니다");
        //유저 정보
        String email = jwtUtil.getClaims(refreshToken).get(CLAIM_EMAIL, String.class);
        String role = jwtUtil.getClaims(refreshToken).get(CLAIM_ROLE, String.class);
        //토큰 생성
        String newAccessToken = jwtUtil.createJwt(CATEGORY_ACCESS, email, role, ACCESSTOKEN_TIME);//refresh 토큰 생성
        //refresh Rotate
        String newRefreshToken = jwtUtil.createJwt(CATEGORY_REFRESH, email, role, REFRESH_TOKEN_TIME);
        return new RefreshResponseDto(newAccessToken, newRefreshToken);
    }

    private boolean validateToken(String refreshToken) {

        return refreshToken == null ||
                !jwtUtil.getClaims(refreshToken).get(CLAIM_CATEGORY,String.class)
                        .equals(CATEGORY_REFRESH); //토큰의 카테고리가 Refresh 토큰이 아닌경우
    }
}