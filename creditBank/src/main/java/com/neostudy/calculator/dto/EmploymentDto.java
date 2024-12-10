package com.neostudy.calculator.dto;

import com.neostudy.calculator.enums.EmploymentStatus;
import com.neostudy.calculator.enums.Position;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "Сущность места работы кредитора")
@Data
public class EmploymentDto {
    @Schema(description = "Рабочий статус (UNEMPLOYED, SELF_EMPLOYED, EMPLOYED, BUSINESS_OWNER, RETIRED)", example = "BUSINESS_OWNER")
    private EmploymentStatus employmentStatus;
    @Schema(description = "ИНН места работы (12 цифр)", example = "456789123456")
    private String employerINN;
    @Schema(description = "Зарплата (не менее 10000)", example = "300000")
    private BigDecimal salary;
    @Schema(description = "Позиция на работе (WORKER, MANAGER, DIRECTOR, OWNER)", example = "OWNER")
    private Position position;
    @Schema(description = "Общий стаж работы, в месяцах (не менее 18 месяцев)", example = "36")
    private Integer workExperienceTotal;
    @Schema(description = "Текущий трудовой стаж, в месяцах (не менее 3 месяцев)", example = "12")
    private Integer workExperienceCurrent;
}
