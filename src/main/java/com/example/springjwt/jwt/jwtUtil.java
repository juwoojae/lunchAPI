package com.example.springjwt.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class jwtUtil {

    private SecretKey secretKey;
    //secret 을 통해서 SecretKey 객체를 생성
    public jwtUtil(@Value("%{spring.jwt.secret}") String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }
    //Validate 용 메서드들
    public String getUsername(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseClaimsJws(token).getPayload().get("username", String.class);
    }
    public String getRole(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseClaimsJws(token).getPayload().get("role", String.class);
    }
    public Boolean isExpired(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }
    public String cresteJwt(String username, String role, Long expiredMs){
        return Jwts.builder()
                .claim("username", username)
                .claim("role", role) //payload 에 key - value 형태로 들어간다
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey) //signature 만들기
                .compact();
    }
}
