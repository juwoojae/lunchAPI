package com.example.springjwt.repository;

import com.example.springjwt.dto.menu.CreateMenuRequest;
import com.example.springjwt.entity.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<MenuEntity, Integer> {
    List<MenuEntity> findByRoundEntity_Id(Long id);
}
