package com.finsure.dto;

import lombok.Data;


@Data
public class LoanReportDTO {
    private Long loanId;
    private Long memberId;
    private double amount;
    private String status; 
    private String purpose;
}

