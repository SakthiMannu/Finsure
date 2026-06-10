package com.finsure.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RepaymentResponseDTO {
    private Long repaymentId;
    private Long loanId;
    private Double amount;
    private LocalDateTime paidAt;
    private String method;
    private String status;
}