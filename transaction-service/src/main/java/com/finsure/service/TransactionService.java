package com.finsure.service;

import java.time.LocalDateTime;
import java.util.List;

import com.finsure.dto.TransactionRequestDTO;
import com.finsure.dto.TransactionResponseDTO;

public interface TransactionService {
    TransactionResponseDTO deposit(TransactionRequestDTO dto);
    TransactionResponseDTO withdraw(TransactionRequestDTO dto);
    Double getBalance(Long accountId);
    List<TransactionResponseDTO> getTransactionHistory(Long accountId);
    List<TransactionResponseDTO> getAllTransactions();
    List<TransactionResponseDTO> getTransactionsByPeriod(
            LocalDateTime start, LocalDateTime end);
}