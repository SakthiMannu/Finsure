package com.finsure.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;




@Entity
@Table(name = "task")
@Data
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;


    private Long assignedToUserId;


    private Long relatedEntityId;

    private String description;
    private LocalDate dueDate;


    private String status;
}
