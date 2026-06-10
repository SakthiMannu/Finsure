package com.finsure.service;

import java.util.List;

import com.finsure.dto.AccountRequestDTO;
import com.finsure.dto.AccountResponseDTO;

public interface AccountService {
	
    AccountResponseDTO createAccount(AccountRequestDTO dto);
    
    List<AccountResponseDTO> getAllAccounts();
    
    AccountResponseDTO getAccountById(Long accId);
    
    List<AccountResponseDTO> getAccountsByMemberId(Long memberId);
    
    AccountResponseDTO updateAccountById(Long accId, AccountRequestDTO dto);
    
    String deleteAccountById(Long accId);
    
    AccountResponseDTO updateAccountBalance(Long accountId, double newBalance);

}
