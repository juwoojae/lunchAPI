package com.example.springjwt.config;

import com.example.springjwt.jwt.JwtFilter;
import com.example.springjwt.jwt.JwtUtil;
import com.example.springjwt.jwt.LoginFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.file.ConfigurationSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

import static com.example.springjwt.jwt.JwtConst.ACCESS_HEADER;

/**
 * 스프링 시큐리티의 인가 및 설정을 담당하는 클래스이다.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final UserDetailsService userDetailsService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //CORS 설정
        http
                .cors((cors) -> cors
                        .configurationSource(new CorsConfigurationSource() {

                            @Override
                            public CorsConfiguration getCorsConfiguration(HttpServletRequest request){
                                CorsConfiguration configuration = new CorsConfiguration();

                                configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                                configuration.setAllowedMethods(Collections.singletonList("*"));
                                configuration.setAllowCredentials(true);
                                configuration.setAllowedHeaders(Collections.singletonList("*"));
                                configuration.setMaxAge(3600L);
                                configuration.setExposedHeaders(Collections.singletonList(ACCESS_HEADER));

                                return configuration;
                            }
                        }));
        //csrf disable JWT 에서는 굳이 방어할 필요가 없다
        http
                .csrf((auth) -> auth.disable());
        //From 로그인 방식 disable
        http
                .httpBasic((auth) -> auth.disable());
        //글로벌 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/api/auth/**").permitAll() //모든 권한에 대해서 허용
                        .requestMatchers("/admin").hasRole("ADMIN")  //ADMIN 권한에 대해서만 허용
                        .anyRequest().authenticated()); //그 이외 로그인한 사용자에 대해 허용

        http
                .addFilterBefore(new JwtFilter(jwtUtil, userDetailsService), LoginFilter.class);
        http //로그인 필터 추가
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);

        //세션 설정
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션을 Stateless 상태로 관리 (JWT)
                );

        return http.build();
    }
}

