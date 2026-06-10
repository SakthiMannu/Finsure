package com.finsure.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DelinquencyResponseDTO {
    private Long delinqId;
    private Long loanId;
    private LocalDateTime detectedAt;
    private String status;
    private String notes;
}