package com.finsure.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class KPIDTO {
    private Long kpiId;
    private String name;
    private String definition;
    private Double target;
    private Double currentValue;
    private LocalDateTime reportingPeriod;
    private String category;
}