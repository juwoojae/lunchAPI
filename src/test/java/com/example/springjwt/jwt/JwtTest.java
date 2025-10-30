package com.example.springjwt.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtTest {

    private JwtUtil jwtUtil;

    private final String secret = "my-super-secret-key-my-super-secret-key";

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(secret);
    }

    @Test
    void 정상_토큰_검증(){
        // given
        String token = jwtUtil.createJwt("ACCESS", "test@example.com", "USER", 1000L * 60);

        //when
        Claims claims = jwtUtil.getClaims(token); //토큰 위조 + 만료 검증

        //then
        assertEquals("ACCESS", claims.get("category", String.class));
        assertEquals("test@example.com",  claims.get("email", String.class));
    }

    @Test
    void 만료된_토큰_검증() throws InterruptedException {

        // given 만료시간이 1초라면
        String token = jwtUtil.createJwt("ACCESS", "test@example.com", "USER", 1000L);

        //wait 1.1 초후
        Thread.sleep(1100);

        //when & then
        assertThrows(ExpiredJwtException.class, () -> jwtUtil.getClaims(token));
    }

    @Test
    void 위조된_토큰_검증(){
        //given
        JwtUtil fakeJwtUtil = new JwtUtil("fake-secret-key-that-is-different123456");
        String fakeToken = fakeJwtUtil.createJwt("ACCESS", "test@example.com", "USER", 1000L * 60);

        //when & then
        assertThrows(SignatureException.class, () -> jwtUtil.getClaims(fakeToken));
    }
}