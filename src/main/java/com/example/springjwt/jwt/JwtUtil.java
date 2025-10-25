package com.example.springjwt.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.MacAlgorithm;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;


@Component
public class JwtUtil {

    private static final String AUTHORIZATION_HEADER = "Authorization";//쿠키의 name 값
    //HMAC 알고리즘
    private static final MacAlgorithm MAC_ALGORITHM = Jwts.SIG.HS256;
    //사용자 id 의 key
    private static final String CLAIM_USERNAME = "username";
    //사용자 role 의 key
    private static final String CLAIM_ROLE = "role";
    //토큰 식별자
    private static final String BEARER_PREFIX = "Bearer "; // 규칙 토큰 앞에 붙이는것
    // 토큰 만료시간
    private final long TOKEN_TIME = 60 * 60 * 1000L; // 60분

    private final SecretKey secretKey;

    //secret 을 통해서 SecretKey 객체를 생성
    public JwtUtil(@Value("${spring.jwt.secret}") String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), MAC_ALGORITHM.key().build().getAlgorithm());
    }
    //Validate 검증용 메서드
    private Claims parseClaims(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseClaimsJws(token).getPayload();
    }

    public String getUsername(String token) {
        return parseClaims(token).get("username", String.class);
    }

    public String getRole(String token) {
        return parseClaims(token).get("role", String.class);
    }

    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public String cresteJwt(String username, String role) {
        return Jwts.builder()
                .claim(CLAIM_USERNAME, username)
                .claim(CLAIM_ROLE, role) //payload 에 key - value 형태로 들어간다
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + TOKEN_TIME))
                .signWith(secretKey) //signature 만들기
                .compact();
    }

    public void addJwtToHeader(HttpServletResponse response, String token) {
        response.addHeader(AUTHORIZATION_HEADER, BEARER_PREFIX + token);
    }
}
