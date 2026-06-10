package com.finsure.client;

import com.finsure.dto.AccountReportDTO;
import com.finsure.dto.MemberReportDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(name = "MEMBER-SERVICE",
        configuration = FeignClientConfig.class)
public interface MemberReportClient {

    @GetMapping("/members/internal/all")
    List<MemberReportDTO> getAllMembers();

    @GetMapping("/accounts/internal/all")
    List<AccountReportDTO> getAllAccounts();


    @GetMapping("/accounts/internal/stats")
    Map<String, Long> getAccountStats();
}
