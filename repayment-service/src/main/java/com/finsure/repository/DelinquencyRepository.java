package com.finsure.repository;

import com.finsure.entity.DelinquencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DelinquencyRepository extends JpaRepository<DelinquencyEntity, Long> {

    List<DelinquencyEntity> findByLoanId(Long loanId);



    boolean existsByLoanId(Long loanId);
}
