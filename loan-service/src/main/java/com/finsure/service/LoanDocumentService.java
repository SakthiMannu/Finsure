package com.finsure.service;

import com.finsure.dto.LoanDocumentRequestDTO;
import com.finsure.dto.LoanDocumentResponseDTO;
import java.util.List;

public interface LoanDocumentService {
	
    LoanDocumentResponseDTO createLoanDocument(LoanDocumentRequestDTO dto);
    
    List<LoanDocumentResponseDTO> getAllDocuments();
    
    LoanDocumentResponseDTO getDocumentById(Long docId);
    
    List<LoanDocumentResponseDTO> getDocumentsByLoanId(Long loanId);
    
    String deleteDocument(Long docId);
}