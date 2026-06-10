package com.finsure.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;



@Entity
@Table(name = "report")
@Data
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;


    private String scope;



    @Column(columnDefinition = "TEXT")
    private String parametersJSON;


    @Column(columnDefinition = "TEXT")
    private String metricsJSON;


    private String generatedBy;

    private LocalDateTime generatedAt;


    private String reportURI;
}
