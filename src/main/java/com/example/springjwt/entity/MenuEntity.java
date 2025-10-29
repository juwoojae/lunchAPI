package com.example.springjwt.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@Table(name = "menu")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "round_id")
    private RoundEntity roundEntity;

    private String name;

    private String type;

    private Integer price;

    public MenuEntity(RoundEntity roundEntity, String name, Integer price, String type) {
        this.roundEntity = roundEntity;
        this.name = name;
        this.price = price;
        this.type = type;
    }
}
