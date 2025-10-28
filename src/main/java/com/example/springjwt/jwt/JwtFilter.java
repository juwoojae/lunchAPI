package com.example.springjwt.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
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

        Claims claims ;

        if (StringUtils.hasText(tokenValue)) {
            String token = tokenValue.split(" ")[1]; //식별자 걷어내기 subString
            try {
                claims = jwtUtil.getClaims(token);
            } catch (ExpiredJwtException e) {  //만약 토큰이 만료가 되었다면
                log.error("Token expired");
                return;
            } catch (JwtException e) {  //토큰이 위조 되었다면
                log.error("Invalid token");
                return;
            }
            String email = claims.get("email", String.class);
            try {
                setAuthentication(email);
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e.getMessage());
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

        // 인증 처리
        public void setAuthentication (String username){
            SecurityContext context = SecurityContextHolder.createEmptyContext();
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
