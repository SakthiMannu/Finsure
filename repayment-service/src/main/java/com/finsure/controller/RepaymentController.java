package com.finsure.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finsure.dto.RepaymentRequestDTO;
import com.finsure.service.RepaymentServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/repayments")
public class RepaymentController {

    @Autowired
    private RepaymentServiceImpl repaymentService;

    @PostMapping
    public ResponseEntity<?> createRepayment(
            @RequestHeader("X-User-Role") String role,
            @Valid @RequestBody RepaymentRequestDTO dto) {

        if (!role.equals("LOAN_OFFICER") && !role.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Only LOAN_OFFICER or ADMIN can process repayments");
        }
        return new ResponseEntity<>(
                repaymentService.createRepayment(dto), HttpStatus.CREATED);
    }

    @GetMapping("/internal/all")
    public ResponseEntity<?> getAllRepaymentsInternal(
            @RequestHeader(value = "X-Internal-Call",
                           required = false) String internalCall) {

        if (!"true".equals(internalCall)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Internal endpoint only");
        }
        return ResponseEntity.ok(repaymentService.getAllRepayments());
    }

    @GetMapping("/internal/by-period")
    public ResponseEntity<?> getRepaymentsByPeriodInternal(
            @RequestHeader(value = "X-Internal-Call",
                           required = false) String internalCall,
            @RequestParam String start,
            @RequestParam String end) {

        if (!"true".equals(internalCall)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Internal endpoint only");
        }
        try {
            LocalDateTime startDt = LocalDateTime.parse(start);
            LocalDateTime endDt = LocalDateTime.parse(end);
            return ResponseEntity.ok(
                    repaymentService.getRepaymentsByPeriod(startDt, endDt));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid date format. Use: 2026-05-01T00:00:00");
        }
    }

    @GetMapping("/history/{loanId}")
    public ResponseEntity<?> getRepaymentHistory(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long loanId) {

        if (!role.equals("LOAN_OFFICER") && !role.equals("BRANCH_MANAGER")
                && !role.equals("ADMIN") && !role.equals("AUDITOR")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Insufficient role");
        }
        return ResponseEntity.ok(repaymentService.getRepaymentHistory(loanId));
    }

    @GetMapping("/{repaymentId}")
    public ResponseEntity<?> getRepaymentById(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long repaymentId) {

        if (!role.equals("LOAN_OFFICER") && !role.equals("BRANCH_MANAGER")
                && !role.equals("ADMIN") && !role.equals("AUDITOR")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Insufficient role");
        }
        return ResponseEntity.ok(repaymentService.getRepaymentById(repaymentId));
    }

    @PutMapping("/{repaymentId}")
    public ResponseEntity<?> updateRepayment(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long repaymentId,
            @Valid @RequestBody RepaymentRequestDTO dto) {

        if (!role.equals("LOAN_OFFICER") && !role.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Only LOAN_OFFICER or ADMIN can update repayments");
        }
        return ResponseEntity.ok(
                repaymentService.updateRepaymentById(repaymentId, dto));
    }

    @DeleteMapping("/{repaymentId}")
    public ResponseEntity<?> deleteRepayment(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long repaymentId) {

        if (!role.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Only ADMIN can delete repayments");
        }
        return ResponseEntity.ok(
                repaymentService.deleteRepaymentById(repaymentId));
    }
}