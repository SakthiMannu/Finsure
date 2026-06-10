package com.finsure.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finsure.dto.AccountRequestDTO;
import com.finsure.dto.AccountResponseDTO;
import com.finsure.entity.Account;
import com.finsure.entity.Member;
import com.finsure.exception.AccountNotFoundException;
import com.finsure.exception.MemberNotFoundException;
import com.finsure.mapper.AccountMapper;
import com.finsure.repository.AccountRepository;
import com.finsure.repository.MemberRepository;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private MemberRepository memberRepo;

    @Autowired
    private AccountMapper acc_map;

    @Transactional
    public AccountResponseDTO createAccount(AccountRequestDTO dto) {


        Member member = memberRepo.findById(dto.getMemberId())
                .orElseThrow(() -> new MemberNotFoundException(
                        "Member Not Found With ID: " + dto.getMemberId()));

        Account acc = acc_map.toAccountEntity(dto);
        acc.setMember(member);
        acc.setStatus("ACTIVE");
        acc.setCreatedAt(LocalDate.now());

        Account saved = accountRepo.save(acc);

        AccountResponseDTO response = acc_map.toResponseDTO(saved);
        response.setMemberId(saved.getMember().getMemberId());
        return response;
    }

    public List<AccountResponseDTO> getAllAccounts() {
    	
        return accountRepo.findAll().stream().map(acc -> {
            AccountResponseDTO res = acc_map.toResponseDTO(acc);
            res.setMemberId(acc.getMember().getMemberId());
            return res;
        }).collect(Collectors.toList());
    }

    public AccountResponseDTO getAccountById(Long accId) {
        Account acc = accountRepo.findById(accId)
                .orElseThrow(() -> new AccountNotFoundException(
                        "No Account with ID: " + accId));
        AccountResponseDTO response = acc_map.toResponseDTO(acc);
        response.setMemberId(acc.getMember().getMemberId());
        return response;
    }

    public List<AccountResponseDTO> getAccountsByMemberId(Long memberId) {

        memberRepo.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(
                        "Member Not Found With ID: " + memberId));

        return accountRepo.findByMember_MemberId(memberId).stream().map(acc -> {
            AccountResponseDTO dto = acc_map.toResponseDTO(acc);
            dto.setMemberId(acc.getMember().getMemberId());
            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public AccountResponseDTO updateAccountById(Long accId, AccountRequestDTO dto) {
        Account acc = accountRepo.findById(accId)
                .orElseThrow(() -> new AccountNotFoundException(
                        "No Account with ID: " + accId));

        Member member = memberRepo.findById(dto.getMemberId())
                .orElseThrow(() -> new MemberNotFoundException(
                        "Member Not Found With ID: " + dto.getMemberId()));

        acc.setType(dto.getType());
        acc.setBalance(dto.getBalance());
        acc.setCreatedAt(LocalDate.now());
        acc.setMember(member);

        Account updated = accountRepo.save(acc);

        AccountResponseDTO response = acc_map.toResponseDTO(updated);
        response.setMemberId(updated.getMember().getMemberId());
        return response;
    }

    @Transactional
    public String deleteAccountById(Long accId) {
        accountRepo.findById(accId)
                .orElseThrow(() -> new AccountNotFoundException(
                        "No Account with ID: " + accId));
        accountRepo.deleteById(accId);
        return "Account with ID " + accId + " Successfully Deleted.";
    }
    
    @Transactional
    public AccountResponseDTO updateAccountBalance(
            Long accountId, double newBalance) {

        Account acc = accountRepo.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(
                        "Account Not Found With ID: " + accountId));

        if (newBalance < 0) {
            throw new RuntimeException(
                    "Balance cannot be negative");
        }

        acc.setBalance(newBalance);
        Account updated = accountRepo.save(acc);


        AccountResponseDTO response = acc_map.toResponseDTO(updated);
        response.setMemberId(updated.getMember().getMemberId());
        return response;
    }
    
}

