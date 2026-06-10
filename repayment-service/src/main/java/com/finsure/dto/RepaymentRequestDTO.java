package com.finsure.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RepaymentRequestDTO {

    @NotNull(message = "Loan ID is required")
    private Long loanId;

    @NotNull(message = "Amount is required")
    @Min(value = 1, message = "Amount must be greater than 0")
    private Double amount;

    @NotBlank(message = "Payment method is required")
    private String method;

    @NotBlank(message = "Status is required")
    private String status;
}