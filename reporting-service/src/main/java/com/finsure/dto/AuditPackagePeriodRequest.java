package com.finsure.dto;

import lombok.Data;

@Data
public class AuditPackagePeriodRequest {
    private String periodStart;
    private String periodEnd;
}