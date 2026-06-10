package com.finsure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "account")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;


    private String type;

    private double balance;
    private LocalDate createdAt;


    private String status;


    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}

