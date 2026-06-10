package com.finsure.client;

import com.finsure.dto.AccountDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;




@FeignClient(name = "MEMBER-SERVICE", configuration = FeignClientConfig.class)
public interface AccountClient {


    @GetMapping("/accounts/internal/{accountId}")
    AccountDTO getAccountById(@PathVariable Long accountId);


    @PutMapping("/accounts/internal/balance/{accountId}")
    AccountDTO updateAccountBalance(@PathVariable Long accountId,
                                    @RequestParam double newBalance);
}
