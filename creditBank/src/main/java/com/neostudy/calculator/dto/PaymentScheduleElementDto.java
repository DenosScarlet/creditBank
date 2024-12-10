package com.neostudy.calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Сущность элемента графика ежемесячных платежей")
@Data
public class PaymentScheduleElementDto {
    @Schema(description = "Номер платежа", example = "1")
    private Integer number;
    @Schema(description = "Дата платежа")
    private LocalDate date;
    private BigDecimal totalPayment;
    private BigDecimal interestPayment;
    private BigDecimal debtPayment;
    private BigDecimal remainingDebt;
}