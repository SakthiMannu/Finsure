package com.finsure.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDTO {

	 private Long memberId;
	    private String name;
	    private LocalDate dob;
	    private String contactInfo;
	    private String status;
}
