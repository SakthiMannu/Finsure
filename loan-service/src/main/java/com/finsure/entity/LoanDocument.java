package com.finsure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "loan_document")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long docId;

    private String fileURI;
    private LocalDate uploadedAt;
    private String verifiedBy;


    private String status;


    @ManyToOne
    @JoinColumn(name = "loan_id")
    private LoanApplication loan;

}
