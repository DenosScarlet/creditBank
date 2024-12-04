package com.neostudy.creditBank.dto;

import com.neostudy.creditBank.enums.Gender;
import com.neostudy.creditBank.enums.MaritalStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Сущность заявки пользователя на кредит (для скоринга)")
@Data
public class ScoringDataDto {
    @Schema(description = "Сумма кредитования (включая стоимость страховки)", example = "400000")
    private BigDecimal amount;
    @Schema(description = "Сущность заявки пользователя на кредит (для скоринга)", example = "false")
    private Integer term;
    private String firstName;
    private String lastName;
    private String middleName;
    private Gender gender;
    private LocalDate birthdate;
    private String passportSeries;
    private String passportNumber;
    private LocalDate passportIssueDate;
    private String passportIssueBranch;
    private MaritalStatus maritalStatus;
    private Integer dependentAmount;
    private EmploymentDto employment;
    private String accountNumber;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;
}
