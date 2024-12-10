package com.neostudy.calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Сущность кредитного предложения после прескоринга")
@Data
public class LoanOfferDto {
    @Schema(description = "Уникальный идентифаикатор заявки", example = "82d7bf1e-4476-472e-a446-7114beac4aaa")
    private UUID statementId;
    @Schema(description = "Запрашиваемая сумма кредита", example = "300000")
    private BigDecimal requestedAmount;
    @Schema(description = "Итоговая сумма кредита (запрашиваемая сумма увеличивается на 100000, если кредитор берёт страховку)", example = "300000")
    private BigDecimal totalAmount;
    @Schema(description = "Срок кредитования", example = "12")
    private Integer term;
    @Schema(description = "Ежемесячный платёж, расчитанный по итогам прескоринга", example = "28750.00")
    private BigDecimal monthlyPayment;
    @Schema(description = "Ставка по кредиту по итогам прескоринга", example = "15.0")
    private BigDecimal rate;
    @Schema(description = "Кредитор переводит зарплату на карту нашего банка", example = "false")
    private Boolean isInsuranceEnabled;
    @Schema(description = "Кредитор берёт страховку", example = "false")
    private Boolean isSalaryClient;
}
