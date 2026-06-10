package com.finsure.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;



@Entity
@Table(name = "delinquency")
@Data
public class DelinquencyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delinq_id")
    private Long delinqId;

    @Column(name = "loan_id", nullable = false)
    private Long loanId;

    @Column(name = "detected_at", nullable = false)
    private LocalDateTime detectedAt;


    @Column(nullable = false)
    private String status;

    @Column(length = 500)
    private String notes;
}
