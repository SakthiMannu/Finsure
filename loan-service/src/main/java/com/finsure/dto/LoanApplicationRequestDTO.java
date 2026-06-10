package com.finsure.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanApplicationRequestDTO {

    @NotNull(message = "Please Provide the MemberId to proceed!!!")
    private Long memberId;

    @NotNull(message = "Please Enter the Amount!!!")
    @Positive(message = "Please Enter valid amount!!!")
    private double amount;

    @NotBlank(message = "Please state the purpose for applying Loan!!!")
    private String purpose;
    private String customPurpose;
}