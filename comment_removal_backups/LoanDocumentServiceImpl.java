package com.finsure.service;

import com.finsure.dto.LoanDocumentRequestDTO;
import com.finsure.dto.LoanDocumentResponseDTO;
import com.finsure.entity.LoanApplication;
import com.finsure.entity.LoanDocument;
import com.finsure.exception.DocumentNotFoundException;
import com.finsure.exception.LoanNotFoundException;
import com.finsure.mapper.LoanDocumentMapper;
import com.finsure.repository.LoanApplicationRepository;
import com.finsure.repository.LoanDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanDocumentServiceImpl implements LoanDocumentService {

    @Autowired
    private LoanDocumentRepository docRepo;

    @Autowired
    private LoanApplicationRepository loanRepo;

    @Autowired
    private LoanDocumentMapper docMapper;

    @Transactional
    public LoanDocumentResponseDTO createLoanDocument(LoanDocumentRequestDTO dto) {


        LoanApplication loan = loanRepo.findById(dto.getLoanId())
                .orElseThrow(() -> new LoanNotFoundException(
                        "No Loan Found With ID: " + dto.getLoanId()));

        LoanDocument doc = docMapper.toEntity(dto);
        doc.setLoan(loan);
        doc.setStatus("PENDING");
        doc.setUploadedAt(LocalDate.now());

        return docMapper.toResponseDTO(docRepo.save(doc));
    }

    public List<LoanDocumentResponseDTO> getAllDocuments() {
        return docRepo.findAll()
                .stream()
                .map(docMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public LoanDocumentResponseDTO getDocumentById(Long docId) {
        LoanDocument doc = docRepo.findById(docId)
                .orElseThrow(() -> new DocumentNotFoundException(
                        "No Document Found With ID: " + docId));
        return docMapper.toResponseDTO(doc);
    }

    public List<LoanDocumentResponseDTO> getDocumentsByLoanId(Long loanId) {


        loanRepo.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException(
                        "No Loan Found With ID: " + loanId));

        List<LoanDocument> docs = docRepo.findByLoan_LoanId(loanId);
        if (docs.isEmpty()) {
            throw new DocumentNotFoundException(
                    "No Documents Found For Loan ID: " + loanId);
        }

        return docs.stream()
                .map(docMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public String deleteDocument(Long docId) {
        docRepo.findById(docId)
                .orElseThrow(() -> new DocumentNotFoundException(
                        "No Document Found With ID: " + docId));
        docRepo.deleteById(docId);
        return "Document ID: " + docId + " deleted successfully!!";
    }
}
