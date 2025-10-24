package com.example.springjwt.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 스프링 시큐리티의 인가 및 설정을 담당하는 클래스이다.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //csrf disable JWT 에서는 굳이 방어할 필요가 없다
        http
                .csrf((auth) -> auth.disable());
        //From 로그인 방식 disable
        http
                .httpBasic((auth) -> auth.disable());
        //글로벌 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/login", "/", "/join").permitAll() //모든 권한에 대해서 허용
                        .requestMatchers("/admin").hasRole("ADMIN")  //ADMIN 권한에 대해서만 허용
                        .anyRequest().authenticated()); //그 이외 로그인한 사용자에 대해 허용
        //세션 설정
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션을 Stateless 상태로 관리 (JWT)
                );

        return http.build();
    }
}
