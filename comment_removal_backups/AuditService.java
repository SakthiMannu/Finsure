package com.finsure.service;

import java.time.LocalDateTime;
import java.util.List;

import com.finsure.entity.AuditLog;

public interface AuditService {


    void log(Long userId, String action, String resourceType, String resourceId, String details);

    List<AuditLog> getAllLogs();

    List<AuditLog> getLogsByUser(Long userId);

    List<AuditLog> getLogsByAction(String action);

    List<AuditLog> getLogsByDateRange(LocalDateTime start, LocalDateTime end);
}


