package com.finsure.repository;

import com.finsure.entity.AuditPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditPackageRepository
        extends JpaRepository<AuditPackage, Long> {
}