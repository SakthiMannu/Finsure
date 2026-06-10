package com.finsure.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "audit_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long auditId;

	    private Long userId;
	    private String action;


	    @Column(nullable = true)
	    private String resourceType;

	    @Column(nullable = true)
	    private String resourceId;

	    @Column(nullable = true)
	    private String details;

	    private LocalDateTime timestamp;


}

