package com.finsure.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;



@Entity
@Table(name = "audit_package")
@Data
public class AuditPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long packageId;

    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;


    @Column(columnDefinition = "TEXT")
    private String contentsJSON;

    private LocalDateTime generatedAt;


    private String packageURI;
}
