package com.finsure.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;




@Entity
@Table(name = "transactions")
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long txnId;


    private Long accountId;


    @Enumerated(EnumType.STRING)
    private TxnType type;

    private Double amount;


    private String performedBy;

    private LocalDateTime performedAt;


    private String status;


    public enum TxnType {
        DEPOSIT, WITHDRAWAL
    }
}
