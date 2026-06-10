package com.finsure.dto;

import lombok.Data;

@Data
public class TransactionReportDTO {
    private Long txnId;
    private Long accountId;
    private String type; 
    private double amount;
    private String status;
}
