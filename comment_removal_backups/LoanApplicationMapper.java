package com.finsure.mapper;

import com.finsure.dto.LoanApplicationRequestDTO;
import com.finsure.dto.LoanApplicationResponseDTO;
import com.finsure.entity.LoanApplication;
import org.springframework.stereotype.Component;

@Component
public class LoanApplicationMapper {


    public LoanApplication toEntity(LoanApplicationRequestDTO dto) {
        LoanApplication loan = new LoanApplication();
        loan.setAmount(dto.getAmount());
        loan.setPurpose(dto.getPurpose());
        loan.setMemberId(dto.getMemberId());
        return loan;
    }


    public LoanApplicationResponseDTO toResponseDTO(LoanApplication loan) {
        LoanApplicationResponseDTO dto = new LoanApplicationResponseDTO();
        dto.setLoanId(loan.getLoanId());
        dto.setMemberId(loan.getMemberId());
        dto.setAmount(loan.getAmount());
        dto.setPurpose(loan.getPurpose());
        dto.setSubmittedAt(loan.getSubmittedAt());
        dto.setStatus(loan.getStatus());
        dto.setApprovedBy(loan.getApprovedBy());
        dto.setNextDueDate(loan.getNextDueDate());
        dto.setCustomPurpose(loan.getCustomPurpose());
        return dto;
    }
}
