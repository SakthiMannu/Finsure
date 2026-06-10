package com.finsure.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReportDTO {
    private Long reportId;
    private String scope;
    private String parametersJSON;
    private String metricsJSON;
    private String generatedBy;
    private LocalDateTime generatedAt;
    private String reportURI;
}