package com.finsure.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finsure.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByNameAndDob(String name, LocalDate dob);

    Optional<Member> findByNameIgnoreCase(String name);
}
