package com.finsure.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finsure.client.ReportingClient;
import com.finsure.dto.NotificationRequestDTO;
import com.finsure.dto.TaskRequestDTO;
import com.finsure.entity.LoanApplication;
import com.finsure.entity.LoanDocument;
import com.finsure.exception.DocumentNotFoundException;
import com.finsure.exception.LoanNotFoundException;
import com.finsure.repository.LoanApplicationRepository;
import com.finsure.repository.LoanDocumentRepository;

@Service
public class LoanApprovalServiceImpl implements LoanApprovalService {

    @Autowired
    private LoanApplicationRepository loanRepo;

    @Autowired
    private LoanDocumentRepository docRepo;

    @Autowired
    private ReportingClient reportingClient;
    
    
    @Transactional
    public String approveLoan(Long loanId, String approvedBy) {

    	 LoanApplication loan = loanRepo.findById(loanId)
    	            .orElseThrow(() -> new LoanNotFoundException(
    	                    "Loan Not Found With ID: " + loanId));


    	    if (loan.getStatus().equals("APPROVED")) {
    	        throw new RuntimeException(
    	                "Loan ID: " + loanId
    	                + " is already APPROVED");
    	    }


    	    if (loan.getStatus().equals("REJECTED")) {
    	        throw new RuntimeException(
    	                "Loan ID: " + loanId
    	                + " has already been REJECTED and cannot be approved");
    	    }
        List<LoanDocument> docs = docRepo.findByLoan_LoanId(loanId);
        if (docs.isEmpty()) {
            throw new DocumentNotFoundException(
                    "No Documents Uploaded For Loan ID: " + loanId
                    + ". Please upload documents before approval.");
        }


        for (LoanDocument doc : docs) {
            doc.setStatus("VERIFIED");
            doc.setVerifiedBy(approvedBy);
        }
        docRepo.saveAll(docs);


        loan.setStatus("APPROVED");
        loan.setApprovedBy(approvedBy);
        loan.setNextDueDate(LocalDate.now().plusMonths(1));
        loanRepo.save(loan);



        try {
            NotificationRequestDTO notification = new NotificationRequestDTO();
            notification.setUserId(loan.getMemberId());
            notification.setEntityId(loan.getLoanId());
            notification.setMessage(
                    "Your loan application of Rs." + loan.getAmount()
                    + " has been APPROVED. First EMI due on: "
                    + loan.getNextDueDate());
            notification.setCategory("LOAN");
            reportingClient.createNotification(notification);
        } catch (Exception e) {

            System.out.println("Notification failed for loan "
                    + loanId + ": " + e.getMessage());
        }


        try {
            TaskRequestDTO task = new TaskRequestDTO();
            task.setAssignedToUserId(1L);
            task.setRelatedEntityId(loanId);
            task.setDescription("Loan ID: " + loanId
                    + " approved by " + approvedBy
                    + ". Monitor first EMI payment.");
            task.setDueDate(LocalDate.now().plusMonths(1));
            reportingClient.createTask(task);
        } catch (Exception e) {
            System.out.println("Task creation failed for loan "
                    + loanId + ": " + e.getMessage());
        }

        return "Loan ID: " + loanId
                + " Approved Successfully by " + approvedBy;
    }

    @Transactional
    public String rejectLoan(Long loanId, String approvedBy, String reason) {

        LoanApplication loan = loanRepo.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException(
                        "Loan Not Found With ID: " + loanId));
        
        if (loan.getStatus().equals("REJECTED")) {
            throw new RuntimeException(
                    "Loan ID: " + loanId
                    + " is already REJECTED");
        }

        if (loan.getStatus().equals("APPROVED")) {
            throw new RuntimeException(
                    "Loan ID: " + loanId
                    + " is already APPROVED and cannot be rejected");
        }


        List<LoanDocument> docs = docRepo.findByLoan_LoanId(loanId);
        if (docs.isEmpty()) {
            throw new DocumentNotFoundException(
                    "No Documents Found For Loan ID: " + loanId);
        }


        for (LoanDocument doc : docs) {
            doc.setStatus("REJECTED");
            doc.setVerifiedBy(approvedBy);
        }
        docRepo.saveAll(docs);


        loan.setStatus("REJECTED");
        loan.setApprovedBy(approvedBy);
        loanRepo.save(loan);


        try {
            NotificationRequestDTO notification = new NotificationRequestDTO();
            notification.setUserId(loan.getMemberId());
            notification.setEntityId(loan.getLoanId());
            notification.setMessage(
                    "Your loan application of Rs." + loan.getAmount()
                    + " has been REJECTED. Reason: " + reason);
            notification.setCategory("LOAN");
            reportingClient.createNotification(notification);
        } catch (Exception e) {
            System.out.println("Notification failed for loan "
                    + loanId + ": " + e.getMessage());
        }

        return "Loan ID: " + loanId
                + " Rejected. Reason: " + reason;
    }
}
