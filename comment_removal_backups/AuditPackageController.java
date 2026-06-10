package com.finsure.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finsure.dto.AuditPackagePeriodRequest;
import com.finsure.service.AuditPackageServiceImpl;

@RestController
@RequestMapping("/audit-packages")
public class AuditPackageController {

    @Autowired
    private AuditPackageServiceImpl auditPackageService;



    @PostMapping("/generate")
    public ResponseEntity<?> generateAuditPackage(
            @RequestHeader("X-User-Role") String role,
            @RequestBody AuditPackagePeriodRequest request) {

        if (!role.equals("AUDITOR") && !role.equals("ADMIN")
                && !role.equals("BRANCH_MANAGER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied");
        }

        try {
            LocalDateTime start = LocalDateTime.parse(
                    request.getPeriodStart());
            LocalDateTime end = LocalDateTime.parse(
                    request.getPeriodEnd());
            return ResponseEntity.ok(
                    auditPackageService.generatePackage(start, end));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid date format. Use: 2026-05-01T00:00:00");
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllAuditPackages(
            @RequestHeader("X-User-Role") String role) {

        if (!role.equals("AUDITOR") && !role.equals("BRANCH_MANAGER")
                && !role.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Insufficient role");
        }
        return ResponseEntity.ok(
                auditPackageService.getAllAuditPackages());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAuditPackageById(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long id) {

        if (!role.equals("AUDITOR") && !role.equals("BRANCH_MANAGER")
                && !role.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Insufficient role");
        }
        return ResponseEntity.ok(
                auditPackageService.getAuditPackageById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAuditPackage(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long id) {

        if (!role.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Only ADMIN");
        }
        auditPackageService.deleteAuditPackage(id);
        return ResponseEntity.ok(
                "Audit Package Deleted Successfully");
    }
}
