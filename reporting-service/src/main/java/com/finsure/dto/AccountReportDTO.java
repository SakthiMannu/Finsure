package com.finsure.dto;

import lombok.Data;

@Data
public class AccountReportDTO {
    private Long accountId;
    private Long memberId;
    private String type; 
    private double balance;
    private String status; 
}
