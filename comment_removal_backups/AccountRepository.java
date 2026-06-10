package com.finsure.repository;

import com.finsure.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface AccountRepository
        extends JpaRepository<Account, Long> {

    List<Account> findByMember_MemberId(Long memberId);


    @Query("SELECT COUNT(a) FROM Account a WHERE a.status = 'ACTIVE'")
    long countActiveAccounts();
}
