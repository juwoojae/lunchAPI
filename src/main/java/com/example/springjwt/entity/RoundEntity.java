package com.example.springjwt.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.*;


@Getter
@Entity
@Table(name = "round")
@NoArgsConstructor
public class RoundEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(unique = true)
    private LocalDate date; //만약 라운드를 추가하는데 같은 날이 있다면 막아야됨

    public RoundEntity(UserEntity user, LocalDate date) {
        this.user = user;
        this.date = date;
    }
}
