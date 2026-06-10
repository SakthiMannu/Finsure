package com.finsure.mapper;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.finsure.dto.AccountRequestDTO;
import com.finsure.dto.AccountResponseDTO;
import com.finsure.entity.Account;

@Component
public class AccountMapper {


    public Account toAccountEntity(AccountRequestDTO dto) {
        Account acc = new Account();
        acc.setType(dto.getType());
        acc.setBalance(dto.getBalance());
        acc.setCreatedAt(LocalDate.now());
        return acc;
    }


    public AccountResponseDTO toResponseDTO(Account acc) {
        AccountResponseDTO response = new AccountResponseDTO();
        response.setAccountId(acc.getAccountId());
        response.setType(acc.getType());
        response.setBalance(acc.getBalance());
        response.setCreatedAt(acc.getCreatedAt());
        response.setStatus(acc.getStatus());

        return response;
    }
}

