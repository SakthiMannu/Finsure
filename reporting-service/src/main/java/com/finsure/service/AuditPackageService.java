package com.finsure.service;

import com.finsure.dto.AuditPackageDTO;
import java.time.LocalDateTime;
import java.util.List;

public interface AuditPackageService {
    AuditPackageDTO generatePackage(LocalDateTime start, LocalDateTime end);
    List<AuditPackageDTO> getAllAuditPackages();
    AuditPackageDTO getAuditPackageById(Long id);
    void deleteAuditPackage(Long id);
}