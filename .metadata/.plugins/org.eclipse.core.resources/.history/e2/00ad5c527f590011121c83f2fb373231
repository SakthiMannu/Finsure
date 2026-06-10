package com.finsure.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequestDTO {

	@NotBlank(message = "Account type is required")
	@Pattern(regexp = "^(SAVINGS|CHECKING)$",
	         message = "Account type must be SAVINGS or CHECKING")
	private String type;

	 @Min(value = 0, message = "Opening balance cannot be negative")
	 private double balance;


	    @NotNull(message = "Please provide the MemberID")
	    private Long memberId;
}
