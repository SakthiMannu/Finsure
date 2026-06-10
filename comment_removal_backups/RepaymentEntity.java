package com.finsure.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "repayment",
indexes = {
@Index(name = "idx_loan_status", columnList = "loan_id, status")
    })
@Data
public class RepaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long repaymentId;



    @Column(name = "loan_id", nullable = false)
    private Long loanId;

    @Column(nullable = false)
    private Double amount;

    @Column(name = "paid_at", nullable = false)
    private LocalDateTime paidAt;


    @Column(nullable = false)
    private String method;


    @Column(nullable = false)
    private String status;
}
