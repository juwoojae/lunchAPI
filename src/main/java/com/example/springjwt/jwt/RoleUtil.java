package com.example.springjwt.jwt;

import java.util.Locale;

public class RoleUtil {

    private static final String DEFAULT_ROLE = "ROLE_USER";   //role 이 주어지지 않을 경우
    private static final String PREFIX = "ROLE_";   //접두사

    public static String toRole(String role) {
        if (role == null || role.isBlank()) {
            return DEFAULT_ROLE;    //디폴트
        }
        return PREFIX + role.toUpperCase(Locale.ROOT);
    }
}
