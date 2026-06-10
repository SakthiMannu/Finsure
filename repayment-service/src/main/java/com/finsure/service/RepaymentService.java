package com.finsure.service;

import java.time.LocalDateTime;
import java.util.List;

import com.finsure.dto.RepaymentRequestDTO;
import com.finsure.dto.RepaymentResponseDTO;

public interface RepaymentService {
    RepaymentResponseDTO createRepayment(RepaymentRequestDTO dto);
    List<RepaymentResponseDTO> getRepaymentHistory(Long loanId);
    RepaymentResponseDTO getRepaymentById(Long repaymentId);
    RepaymentResponseDTO updateRepaymentById(Long repaymentId, RepaymentRequestDTO dto);
    String deleteRepaymentById(Long repaymentId);
    List<RepaymentResponseDTO> getRepaymentsByPeriod(
            LocalDateTime start, LocalDateTime end);
    List<RepaymentResponseDTO> getAllRepayments();
}