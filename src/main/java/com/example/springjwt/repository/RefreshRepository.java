package com.example.springjwt.repository;

import com.example.springjwt.entity.refresh.RefreshEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshRepository extends JpaRepository<RefreshEntity, Long> {

    Boolean existsByRefreshToken(String refreshToken);
    @Transactional
    void deleteByRefreshToken(String refresh);
}
