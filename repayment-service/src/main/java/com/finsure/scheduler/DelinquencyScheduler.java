package com.finsure.scheduler;

import com.finsure.client.LoanClient;
import com.finsure.client.ReportingClient;
import com.finsure.dto.LoanDTO;
import com.finsure.dto.NotificationRequestDTO;
import com.finsure.dto.TaskRequestDTO;
import com.finsure.entity.DelinquencyEntity;
import com.finsure.repository.DelinquencyRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DelinquencyScheduler {

    @Autowired
    private LoanClient loanClient;

    @Autowired
    private ReportingClient reportingClient;

    @Autowired
    private DelinquencyRepository delinquencyRepo;


    @Scheduled(cron = "0 06 12 * * *")
    @Transactional
    public void detectDelinquencies() {
        try {
            System.out.println("Delinquency check started: "
                    + LocalDateTime.now());

            List<LoanDTO> overdueLoans = loanClient.getOverdueLoans();

            for (LoanDTO loan : overdueLoans) {


                if (delinquencyRepo.existsByLoanId(loan.getLoanId())) {
                    continue;
                }


                DelinquencyEntity delinquency = new DelinquencyEntity();
                delinquency.setLoanId(loan.getLoanId());
                delinquency.setDetectedAt(LocalDateTime.now());
                delinquency.setStatus("DELINQUENT");
                delinquency.setNotes("Auto-detected: EMI overdue since "
                        + loan.getNextDueDate()
                        + ". Loan amount: Rs." + loan.getAmount());

                delinquencyRepo.save(delinquency);


                try {
                    NotificationRequestDTO notification =
                            new NotificationRequestDTO();
                    notification.setUserId(loan.getMemberId());
                    notification.setEntityId(loan.getLoanId());
                    notification.setMessage(
                            "ALERT: Your EMI for Loan ID "
                                    + loan.getLoanId()
                                    + " is overdue since " + loan.getNextDueDate()
                                    + ". Please make payment immediately.");
                    notification.setCategory("DELINQUENCY");
                    reportingClient.createNotification(notification);
                } catch (Exception e) {
                    System.out.println("Notification failed for loan "
                            + loan.getLoanId() + ": " + e.getMessage());
                }


                System.out.println("Delinquency processed for loan: "
                        + loan.getLoanId());
            }

            System.out.println("Delinquency check complete. "
                    + overdueLoans.size() + " loans processed.");

        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
