package com.finsure.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.finsure.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {


    List<Transaction> findByAccountIdOrderByPerformedAtDesc(Long accountId);
    
    List<Transaction> findByPerformedAtBetween(
            LocalDateTime start, LocalDateTime end);
    @Query("SELECT COUNT(t) FROM Transaction t")
    long countAllTransactions();

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
           "WHERE t.type = 'DEPOSIT'")
    Double sumAllDeposits();
}
