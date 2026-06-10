package com.finsure.dto;

import lombok.Data;
import java.time.LocalDate;



@Data
public class AccountDTO {
    private Long accountId;
    private Long memberId;
    private String type;
    private double balance;
    private LocalDate createdAt;
    private String status;
}
