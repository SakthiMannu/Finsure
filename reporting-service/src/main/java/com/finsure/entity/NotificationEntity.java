package com.finsure.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;




@Entity
@Table(name = "notifications")
@Data
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;


    private Long userId;


    private Long entityId;

    private String message;


    private String category;


    private String status;

    private LocalDateTime createdAt;
}
