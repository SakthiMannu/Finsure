package com.finsure.client;

import com.finsure.dto.LoanDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import java.util.List;

@FeignClient(name = "LOAN-SERVICE", configuration = FeignClientConfig.class)
public interface LoanClient {


    @GetMapping("/loans/internal/{loanId}")
    LoanDTO getLoanById(@PathVariable Long loanId);


    @GetMapping("/loans/internal/overdue")
    List<LoanDTO> getOverdueLoans();


    @PutMapping("/loans/internal/update-due-date/{loanId}")
    void updateNextDueDate(@PathVariable Long loanId);

    @PutMapping("/loans/internal/close/{loanId}")
    void closeLoan(@PathVariable Long loanId);
}
