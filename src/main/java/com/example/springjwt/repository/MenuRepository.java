package com.example.springjwt.repository;

import com.example.springjwt.dto.menu.CreateMenuRequest;
import com.example.springjwt.entity.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MenuRepository extends JpaRepository<MenuEntity, Long> {

    List<MenuEntity> findByRound_Id(Long id);

    @Transactional
    void deleteAllByRound_Id(Long roundEntityId);

}
