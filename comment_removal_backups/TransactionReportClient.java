package com.finsure.client;

import com.finsure.dto.TransactionReportDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@FeignClient(name = "TRANSACTION-SERVICE",
        configuration = FeignClientConfig.class)
public interface TransactionReportClient {

    @GetMapping("/api/transactions/internal/all")
    List<TransactionReportDTO> getAllTransactions();



    @GetMapping("/api/transactions/internal/stats")
    Map<String, Object> getTransactionStats();
    
    @GetMapping("/api/transactions/internal/by-period")
    List<TransactionReportDTO> getTransactionsByPeriod(
            @RequestParam String start,
            @RequestParam String end);
}
