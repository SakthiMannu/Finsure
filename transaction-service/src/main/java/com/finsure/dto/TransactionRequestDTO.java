package com.finsure.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class TransactionRequestDTO {

    @NotNull
    private Long accountId;

    @NotNull
    @Positive(message = "Amount must be greater than zero")
    @DecimalMax(value = "1000000.00",
                message = "Maximum transaction limit is Rs.10,00,000")
    private Double amount;

    private String performedByEmail;
    private String performedByRole;
}