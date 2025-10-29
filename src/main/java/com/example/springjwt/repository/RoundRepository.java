package com.example.springjwt.repository;

import com.example.springjwt.entity.RoundEntity;
import org.hibernate.mapping.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

public interface RoundRepository extends JpaRepository<RoundEntity, Long> {

    boolean existsByDate(LocalDate date);

    Optional<RoundEntity> findByDate(LocalDate date);
}
