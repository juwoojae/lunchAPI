package com.example.springjwt.controller;

import com.example.springjwt.dto.auth.JoinRequestDto;
import com.example.springjwt.dto.auth.JoinResponseDto;
import com.example.springjwt.dto.RefreshResponseDto;
import com.example.springjwt.jwt.JwtUtil;
import com.example.springjwt.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.springjwt.jwt.JwtConst.*;

@Slf4j(topic = "AuthController")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    /**
     * 회원 가입처리 컨트롤러
     */
    @PostMapping("/join")
    public ResponseEntity<JoinResponseDto> joinProcess(@RequestBody JoinRequestDto joinRequestDto) {
        log.info("joinProcess {} {}", joinRequestDto.getEmail(), joinRequestDto.getPassword());
        JoinResponseDto result = authService.join(joinRequestDto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
    /**
     * refresh 토큰 재발행 컨트롤러
     */
    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponseDto> refreshProcess(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = getRefreshTokenFromCookies(request);//쿠키에서 refreshToken 꺼내기
        RefreshResponseDto result = authService.reissueToken(refreshToken);//서비스로직실행후, refreshToken,AccessToken 재발급후 리턴
        //refresh 토큰 재발행후 쿠키에 넣기, access 토큰 재발행후 헤더에 넣기.
        jwtUtil.addJwtToHeader(response, result.getAccessToken());
        jwtUtil.addJwtToCookie(response, result.getRefreshToken());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 쿠키에서 refresh 토큰을 가지고 오는 헬퍼메서드
     */
    private String getRefreshTokenFromCookies(HttpServletRequest request) {
        String refreshToken = null;  //이것도 JwtUtil 에 넣기
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals(REFRESH_HEADER)) {
                refreshToken = cookie.getValue();
            }
        }
        return refreshToken;
    }



}
