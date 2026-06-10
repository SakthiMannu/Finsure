package com.finsure.controller;

import com.finsure.dto.LoanDocumentRequestDTO;
import com.finsure.dto.LoanDocumentResponseDTO;
import com.finsure.service.LoanDocumentServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/loans/documents")
public class LoanDocumentController {

    @Autowired
    private LoanDocumentServiceImpl docService;


    @PostMapping
    public ResponseEntity<?> createLoanDocument(
            @RequestHeader("X-User-Role") String role,
            @Valid @RequestBody LoanDocumentRequestDTO dto) {

        if (!role.equals("MEMBER") && !role.equals("TELLER")
                && !role.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Only MEMBER, TELLER or ADMIN can upload documents");
        }
        return new ResponseEntity<>(
                docService.createLoanDocument(dto), HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<?> getAllDocuments(
            @RequestHeader("X-User-Role") String role) {

        if (!role.equals("LOAN_OFFICER") && !role.equals("ADMIN")
                && !role.equals("AUDITOR")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Insufficient role");
        }
        return new ResponseEntity<>(docService.getAllDocuments(), HttpStatus.OK);
    }


    @GetMapping("/{docId}")
    public ResponseEntity<?> getDocumentById(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long docId) {

        if (!role.equals("LOAN_OFFICER") && !role.equals("TELLER")
                && !role.equals("ADMIN") && !role.equals("AUDITOR")
                && !role.equals("MEMBER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Insufficient role");
        }
        return new ResponseEntity<>(
                docService.getDocumentById(docId), HttpStatus.OK);
    }


    @GetMapping("/loan/{loanId}")
    public ResponseEntity<?> getDocumentsByLoanId(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long loanId) {

        if (!role.equals("LOAN_OFFICER") && !role.equals("TELLER")
                && !role.equals("ADMIN") && !role.equals("AUDITOR")
                && !role.equals("MEMBER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Insufficient role");
        }
        return new ResponseEntity<>(
                docService.getDocumentsByLoanId(loanId), HttpStatus.OK);
    }


    @DeleteMapping("/{docId}")
    public ResponseEntity<?> deleteDocument(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long docId) {

        if (!role.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Only ADMIN can delete documents");
        }
        return new ResponseEntity<>(
                docService.deleteDocument(docId), HttpStatus.OK);
    }
}
