package com.example.springjwt.jwt;

import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static com.example.springjwt.jwt.JwtConst.*;

@Slf4j
@Component
public class JwtUtil {

    private final SecretKey secretKey;

    public JwtUtil(@Value("${spring.jwt.secret}") String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), MAC_ALGORITHM.key().build().getAlgorithm());
    }

    /**
     * Jwt Signature 의 위조 + 만료 검증
     */
    public Claims getClaims(String token) {//정보를 찾아오려면 시큐리티 키값이 필요함
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }
        /**
         * Jwt 토큰을 만들어서 반환하는 함수
         */
    public String createJwt(String category, String email, String role, Long expiredMs) {
        return Jwts.builder()
                .claim(CLAIM_CATEGORY, category) // (Access / Refresh)
                .claim(CLAIM_EMAIL, email) //표준 클레임
                .claim(CLAIM_ROLE, RoleUtil.toRole(role)) //payload 에 key - value 형태로 들어간다
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey) //signature 만들기
                .compact();
    }

    /**
     * JWT 를 HTTP 헤더에 추가하기
     */
    public void addJwtToHeader(HttpServletResponse response, String token) {
        response.addHeader(ACCESS_HEADER, BEARER_PREFIX + token);
    }

    /**
     * JWT 를 Cookie 에 저장하기
     */
    public void addJwtToCookie(HttpServletResponse response, String token) {
        try {
            token = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20"); // Cookie Value 에는 공백이 불가능해서 encoding 진행

            Cookie cookie = new Cookie(REFRESH_HEADER, token); // Name-Value
            cookie.setMaxAge(24 * 60 * 60);
            //setSecure(true) // 테스트를 위해 잠시 주석처리
            cookie.setPath("/");
            cookie.setHttpOnly(true);

            response.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
        }
    }
    public String getTokenFromHeader(HttpServletRequest request) {
        return request.getHeader(ACCESS_HEADER);
    }
}
