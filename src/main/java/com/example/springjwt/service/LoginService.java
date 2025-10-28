package com.example.springjwt.service;

import com.example.springjwt.dto.JoinRequestDto;
import com.example.springjwt.dto.JoinResponseDto;
import com.example.springjwt.entity.UserEntity;
import com.example.springjwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
/**
 * 회원가입 서비스
 */
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public JoinResponseDto joinProcess(JoinRequestDto joinRequestDto) {
        String email = joinRequestDto.getEmail();
        String password = joinRequestDto.getPassword();
        String name = joinRequestDto.getName();
        String role = joinRequestDto.getRole();

        Boolean isExist = userRepository.existsByEmail(email);

        if (isExist) {  //해당 Email 이 존재하는 경우
            throw new DataIntegrityViolationException("이미 존재하는 이메일");
        }
        UserEntity data = new UserEntity(email, name, passwordEncoder.encode(password), role);
        userRepository.save(data);
        return new JoinResponseDto(data.getId(),
                data.getEmail(),
                data.getName(),
                data.getRole(),
                data.getCreatedDate());
    }
}
