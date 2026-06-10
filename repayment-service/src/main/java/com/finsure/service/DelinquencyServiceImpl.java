package com.finsure.service;

import com.finsure.client.LoanClient;
import com.finsure.dto.DelinquencyRequestDTO;
import com.finsure.dto.DelinquencyResponseDTO;
import com.finsure.dto.LoanDTO;
import com.finsure.entity.DelinquencyEntity;
import com.finsure.exception.InvalidDelinquencyIdException;
import com.finsure.exception.InvalidLoanIdException;
import com.finsure.repository.DelinquencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DelinquencyServiceImpl implements DelinquencyService {

    @Autowired
    private DelinquencyRepository delinquencyRepo;

    @Autowired
    private LoanClient loanClient;

    @Transactional
    public DelinquencyResponseDTO createDelinquency(
            Long loanId, DelinquencyRequestDTO dto) {


        LoanDTO loan = loanClient.getLoanById(loanId);
        if (loan == null) {
            throw new InvalidLoanIdException(
                    "Loan Not Found With ID: " + loanId);
        }


        if (delinquencyRepo.existsByLoanId(loanId)) {
            throw new RuntimeException(
                    "Delinquency record already exists for Loan ID: " + loanId);
        }

        DelinquencyEntity entity = new DelinquencyEntity();
        entity.setLoanId(loanId);
        entity.setDetectedAt(LocalDateTime.now());
        entity.setStatus(dto.getStatus());
        entity.setNotes(dto.getNotes());

        return mapToResponseDTO(delinquencyRepo.save(entity));
    }

    public List<DelinquencyResponseDTO> getDelinquenciesByLoanId(Long loanId) {
        List<DelinquencyEntity> entities = delinquencyRepo.findByLoanId(loanId);
        if (entities.isEmpty()) {
            throw new InvalidLoanIdException(
                    "No Delinquency Records Found For Loan ID: " + loanId);
        }
        return entities.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public DelinquencyResponseDTO getDelinquencyById(Long delinqId) {
        DelinquencyEntity entity = delinquencyRepo.findById(delinqId)
                .orElseThrow(() -> new InvalidDelinquencyIdException(
                        "Delinquency Not Found With ID: " + delinqId));
        return mapToResponseDTO(entity);
    }

    @Transactional
    public DelinquencyResponseDTO updateDelinquency(
            Long delinqId, DelinquencyRequestDTO dto) {

        DelinquencyEntity entity = delinquencyRepo.findById(delinqId)
                .orElseThrow(() -> new InvalidDelinquencyIdException(
                        "Delinquency Not Found With ID: " + delinqId));

        entity.setStatus(dto.getStatus());
        entity.setNotes(dto.getNotes());

        return mapToResponseDTO(delinquencyRepo.save(entity));
    }

    @Transactional
    public String deleteDelinquency(Long delinqId) {
        delinquencyRepo.findById(delinqId)
                .orElseThrow(() -> new InvalidDelinquencyIdException(
                        "Delinquency Not Found With ID: " + delinqId));
        delinquencyRepo.deleteById(delinqId);
        return "Delinquency Record Deleted Successfully";
    }

    private DelinquencyResponseDTO mapToResponseDTO(DelinquencyEntity entity) {
        DelinquencyResponseDTO dto = new DelinquencyResponseDTO();
        dto.setDelinqId(entity.getDelinqId());
        dto.setLoanId(entity.getLoanId());
        dto.setDetectedAt(entity.getDetectedAt());
        dto.setStatus(entity.getStatus());
        dto.setNotes(entity.getNotes());
        return dto;
    }
}
