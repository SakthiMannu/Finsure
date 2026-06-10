package com.finsure.client;

import com.finsure.dto.LoanReportDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;



@FeignClient(name = "LOAN-SERVICE", configuration = FeignClientConfig.class)
public interface LoanReportClient {

	@GetMapping("/loans/internal/all")
	List<LoanReportDTO> getAllLoans();


	@GetMapping("/loans/internal/period-filter")
	List<LoanReportDTO> getLoansByPeriod(@RequestParam String start, @RequestParam String end);

	@GetMapping("/loans/internal/stats")
	Map<String, Long> getLoanStats();
}
