package com.neostudy.creditBank.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Сущность первичной заявки пользователя на кредит (для прескоринга)")
@Data
public class LoanStatementRequestDto {
    @Schema(description = "Запрашиваемая сумма кредита (не менее 20000)", example = "300000")
    private BigDecimal amount;
    @Schema(description = "Срок кредитования, в месяцах (не менее 6)", example = "12")
    private Integer term;
    @Schema(description = "Имя (от 2 до 30 латинских букв)", example = "Denis")
    private String firstName;
    @Schema(description = "Фамилия (от 2 до 30 латинских букв)", example = "Davydov")
    private String lastName;
    @Schema(description = "Отчество (от 2 до 30 латинских букв) (при наличии)", example = "Nikolaevich")
    private String middleName;
    @Schema(description = "Адрес электроннй почты", example = "my.own.address@mail.ru")
    private String email;
    @Schema(description = "Дата рождения (возрасть должен быть не меньше 18 лет)", example = "2003-12-12")
    private LocalDate birthdate;
    @Schema(description = "Серия паспорта (4 цифры)", example = "1234")
    private String passportSeries;
    @Schema(description = "Номер паспорта", example = "123456")
    private String passportNumber;
}
