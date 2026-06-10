package com.finsure.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationDTO {
    private Long notificationId;
    private Long userId;
    private Long entityId;
    private String message;
    private String category;
    private String status;
    private LocalDateTime createdAt;
}