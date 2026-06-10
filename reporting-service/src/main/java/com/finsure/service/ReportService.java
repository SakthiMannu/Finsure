package com.finsure.service;

import com.finsure.dto.ReportDTO;
import java.util.List;

public interface ReportService {



    ReportDTO generateReport(String scope, String generatedBy);

    List<ReportDTO> getAllReports();
    List<ReportDTO> getReportsByScope(String scope);
    ReportDTO getReportById(Long id);
    void deleteReport(Long id);
}
