package com.finsure.client;

import com.finsure.dto.RepaymentReportDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@FeignClient(name = "REPAYMENT-SERVICE",
        configuration = FeignClientConfig.class)
public interface RepaymentReportClient {

    
    @GetMapping("/api/repayments/internal/by-period")
    List<RepaymentReportDTO> getRepaymentsByPeriod(
            @RequestParam String start,
            @RequestParam String end);
}