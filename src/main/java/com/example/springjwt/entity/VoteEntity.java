package com.example.springjwt.entity;

import jakarta.persistence.*;
@Table(name = "vote")
@Entity
public class VoteEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
