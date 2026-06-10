package com.finsure.controller;

import com.finsure.dto.DelinquencyRequestDTO;
import com.finsure.dto.DelinquencyResponseDTO;
import com.finsure.service.DelinquencyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/delinquencies")
public class DelinquencyController {

    @Autowired
    private DelinquencyServiceImpl delinquencyService;


    @PostMapping
    public ResponseEntity<?> createDelinquency(
            @RequestHeader("X-User-Role") String role,
            @RequestParam Long loanId,
            @RequestBody DelinquencyRequestDTO dto) {

        if (!role.equals("LOAN_OFFICER") && !role.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Only LOAN_OFFICER or ADMIN can create delinquency records");
        }
        return new ResponseEntity<>(
                delinquencyService.createDelinquency(loanId, dto),
                HttpStatus.CREATED);
    }


    @GetMapping("/loan/{loanId}")
    public ResponseEntity<?> getByLoanId(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long loanId) {

        if (!role.equals("LOAN_OFFICER") && !role.equals("BRANCH_MANAGER")
                && !role.equals("ADMIN") && !role.equals("AUDITOR")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Insufficient role");
        }
        return ResponseEntity.ok(
                delinquencyService.getDelinquenciesByLoanId(loanId));
    }


    @GetMapping("/{delinqId}")
    public ResponseEntity<?> getById(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long delinqId) {

        if (!role.equals("LOAN_OFFICER") && !role.equals("BRANCH_MANAGER")
                && !role.equals("ADMIN") && !role.equals("AUDITOR")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Insufficient role");
        }
        return ResponseEntity.ok(
                delinquencyService.getDelinquencyById(delinqId));
    }


    @PutMapping("/{delinqId}")
    public ResponseEntity<?> updateDelinquency(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long delinqId,
            @RequestBody DelinquencyRequestDTO dto) {

        if (!role.equals("LOAN_OFFICER") && !role.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Only LOAN_OFFICER or ADMIN can update delinquency records");
        }
        return ResponseEntity.ok(
                delinquencyService.updateDelinquency(delinqId, dto));
    }


    @DeleteMapping("/{delinqId}")
    public ResponseEntity<?> deleteDelinquency(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long delinqId) {

        if (!role.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Only ADMIN can delete delinquency records");
        }
        return ResponseEntity.ok(
                delinquencyService.deleteDelinquency(delinqId));
    }
}
