package com.finsure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.finsure.dto.MemberDTO;

@FeignClient(name = "MEMBER-SERVICE",
        configuration = FeignClientConfig.class)
public interface MemberClient {

    @GetMapping("/members/internal/{id}")
    MemberDTO getMemberById(@PathVariable Long id);

}
