package com.finsure.dto;

import lombok.Data;

@Data
public class MemberReportDTO {
    private Long memberId;
    private String name;
    private String status; 
}
