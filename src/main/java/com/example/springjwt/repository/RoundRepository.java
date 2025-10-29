package com.example.springjwt.repository;

import com.example.springjwt.entity.RoundEntity;
import org.hibernate.mapping.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

public interface RoundRepository extends JpaRepository<RoundEntity, Long> {

    boolean existsByDate(LocalDate date);//해당 날짜가 Round 테이블 컬럼중 row 에 존재하는지

    @Transactional
    Optional<RoundEntity> findByDate(LocalDate date); //해당 날짜를 가지는 컬럼 반환

    @Transactional
    void deleteById(Long roundId);
}
