package com.finsure.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finsure.dto.TransactionRequestDTO;
import com.finsure.service.TransactionServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private TransactionServiceImpl transactionService;

    @GetMapping("/transactions/internal/all")
    public ResponseEntity<?> getAllTransactionsInternal(
            @RequestHeader(value = "X-Internal-Call",
                           required = false) String internalCall) {

        if (!"true".equals(internalCall)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Internal endpoint only");
        }
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @GetMapping("/transactions/internal/by-period")
    public ResponseEntity<?> getTransactionsByPeriodInternal(
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
                    transactionService.getTransactionsByPeriod(startDt, endDt));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid date format. Use: 2026-05-01T00:00:00");
        }
    }

    @GetMapping("/transactions/internal/stats")
    public ResponseEntity<?> getTransactionStatsInternal(
            @RequestHeader(value = "X-Internal-Call",
                           required = false) String internalCall) {

        if (!"true".equals(internalCall)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Internal endpoint only");
        }
        return ResponseEntity.ok(transactionService.getTransactionStats());
    }

    @PostMapping("/transactions/deposit")
    public ResponseEntity<?> deposit(
            @RequestHeader("X-User-Role") String role,
            @RequestHeader("X-User-Email") String email,
            @Valid @RequestBody TransactionRequestDTO dto) {

        if (!role.equals("TELLER") && !role.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Only TELLER or ADMIN can deposit");
        }
        dto.setPerformedByEmail(email);
        dto.setPerformedByRole(role);
        return new ResponseEntity<>(
                transactionService.deposit(dto), HttpStatus.CREATED);
    }

    @PostMapping("/transactions/withdraw")
    public ResponseEntity<?> withdraw(
            @RequestHeader("X-User-Role") String role,
            @RequestHeader("X-User-Email") String email,
            @Valid @RequestBody TransactionRequestDTO dto) {

        if (!role.equals("TELLER") && !role.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Only TELLER or ADMIN can withdraw");
        }
        dto.setPerformedByEmail(email);
        dto.setPerformedByRole(role);
        return new ResponseEntity<>(
                transactionService.withdraw(dto), HttpStatus.CREATED);
    }

    @GetMapping("/account/balance/{accountId}")
    public ResponseEntity<?> getBalance(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long accountId) {

        if (!role.equals("TELLER") && !role.equals("MEMBER")
                && !role.equals("BRANCH_MANAGER") && !role.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Insufficient role");
        }
        return ResponseEntity.ok(transactionService.getBalance(accountId));
    }

    @GetMapping("/transactions/history/{accountId}")
    public ResponseEntity<?> getTransactionHistory(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long accountId) {

        if (!role.equals("TELLER") && !role.equals("MEMBER")
                && !role.equals("BRANCH_MANAGER") && !role.equals("ADMIN")
                && !role.equals("AUDITOR")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Insufficient role");
        }
        return ResponseEntity.ok(
                transactionService.getTransactionHistory(accountId));
    }
}