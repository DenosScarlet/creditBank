package com.neostudy.creditBank.dto;

import com.neostudy.creditBank.enums.EmploymentStatus;
import com.neostudy.creditBank.enums.Position;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class EmploymentDto {
    private EmploymentStatus employmentStatus;
    private String employerINN;
    private BigDecimal salary;
    private Position position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}
