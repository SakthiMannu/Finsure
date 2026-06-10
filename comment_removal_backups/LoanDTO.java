package com.finsure.dto;

import lombok.Data;
import java.time.LocalDate;



@Data
public class LoanDTO {
    private Long loanId;
    private Long memberId;
    private double amount;
    private String purpose;
    private LocalDate submittedAt;
    private String status;
    private String approvedBy;
    private LocalDate nextDueDate;
}
