package com.finsure.controller;

import com.finsure.dto.ReportDTO;
import com.finsure.service.ReportServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportServiceImpl reportService;



    @PostMapping("/generate")
    public ResponseEntity<?> generateReport(
            @RequestHeader("X-User-Role") String role,
            @RequestParam String scope,
            @RequestParam String generatedBy) {

        if (!role.equals("BRANCH_MANAGER") && !role.equals("ADMIN")
                && !role.equals("AUDITOR")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Only BRANCH_MANAGER, ADMIN or AUDITOR can generate reports");
        }
        return new ResponseEntity<>(
                reportService.generateReport(scope, generatedBy),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getAllReports(
            @RequestHeader("X-User-Role") String role) {

        if (!role.equals("BRANCH_MANAGER") && !role.equals("ADMIN")
                && !role.equals("AUDITOR")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Insufficient role");
        }
        return ResponseEntity.ok(reportService.getAllReports());
    }

    @GetMapping("/filter")
    public ResponseEntity<?> getReportsByScope(
            @RequestHeader("X-User-Role") String role,
            @RequestParam String scope) {

        if (!role.equals("BRANCH_MANAGER") && !role.equals("ADMIN")
                && !role.equals("AUDITOR")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Insufficient role");
        }
        return ResponseEntity.ok(reportService.getReportsByScope(scope));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReportById(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long id) {

        if (!role.equals("BRANCH_MANAGER") && !role.equals("ADMIN")
                && !role.equals("AUDITOR")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Insufficient role");
        }
        return ResponseEntity.ok(reportService.getReportById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReport(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long id) {

        if (!role.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Only ADMIN can delete reports");
        }
        reportService.deleteReport(id);
        return ResponseEntity.ok("Report Deleted Successfully");
    }
}
