package com.finsure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Entity
@Table(name = "loan_application")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loanId;



    private Long memberId;

    private double amount;
    private String purpose;
    private LocalDate submittedAt;


    private String status;

    private String approvedBy;


    private LocalDate nextDueDate;
    
    @Column(nullable = true, length = 500)
    private String customPurpose;

}
