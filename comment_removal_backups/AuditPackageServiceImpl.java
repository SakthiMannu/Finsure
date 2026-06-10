package com.finsure.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finsure.client.LoanReportClient;
import com.finsure.client.MemberReportClient;
import com.finsure.client.RepaymentReportClient;
import com.finsure.client.TransactionReportClient;
import com.finsure.dto.AccountReportDTO;
import com.finsure.dto.AuditPackageDTO;
import com.finsure.dto.LoanReportDTO;
import com.finsure.dto.MemberReportDTO;
import com.finsure.dto.RepaymentReportDTO;
import com.finsure.dto.TransactionReportDTO;
import com.finsure.entity.AuditPackage;
import com.finsure.exception.ResourceNotFoundException;
import com.finsure.repository.AuditPackageRepository;

@Service
public class AuditPackageServiceImpl implements AuditPackageService {

    @Autowired
    private AuditPackageRepository auditPackageRepo;

    @Autowired
    private LoanReportClient loanClient;

    @Autowired
    private MemberReportClient memberClient;

    @Autowired
    private TransactionReportClient transactionClient;

    @Autowired
    private RepaymentReportClient repaymentClient;

    public AuditPackageDTO generatePackage(
            LocalDateTime start, LocalDateTime end) {




        String startStr = start.toString();
        String endStr = end.toString();


        List<LoanReportDTO> loans = Collections.emptyList();
        try {
            loans = loanClient.getLoansByPeriod(startStr, endStr);
        } catch (Exception e) {
            System.out.println("Loan fetch failed: " + e.getMessage());
        }


        List<MemberReportDTO> members = Collections.emptyList();
        try {
            members = memberClient.getAllMembers();
        } catch (Exception e) {
            System.out.println("Member fetch failed: " + e.getMessage());
        }


        List<AccountReportDTO> accounts = Collections.emptyList();
        try {
            accounts = memberClient.getAllAccounts();
        } catch (Exception e) {
            System.out.println("Account fetch failed: " + e.getMessage());
        }


        List<TransactionReportDTO> transactions = Collections.emptyList();
        try {
            transactions = transactionClient
                    .getTransactionsByPeriod(startStr, endStr);
        } catch (Exception e) {
            System.out.println("Transaction fetch failed: " + e.getMessage());
        }


        List<RepaymentReportDTO> repayments = Collections.emptyList();
        try {
            repayments = repaymentClient
                    .getRepaymentsByPeriod(startStr, endStr);
        } catch (Exception e) {
            System.out.println("Repayment fetch failed: " + e.getMessage());
        }


        long totalLoans = loans.size();
        long approvedLoans = loans.stream()
                .filter(l -> "APPROVED".equals(l.getStatus()))
                .count();
        long rejectedLoans = loans.stream()
                .filter(l -> "REJECTED".equals(l.getStatus()))
                .count();

        long totalMembers = members.size();
        long activeMembers = members.stream()
                .filter(m -> "ACTIVE".equals(m.getStatus()))
                .count();

        long totalAccounts = accounts.size();

        long totalTransactions = transactions.size();
        double totalDeposits = transactions.stream()
                .filter(t -> "DEPOSIT".equals(t.getType()))
                .mapToDouble(TransactionReportDTO::getAmount)
                .sum();
        double totalWithdrawals = transactions.stream()
                .filter(t -> "WITHDRAWAL".equals(t.getType()))
                .mapToDouble(TransactionReportDTO::getAmount)
                .sum();

        long totalRepayments = repayments.size();
        double totalRepaymentAmount = repayments.stream()
                .mapToDouble(RepaymentReportDTO::getAmount)
                .sum();


        String contentsJSON = String.format(
            "{" +
            "\"period\":\"%s to %s\"," +
            "\"loans\":{" +
                "\"total\":%d," +
                "\"approved\":%d," +
                "\"rejected\":%d" +
            "}," +
            "\"members\":{" +
                "\"total\":%d," +
                "\"active\":%d" +
            "}," +
            "\"accounts\":{" +
                "\"total\":%d" +
            "}," +
            "\"transactions\":{" +
                "\"total\":%d," +
                "\"totalDeposits\":%.2f," +
                "\"totalWithdrawals\":%.2f," +
                "\"netFlow\":%.2f" +
            "}," +
            "\"repayments\":{" +
                "\"total\":%d," +
                "\"totalAmount\":%.2f" +
            "}" +
            "}",
            start.toLocalDate(),
            end.toLocalDate(),
            totalLoans, approvedLoans, rejectedLoans,
            totalMembers, activeMembers,
            totalAccounts,
            totalTransactions,
            Math.round(totalDeposits * 100.0) / 100.0,
            Math.round(totalWithdrawals * 100.0) / 100.0,
            Math.round((totalDeposits - totalWithdrawals) * 100.0) / 100.0,
            totalRepayments,
            Math.round(totalRepaymentAmount * 100.0) / 100.0
        );

        AuditPackage pkg = new AuditPackage();
        pkg.setPeriodStart(start);
        pkg.setPeriodEnd(end);
        pkg.setContentsJSON(contentsJSON);
        pkg.setGeneratedAt(LocalDateTime.now());
        pkg.setPackageURI("audit/packages/"
                + start.toLocalDate()
                + "_to_"
                + end.toLocalDate()
                + ".json");

        return mapToDTO(auditPackageRepo.save(pkg));
    }

    public List<AuditPackageDTO> getAllAuditPackages() {
        return auditPackageRepo.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public AuditPackageDTO getAuditPackageById(Long id) {
        AuditPackage pkg = auditPackageRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Audit Package Not Found With ID: " + id));
        return mapToDTO(pkg);
    }

    public void deleteAuditPackage(Long id) {
        auditPackageRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Audit Package Not Found With ID: " + id));
        auditPackageRepo.deleteById(id);
    }

    private AuditPackageDTO mapToDTO(AuditPackage p) {
        AuditPackageDTO dto = new AuditPackageDTO();
        dto.setPackageId(p.getPackageId());
        dto.setPeriodStart(p.getPeriodStart());
        dto.setPeriodEnd(p.getPeriodEnd());
        dto.setContentsJSON(p.getContentsJSON());
        dto.setGeneratedAt(p.getGeneratedAt());
        dto.setPackageURI(p.getPackageURI());
        return dto;
    }
}
