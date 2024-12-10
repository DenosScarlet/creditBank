package com.neostudy.calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Сущность итогового кредитного предложения по итогам скоринга")
@Data
public class CreditDto {
    @Schema(description = "Итоговая сумма платежа по кредиту", example = "300000")
    private BigDecimal amount;
    @Schema(description = "Срок кредитования", example = "12")
    private Integer term;
    @Schema(description = "Ежемесячный платёж", example = "28000.00")
    private BigDecimal monthlyPayment;
    @Schema(description = "Итоговая ставка по кредиту", example = "12.0")
    private BigDecimal rate;
    @Schema(description = "Полная стоимость кредита", example = "33600.00")
    private BigDecimal psk;
    @Schema(description = "Кредитор переводит зарплату на карту нашего банка", example = "false")
    private Boolean isInsuranceEnabled;
    @Schema(description = "Кредитор берёт страховку", example = "false")
    private Boolean isSalaryClient;
    @Schema(description = "График ежемесячных платежей")
    private List<PaymentScheduleElementDto> paymentSchedule;
}
