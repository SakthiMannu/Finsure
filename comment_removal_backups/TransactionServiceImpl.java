package com.finsure.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finsure.client.AccountClient;
import com.finsure.dto.AccountDTO;
import com.finsure.dto.TransactionRequestDTO;
import com.finsure.dto.TransactionResponseDTO;
import com.finsure.entity.Transaction;
import com.finsure.exception.AccountNotFoundException;
import com.finsure.exception.TransactionNotFoundException;
import com.finsure.repository.TransactionRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepo;

    @Autowired
    private AccountClient accountClient;

    @Transactional
    @CircuitBreaker(name = "default", fallbackMethod = "depositFallback")
    @Retry(name = "default")
    public TransactionResponseDTO deposit(TransactionRequestDTO dto) {

        AccountDTO account;
        try {
            account = accountClient.getAccountById(dto.getAccountId());
        } catch (feign.FeignException.NotFound e) {
            throw new AccountNotFoundException(
                    "No Account with ID: " + dto.getAccountId());
        } catch (feign.FeignException e) {
            throw new RuntimeException(
                    "Account service error. Please try again.");
        }

        if (account == null) {
            throw new AccountNotFoundException(
                    "No Account with ID: " + dto.getAccountId());
        }

        if (!account.getStatus().equalsIgnoreCase("ACTIVE")) {
            throw new RuntimeException(
                    "Account ID: " + dto.getAccountId()
                            + " is not active");
        }
        if (dto.getAmount() == null || dto.getAmount() <= 0) {
            throw new RuntimeException(
                    "Amount must be greater than zero");
        }
        if (dto.getAmount() > 1000000) {
            throw new RuntimeException(
                    "Maximum deposit limit is Rs.10,00,000 per transaction");
        }

        double newBalance = account.getBalance() + dto.getAmount();
        accountClient.updateAccountBalance(dto.getAccountId(), newBalance);

        Transaction txn = new Transaction();
        txn.setAccountId(dto.getAccountId());
        txn.setType(Transaction.TxnType.DEPOSIT);
        txn.setAmount(dto.getAmount());
        txn.setPerformedBy(dto.getPerformedByEmail());
        txn.setPerformedAt(LocalDateTime.now());
        txn.setStatus("SUCCESS");

        return mapToResponseDTO(transactionRepo.save(txn));
    }

    public TransactionResponseDTO depositFallback(
            TransactionRequestDTO dto, Throwable ex) {


        if (ex instanceof AccountNotFoundException) {
            throw (AccountNotFoundException) ex;
        }
        if (ex instanceof RuntimeException
                && ex.getMessage() != null
                && (ex.getMessage().contains("No Account")
                    || ex.getMessage().contains("not active")
                    || ex.getMessage().contains("Insufficient"))) {
            throw (RuntimeException) ex;
        }
        throw new RuntimeException(
                "Transaction service is currently unavailable. "
                        + "Cannot process deposit. Please try again shortly.");
    }

    @Transactional
    @CircuitBreaker(name = "default", fallbackMethod = "withdrawFallback")
    @Retry(name = "default")
    public TransactionResponseDTO withdraw(TransactionRequestDTO dto) {

        AccountDTO account;
        try {
            account = accountClient.getAccountById(dto.getAccountId());
        } catch (feign.FeignException.NotFound e) {
            throw new AccountNotFoundException(
                    "No Account with ID: " + dto.getAccountId());
        } catch (feign.FeignException e) {
            throw new RuntimeException(
                    "Account service error. Please try again.");
        }

        if (account == null) {
            throw new AccountNotFoundException(
                    "No Account with ID: " + dto.getAccountId());
        }

        if (!account.getStatus().equalsIgnoreCase("ACTIVE")) {
            throw new RuntimeException(
                    "Account ID: " + dto.getAccountId()
                            + " is not active");
        }

        if (account.getBalance() < dto.getAmount()) {
            throw new RuntimeException(
                    "Insufficient Balance. Available: Rs."
                            + account.getBalance()
                            + " Requested: Rs." + dto.getAmount());
        }
        if (dto.getAmount() == null || dto.getAmount() <= 0) {
            throw new RuntimeException(
                    "Amount must be greater than zero");
        }
        if (dto.getAmount() > 1000000) {
            throw new RuntimeException(
                    "Maximum withdrawal limit is Rs.10,00,000 per transaction");
        }

        double newBalance = account.getBalance() - dto.getAmount();
        accountClient.updateAccountBalance(dto.getAccountId(), newBalance);

        Transaction txn = new Transaction();
        txn.setAccountId(dto.getAccountId());
        txn.setType(Transaction.TxnType.WITHDRAWAL);
        txn.setAmount(dto.getAmount());
        txn.setPerformedBy(dto.getPerformedByEmail());
        txn.setPerformedAt(LocalDateTime.now());
        txn.setStatus("SUCCESS");

        return mapToResponseDTO(transactionRepo.save(txn));
    }

    public TransactionResponseDTO withdrawFallback(
            TransactionRequestDTO dto, Throwable ex) {
        if (ex instanceof AccountNotFoundException) {
            throw (AccountNotFoundException) ex;
        }
        if (ex instanceof RuntimeException
                && ex.getMessage() != null
                && (ex.getMessage().contains("No Account")
                    || ex.getMessage().contains("not active")
                    || ex.getMessage().contains("Insufficient"))) {
            throw (RuntimeException) ex;
        }
        throw new RuntimeException(
                "Transaction service is currently unavailable. "
                        + "Cannot process withdrawal. Please try again shortly.");
    }

    public Double getBalance(Long accountId) {
        try {
            AccountDTO account = accountClient.getAccountById(accountId);
            if (account == null) {
                throw new AccountNotFoundException(
                        "No Account with ID: " + accountId);
            }
            return account.getBalance();
        } catch (feign.FeignException.NotFound e) {
            throw new AccountNotFoundException(
                    "No Account with ID: " + accountId);
        } catch (AccountNotFoundException e) {
            throw e;
        } catch (feign.FeignException e) {
            throw new RuntimeException(
                    "Account service error. Please try again.");
        } catch (Exception e) {
            String msg = e.getMessage();
            if (msg != null && (msg.contains("NOT_FOUND")
                    || msg.contains("404")
                    || msg.contains("No Account"))) {
                throw new AccountNotFoundException(
                        "No Account with ID: " + accountId);
            }
            throw new RuntimeException(
                    "Could not fetch balance. Please try again.");
        }
    }

    public List<TransactionResponseDTO> getTransactionHistory(
            Long accountId) {
        List<Transaction> transactions =
                transactionRepo.findByAccountIdOrderByPerformedAtDesc(
                        accountId);
        if (transactions.isEmpty()) {
            throw new TransactionNotFoundException(
                    "No Transactions Found For Account ID: " + accountId);
        }
        return transactions.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<TransactionResponseDTO> getAllTransactions() {
        return transactionRepo.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<TransactionResponseDTO> getTransactionsByPeriod(
            LocalDateTime start, LocalDateTime end) {
        return transactionRepo.findByPerformedAtBetween(start, end)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public Map<String, Object> getTransactionStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalTransactions",
                transactionRepo.countAllTransactions());
        stats.put("totalDeposits",
                transactionRepo.sumAllDeposits());
        return stats;
    }

    private TransactionResponseDTO mapToResponseDTO(Transaction txn) {
        TransactionResponseDTO dto = new TransactionResponseDTO();
        dto.setTxnId(txn.getTxnId());
        dto.setAccountId(txn.getAccountId());
        dto.setType(txn.getType().name());
        dto.setAmount(txn.getAmount());
        dto.setPerformedBy(txn.getPerformedBy());
        dto.setPerformedAt(txn.getPerformedAt());
        dto.setStatus(txn.getStatus());
        return dto;
    }
}
