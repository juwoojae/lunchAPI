package com.example.springjwt.repository;

import com.example.springjwt.entity.MenuEntity;
import com.example.springjwt.entity.RoundEntity;
import com.example.springjwt.entity.UserEntity;
import com.example.springjwt.entity.VoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface VoteRepository extends JpaRepository<VoteEntity, Long> {

    /**
     * 만약 user 가 해당 menu 에 투표한적이 있는지
     */
    @Transactional
    boolean existsByUserAndMenu(UserEntity user, MenuEntity menu);
    @Transactional
    List<VoteEntity> findAllByUserAndMenu_Round_Id(UserEntity user, Long menuRoundEntityId);
    @Transactional
    List<VoteEntity> findAllByUserAndMenu_Round_Date(UserEntity user, LocalDate menuRoundEntityDate);

    //집계 함수
    @Transactional
    int countByMenu_Id(Long menuId);
    @Transactional
    int countByUserAndMenu_Round_Id(UserEntity user,Long roundId);

}
