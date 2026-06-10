package com.finsure.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanDocumentResponseDTO {

    private Long docId;
    private Long loanId;
    private String fileURI;
    private LocalDate uploadedAt;
    private String verifiedBy;
    private String status;

}