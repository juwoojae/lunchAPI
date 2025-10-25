package com.example.springjwt.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JwtFilter")
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //request 에서 Authorization 헤더를 찾음
        String tokenValue = jwtUtil.getTokenFromHeader(request);
        if (StringUtils.hasText(tokenValue)) {
            // JWT 토큰 substring
            String token = tokenValue.split(" ")[1]; //식별자 걷어내기
            log.info("token: {}", token);
            if (!jwtUtil.validateToken(token)) { //토큰이 위조되지 않았는지 확인
                log.error("Token Error");
                return;
            }
            String username = jwtUtil.getUsername(token);
            try {

                setAuthentication(username); //여기서 문제
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }
        log.info("doFilter (next)");
        filterChain.doFilter(request, response);
    }
        // 인증 처리
        public void setAuthentication (String username){
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            //인증 Filter 에서는 권한 까지는 토큰에 담기지 않았지만 여기서는 토큰에 권한을 담는다
            Authentication authentication = createAuthentication(username); //유저 정보로 인증 객체 생성

            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context); //현재 요청을 인증된 상태로 설정
        }

        // 인증 객체 생성
        private Authentication createAuthentication (String username){
            UserDetails userDetails = userDetailsService.loadUserByUsername(username); //username 을 DB 에서 찾아 올때
            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());  //인가
        }
    }
