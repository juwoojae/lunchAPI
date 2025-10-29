package com.example.springjwt.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;

public interface JwtConst {

    String ACCESS_HEADER = "Authorization";

    String REFRESH_HEADER = "refresh";
    //HMAC 알고리즘
    MacAlgorithm MAC_ALGORITHM = Jwts.SIG.HS256;


    //사용자 Category 의 key
    String CLAIM_CATEGORY = "category";
    //사용자 email 의 key
    String CLAIM_EMAIL = "email";
    //사용자 role 의 key
    String CLAIM_ROLE = "role";


    //토큰 식별자
    String BEARER_PREFIX = "Bearer "; // 규칙 토큰 앞에 붙이는것
    // 토큰 만료시간
    long ACCESSION_TIME = 60 * 60 * 1000L; // 1시간

    long REFRESH_TOKEN_TIME = 7*24 * 60 * 60 * 1000L; //7일

    //토큰 카테고리
    String CATEGORY_ACCESS = "access";
    String CATEGORY_REFRESH = "refresh";
}
