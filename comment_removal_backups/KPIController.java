package com.finsure.controller;

import com.finsure.dto.KPIDTO;
import com.finsure.service.KPIServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/kpi")
public class KPIController {

    @Autowired
    private KPIServiceImpl kpiService;

    @PostMapping
    public ResponseEntity<?> createKPI(
            @RequestHeader("X-User-Role") String role,
            @RequestBody KPIDTO dto) {

        if (!role.equals("BRANCH_MANAGER") && !role.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Only BRANCH_MANAGER or ADMIN");
        }



        return new ResponseEntity<>(
                kpiService.calculateAndSaveKPI(
                        dto.getCategory(), "ADMIN"),
                HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<?> getAllKPIs(
            @RequestHeader("X-User-Role") String role) {

        if (!role.equals("BRANCH_MANAGER") && !role.equals("ADMIN")
                && !role.equals("AUDITOR") && !role.equals("LOAN_OFFICER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Insufficient role");
        }
        return ResponseEntity.ok(kpiService.getAllKPIs());
    }

    @GetMapping("/filter")
    public ResponseEntity<?> getKPIsByCategory(
            @RequestHeader("X-User-Role") String role,
            @RequestParam String category) {

        if (!role.equals("BRANCH_MANAGER") && !role.equals("ADMIN")
                && !role.equals("AUDITOR") && !role.equals("LOAN_OFFICER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Insufficient role");
        }
        return ResponseEntity.ok(kpiService.getKPIsByCategory(category));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getKPIById(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long id) {

        if (!role.equals("BRANCH_MANAGER") && !role.equals("ADMIN")
                && !role.equals("AUDITOR") && !role.equals("LOAN_OFFICER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Insufficient role");
        }
        return ResponseEntity.ok(kpiService.getKPIById(id));
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateKPI(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long id,
            @RequestBody KPIDTO dto) {

        if (!role.equals("BRANCH_MANAGER") && !role.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Only BRANCH_MANAGER or ADMIN can update KPIs");
        }
        return ResponseEntity.ok(kpiService.updateKPI(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteKPI(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long id) {

        if (!role.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Only ADMIN can delete KPIs");
        }
        kpiService.deleteKPI(id);
        return ResponseEntity.ok("KPI Deleted Successfully");
    }

    @PostMapping("/calculate")
    public ResponseEntity<?> calculateKPI(
            @RequestHeader("X-User-Role") String role,
            @RequestParam String category) {

        if (!role.equals("BRANCH_MANAGER") && !role.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied");
        }
        return ResponseEntity.ok(
                kpiService.calculateAndSaveKPI(category, role));
    }
}
