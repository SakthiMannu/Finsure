package com.finsure.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.finsure.entity.LoanApplication;

@Repository
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {


    List<LoanApplication> findByMemberId(Long memberId);


    List<LoanApplication> findByNextDueDateBeforeAndStatus(LocalDate date, String status);
    
    List<LoanApplication> findByStatusAndNextDueDateBefore(
            String status, LocalDate date);
    
    List<LoanApplication> findBySubmittedAtBetween(
            LocalDate start, LocalDate end);
    @Query("SELECT COUNT(l) FROM LoanApplication l " +
    	       "WHERE l.status = 'APPROVED'")
    	long countApprovedLoans();

    	@Query("SELECT COUNT(l) FROM LoanApplication l " +
    	       "WHERE l.status = 'PENDING'")
    	long countPendingLoans();

    	@Query("SELECT COUNT(l) FROM LoanApplication l")
    	long countAllLoans();
}
