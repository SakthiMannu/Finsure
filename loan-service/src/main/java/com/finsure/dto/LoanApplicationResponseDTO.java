package com.finsure.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanApplicationResponseDTO {

    private Long loanId;
    private Long memberId;
    private double amount;
    private String purpose;
    private LocalDate submittedAt;
    private String status;
    private String approvedBy;
    private LocalDate nextDueDate;
    private String customPurpose; 
}
