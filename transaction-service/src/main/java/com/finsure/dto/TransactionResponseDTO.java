package com.finsure.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TransactionResponseDTO {
    private Long txnId;
    private Long accountId;
    private String type;
    private Double amount;
    private String performedBy;
    private LocalDateTime performedAt;
    private String status;
}