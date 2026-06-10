package com.finsure.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finsure.entity.AuditLog;
import com.finsure.repository.AuditLogRepository;

@Service
public class AuditServiceImpl implements AuditService {

    @Autowired
    private AuditLogRepository auditRepo;

    @Override
    public void log(Long userId, String action, String resourceType, String resourceId, String details) {
        AuditLog log = new AuditLog();
        log.setUserId(userId);
        log.setAction(action);
        log.setResourceType(resourceType);
        log.setResourceId(resourceId);
        log.setDetails(details);
        log.setTimestamp(LocalDateTime.now());


        auditRepo.save(log);
    }

    @Override
    public List<AuditLog> getAllLogs() {
        return auditRepo.findAll();
    }

    @Override
    public List<AuditLog> getLogsByUser(Long userId) {
        return auditRepo.findByUserId(userId);
    }

    @Override
    public List<AuditLog> getLogsByAction(String action) {
        return auditRepo.findByAction(action);
    }

    @Override
    public List<AuditLog> getLogsByDateRange(LocalDateTime start, LocalDateTime end) {
        return auditRepo.findByTimestampBetween(start, end);
    }
}

