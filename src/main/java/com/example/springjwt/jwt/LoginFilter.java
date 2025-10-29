package com.example.springjwt.jwt;

import com.example.springjwt.dto.CustomUserDetails;
import com.example.springjwt.dto.auth.LoginRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

import static com.example.springjwt.jwt.JwtConst.*;

@Slf4j(topic = "LoginFilter")
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager; //얘가 검증처리하는얘임
    private final JwtUtil jwtUtil;

    public LoginFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/auth/login"); //로그인 처리 (Post /api/user/login)
    }

    public LoginFilter(AuthenticationManager authenticationManager, AuthenticationManager authenticationManager1, JwtUtil jwtUtil) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager1;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("attemptAuthentication");
        LoginRequestDto requestDto = null;
        try {
            requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);
        } catch (IOException e) {
            log.info("ObjectMapper parse error");
            throw new RuntimeException(e.getMessage());
        }
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.getEmail(),
                        requestDto.getPassword(),
                        null
                )
        );
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();
        //유저 정보
        String email = customUserDetails.getUsername();
        String role = customUserDetails.getRole();
        //토큰 생성
        String accessToken = jwtUtil.createJwt(CATEGORY_ACCESS, email, role, ACCESSION_TIME);
        String refreshToken = jwtUtil.createJwt(CATEGORY_REFRESH, email, role, REFRESH_TOKEN_TIME);

        jwtUtil.addJwtToHeader(response, accessToken); //Access 토큰 Http헤더에 넣기
        jwtUtil.addJwtToCookie(response, refreshToken); //Refresh 토큰 쿠키에 넣기
    }

    //authenticationManger 의 검증이 실패했으면 실행하는 메서드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(401);
    }
}
