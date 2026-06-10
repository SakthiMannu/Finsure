package com.finsure.service;

import com.finsure.dto.DelinquencyRequestDTO;
import com.finsure.dto.DelinquencyResponseDTO;
import java.util.List;

public interface DelinquencyService {
    DelinquencyResponseDTO createDelinquency(Long loanId,
            DelinquencyRequestDTO dto);
    List<DelinquencyResponseDTO> getDelinquenciesByLoanId(Long loanId);
    DelinquencyResponseDTO getDelinquencyById(Long delinqId);
    DelinquencyResponseDTO updateDelinquency(Long delinqId,
            DelinquencyRequestDTO dto);
    String deleteDelinquency(Long delinqId);
}