package com.finsure.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.finsure.entity.RepaymentEntity;

@Repository
public interface RepaymentRepository extends JpaRepository<RepaymentEntity, Long> {

    List<RepaymentEntity> findByLoanId(Long loanId);

    @Query("SELECT COALESCE(SUM(r.amount), 0) FROM RepaymentEntity r WHERE r.loanId = :loanId AND r.status = 'PAID'")
    Double sumPaidAmountByLoanId(@Param("loanId") Long loanId);
    List<RepaymentEntity> findByPaidAtBetween(
            LocalDateTime start, LocalDateTime end);
}