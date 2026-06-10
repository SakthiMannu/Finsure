package com.finsure.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;



@Entity
@Table(name = "kpi")
@Data
public class KPI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kpiId;

    private String name;
    private String definition;


    private Double target;


    private Double currentValue;

    private LocalDateTime reportingPeriod;


    private String category;
}
