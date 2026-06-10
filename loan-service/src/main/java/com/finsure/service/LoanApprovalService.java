package com.finsure.service;

public interface LoanApprovalService {
	
    String approveLoan(Long loanId, String approvedBy);
    
    String rejectLoan(Long loanId, String approvedBy, String reason);
}