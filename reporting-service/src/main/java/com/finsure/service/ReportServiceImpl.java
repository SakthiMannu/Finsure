package com.finsure.service;

import com.finsure.client.LoanReportClient;
import com.finsure.client.MemberReportClient;
import com.finsure.client.TransactionReportClient;
import com.finsure.dto.*;
import com.finsure.entity.Report;
import com.finsure.exception.ResourceNotFoundException;
import com.finsure.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportRepository reportRepo;


    @Autowired
    private LoanReportClient loanClient;

    @Autowired
    private MemberReportClient memberClient;

    @Autowired
    private TransactionReportClient transactionClient;





    public ReportDTO generateReport(String scope, String generatedBy) {

        String metricsJSON;

        switch (scope.toUpperCase()) {

            case "LOANS" -> {

                List<LoanReportDTO> loans = loanClient.getAllLoans();

                long total = loans.size();
                long approved = loans.stream()
                        .filter(l -> l.getStatus().equals("APPROVED"))
                        .count();
                long rejected = loans.stream()
                        .filter(l -> l.getStatus().equals("REJECTED"))
                        .count();
                long pending = loans.stream()
                        .filter(l -> l.getStatus().equals("PENDING"))
                        .count();
                double totalAmount = loans.stream()
                        .mapToDouble(LoanReportDTO::getAmount)
                        .sum();


                double approvalRate = total > 0
                        ? Math.round((approved * 100.0 / total) * 10.0) / 10.0
                        : 0.0;

                metricsJSON = String.format(
                    "{\"totalLoans\":%d,\"approved\":%d,\"rejected\":%d," +
                    "\"pending\":%d,\"totalLoanAmount\":%.2f,\"approvalRate\":%.1f}",
                    total, approved, rejected, pending, totalAmount, approvalRate
                );
            }

            case "ACCOUNTS" -> {

                List<MemberReportDTO> members = memberClient.getAllMembers();
                List<AccountReportDTO> accounts = memberClient.getAllAccounts();

                long totalMembers = members.size();
                long activeMembers = members.stream()
                        .filter(m -> m.getStatus().equals("ACTIVE"))
                        .count();
                long totalAccounts = accounts.size();
                long savingsAccounts = accounts.stream()
                        .filter(a -> a.getType().equals("SAVINGS"))
                        .count();
                long checkingAccounts = accounts.stream()
                        .filter(a -> a.getType().equals("CHECKING"))
                        .count();
                double totalBalance = accounts.stream()
                        .mapToDouble(AccountReportDTO::getBalance)
                        .sum();

                metricsJSON = String.format(
                    "{\"totalMembers\":%d,\"activeMembers\":%d," +
                    "\"totalAccounts\":%d,\"savingsAccounts\":%d," +
                    "\"checkingAccounts\":%d,\"totalBalance\":%.2f}",
                    totalMembers, activeMembers, totalAccounts,
                    savingsAccounts, checkingAccounts, totalBalance
                );
            }

            case "TRANSACTIONS" -> {

                List<TransactionReportDTO> transactions =
                        transactionClient.getAllTransactions();

                long totalTransactions = transactions.size();
                double totalDeposits = transactions.stream()
                        .filter(t -> t.getType().equals("DEPOSIT"))
                        .mapToDouble(TransactionReportDTO::getAmount)
                        .sum();
                double totalWithdrawals = transactions.stream()
                        .filter(t -> t.getType().equals("WITHDRAWAL"))
                        .mapToDouble(TransactionReportDTO::getAmount)
                        .sum();
                double netFlow = totalDeposits - totalWithdrawals;

                metricsJSON = String.format(
                    "{\"totalTransactions\":%d,\"totalDeposits\":%.2f," +
                    "\"totalWithdrawals\":%.2f,\"netFlow\":%.2f}",
                    totalTransactions, totalDeposits, totalWithdrawals, netFlow
                );
            }

            default -> throw new RuntimeException(
                "Invalid scope. Valid values: LOANS, ACCOUNTS, TRANSACTIONS"
            );
        }


        Report report = new Report();
        report.setScope(scope.toUpperCase());
        report.setParametersJSON("{\"generatedAt\":\"" +
                LocalDateTime.now() + "\"}");
        report.setMetricsJSON(metricsJSON);
        report.setGeneratedBy(generatedBy);
        report.setGeneratedAt(LocalDateTime.now());
        report.setReportURI("reports/" + scope.toLowerCase() + "/" +
                LocalDateTime.now().toLocalDate() + ".json");

        return mapToDTO(reportRepo.save(report));
    }

    public List<ReportDTO> getAllReports() {
        return reportRepo.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<ReportDTO> getReportsByScope(String scope) {
        List<Report> reports = reportRepo.findByScope(scope.toUpperCase());
        if (reports.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No Reports Found With Scope: " + scope);
        }
        return reports.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public ReportDTO getReportById(Long id) {
        Report report = reportRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Report Not Found With ID: " + id));
        return mapToDTO(report);
    }

    public void deleteReport(Long id) {
        reportRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Report Not Found With ID: " + id));
        reportRepo.deleteById(id);
    }

    private ReportDTO mapToDTO(Report r) {
        ReportDTO dto = new ReportDTO();
        dto.setReportId(r.getReportId());
        dto.setScope(r.getScope());
        dto.setParametersJSON(r.getParametersJSON());
        dto.setMetricsJSON(r.getMetricsJSON());
        dto.setGeneratedBy(r.getGeneratedBy());
        dto.setGeneratedAt(r.getGeneratedAt());
        dto.setReportURI(r.getReportURI());
        return dto;
    }
}
