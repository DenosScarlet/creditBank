package com.neostudy.calculator.dto;

import com.neostudy.calculator.enums.Gender;
import com.neostudy.calculator.enums.MaritalStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Сущность заявки пользователя на кредит (для скоринга)")
@Data
public class ScoringDataDto {
    @Schema(description = "Сумма кредитования (включая стоимость страховки)", example = "400000")
    private BigDecimal amount;
    @Schema(description = "Срок кредитования, в месяцах (не менее 6 месяцев)", example = "12")
    private Integer term;
    @Schema(description = "Имя (от 2 до 30 латинских букв)", example = "Denis")
    private String firstName;
    @Schema(description = "Фамилия (от 2 до 30 латинских букв)", example = "Davydov")
    private String lastName;
    @Schema(description = "Отчество (от 2 до 30 латинских букв) (при наличии)", example = "Nikolaevich")
    private String middleName;
    @Schema(description = "Пол (MALE, FEMALE, NON_BINARY)", example = "MALE")
    private Gender gender;
    @Schema(description = "Дата рождения (возрасть должен быть не меньше 18 лет)", example = "2003-12-12")
    private LocalDate birthdate;
    @Schema(description = "Серия паспорта (4 цифры)", example = "1234")
    private String passportSeries;
    @Schema(description = "Номер паспорта", example = "123456")
    private String passportNumber;
    @Schema(description = "Дата выдачи паспорта паспорта (не более 20 лет)", example = "2023-12-03")
    private LocalDate passportIssueDate;
    @Schema(description = "Номер места выдачи (в формате NNN-NNN)", example = "999-888")
    private String passportIssueBranch;
    @Schema(description = "Семейное положение (SINGLE, MARRIED, DIVORCED)", example = "SINGLE")
    private MaritalStatus maritalStatus;
    @Schema(description = "Количество иждивенцев", example = "0")
    private Integer dependentAmount;
    @Schema(description = "Информация о месте работы", example = "SINGLE")
    private EmploymentDto employment;
    @Schema(description = "Номер счета (20 цифр)", example = "48569321568247965368")
    private String accountNumber;
    @Schema(description = "Кредитор переводит зарплату на карту нашего банка", example = "true")
    private Boolean isInsuranceEnabled;
    @Schema(description = "Кредитор берёт страховку", example = "true")
    private Boolean isSalaryClient;
}
