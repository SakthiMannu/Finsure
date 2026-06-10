package com.finsure.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class TaskDTO {
    private Long taskId;
    private Long assignedToUserId;
    private Long relatedEntityId;
    private String description;
    private LocalDate dueDate;
    private String status;
}