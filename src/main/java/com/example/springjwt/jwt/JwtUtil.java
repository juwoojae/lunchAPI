package com.example.springjwt.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.MacAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
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
    public static final String BEARER_PREFIX = "Bearer "; // 규칙 토큰 앞에 붙이는것
    // 토큰 만료시간
    private final long TOKEN_TIME = 60 * 60 * 1000L; // 60분

    private final SecretKey secretKey;

    //secret 을 통해서 SecretKey 객체를 생성
    public JwtUtil(@Value("${spring.jwt.secret}") String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), MAC_ALGORITHM.key().build().getAlgorithm());
    }

    public boolean validateToken(String token) {
        try {
            //토큰의 위/변조가 있는지, 만료가 되지는 않았는지 확인할수 있다
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    public String createJwt(String username, String role) {
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

    // 토큰에서 사용자 정보 가져오기 Payload 가지고 오기
    public Claims getClaimsFromToken(String token) {//정보를 찾아오려면 시큐리티 키값이 필요함
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }
    public String getUsername(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
    }

    public String getRole(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }


    public String getTokenFromHeader(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION_HEADER);
    }
}
