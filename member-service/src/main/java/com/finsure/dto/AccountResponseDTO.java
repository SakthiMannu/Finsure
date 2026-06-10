package com.finsure.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDTO {

	  private Long accountId;
	    private Long memberId;
	    private String type;
	    private double balance;
	    private LocalDate createdAt;
	    private String status;
}
