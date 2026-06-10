package com.finsure.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanDocumentRequestDTO {

    @NotBlank(message = "Please Provide The File URI!!!!")
    private String fileURI;

    @NotNull(message = "Please Provide The Loan ID!!!!")
    private Long loanId;

}