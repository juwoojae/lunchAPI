package com.example.springjwt.service;

import com.example.springjwt.dto.auth.JoinRequestDto;
import com.example.springjwt.dto.auth.JoinResponseDto;
import com.example.springjwt.dto.RefreshResponseDto;
import com.example.springjwt.entity.UserEntity;
import com.example.springjwt.entity.refresh.RefreshEntity;
import com.example.springjwt.exception.EmailAlreadyExistsException;
import com.example.springjwt.exception.ExpiredException;
import com.example.springjwt.exception.InvalidTokenException;
import com.example.springjwt.exception.TokenMissingException;
import com.example.springjwt.jwt.JwtUtil;
import com.example.springjwt.repository.RefreshRepository;
import com.example.springjwt.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

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
    private final RefreshRepository refreshRepository;

    @Transactional
    public JoinResponseDto join(JoinRequestDto joinRequestDto) {
        String email = joinRequestDto.getEmail();
        String password = joinRequestDto.getPassword();
        String name = joinRequestDto.getName();
        String role = joinRequestDto.getRole();

        Boolean isExist = userRepository.existsByEmail(email);

        if (isExist) {  //해당 Email 이 존재하는 경우
            throw new EmailAlreadyExistsException("이미 존재하는 이메일");
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
     * 2. refresh Rotate 로 보안성 강화
     * 3. replay 공격(Refresh 토큰이 탈취된경우) 방지 -> DB 에 refresh 토큰을 관리, reissue 로직을 실행하면
     * 이전의 refresh 토큰들에 대한 정보는 DB 에서 말소하기, DB 에서 관리하지 않는 refresh 토큰으로 접근하는 경우 방지
     */

    public RefreshResponseDto reissueToken(String refreshToken) {
        //refresh Token 검증로직
        validateToken(refreshToken);

        //DB에 저장되어 있는지 확인
        Boolean isExist = refreshRepository.existsByRefreshToken(refreshToken);
        if (!isExist) {

            //response body
            throw new InvalidTokenException("해당 토큰이 DB 에 없음");
        }


        //유저 정보
        String email = jwtUtil.getClaims(refreshToken).get(CLAIM_EMAIL, String.class);
        String role = jwtUtil.getClaims(refreshToken).get(CLAIM_ROLE, String.class);
        //토큰 생성
        String newAccessToken = jwtUtil.createJwt(CATEGORY_ACCESS, email, role, ACCESSION_TIME);//refresh 토큰 생성
        //refresh Rotate
        String newRefreshToken = jwtUtil.createJwt(CATEGORY_REFRESH, email, role, REFRESH_TOKEN_TIME);

        refreshRepository.existsByRefreshToken(refreshToken); //DB 의 refresh 토큰 삭제
        addRefreshEntity(email, role, REFRESH_TOKEN_TIME);

        return new RefreshResponseDto(newAccessToken, newRefreshToken);
    }

    private void validateToken(String refreshToken) {
        if (refreshToken == null) {
            throw new TokenMissingException("토큰 유실");   //403
        }
        try {
            Claims claims =  jwtUtil.getClaims(refreshToken); //토큰만료 + 위조 + 손상 검증

            if (!jwtUtil.getClaims(refreshToken).get(CLAIM_CATEGORY, String.class)
                    .equals(CATEGORY_REFRESH)) {
                throw new InvalidTokenException("토큰 위조+손상");  //401
            }
        } catch (ExpiredJwtException e) {
            log.error("expired JWT Token");
            throw new ExpiredException("토큰이 만료되었음");  //토큰 만료 401
        } catch (JwtException e) {
            log.error("Invalid token");
            throw new InvalidTokenException("토큰이 위조되었음");  //토큰이 위조/손상 401
        }
    }

    private void addRefreshEntity(String email, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshEntity refreshEntity = new RefreshEntity(refresh, date.toString(), email);
        refreshRepository.save(refreshEntity);
    }
}