package com.finsure.mapper;

import com.finsure.dto.LoanDocumentRequestDTO;
import com.finsure.dto.LoanDocumentResponseDTO;
import com.finsure.entity.LoanDocument;
import org.springframework.stereotype.Component;

@Component
public class LoanDocumentMapper {


    public LoanDocument toEntity(LoanDocumentRequestDTO dto) {
        LoanDocument doc = new LoanDocument();
        doc.setFileURI(dto.getFileURI());
        return doc;
    }


    public LoanDocumentResponseDTO toResponseDTO(LoanDocument doc) {
        LoanDocumentResponseDTO res = new LoanDocumentResponseDTO();
        res.setDocId(doc.getDocId());
        res.setFileURI(doc.getFileURI());
        res.setStatus(doc.getStatus());
        res.setUploadedAt(doc.getUploadedAt());
        res.setVerifiedBy(doc.getVerifiedBy());
        res.setLoanId(doc.getLoan().getLoanId());
        return res;
    }
}
