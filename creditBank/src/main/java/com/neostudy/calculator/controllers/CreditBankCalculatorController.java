package com.neostudy.calculator.controllers;

import com.neostudy.calculator.dto.LoanStatementRequestDto;
import com.neostudy.calculator.dto.ScoringDataDto;
import com.neostudy.calculator.services.PrescoringService;
import com.neostudy.calculator.services.ScoringService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/calculator")
@Tag(name = "Контроллер банковского калькулятора", description = "Скоринг и прескоринг данных")
public class CreditBankCalculatorController {
    private final PrescoringService prescoringService;
    private final ScoringService scoringService;

    @Autowired
    public CreditBankCalculatorController(PrescoringService prescoringService, ScoringService scoringService) {
        this.prescoringService = prescoringService;
        this.scoringService = scoringService;
    }

    @Operation(
            summary = "Прескоринг (расчёт возможных условий кредита)",
            description = "1. По API приходит LoanStatementRequestDto.\n" +
                    "2. На основании LoanStatementRequestDto происходит прескоринг, создаётся 4 кредитных предложения LoanOfferDto на основании всех возможных комбинаций булевских полей isInsuranceEnabled и isSalaryClient (false-false, false-true, true-false, true-true).\n" +
                    "3. Логику формирования кредитных предложений можно придумать самому.\n" +
                    "4. К примеру: в зависимости от страховых услуг увеличивается/уменьшается процентная ставка и сумма кредита, базовая ставка хардкодится в коде через property файл. Например цена страховки 100к (или прогрессивная, в зависимости от запрошенной суммы кредита), ее стоимость добавляется в тело кредита, но она уменьшает ставку на 3. Цена зарплатного клиента 0, уменьшает ставку на 1.\n" +
                    "5. Ответ на API - список из 4х LoanOfferDto от \"худшего\" к \"лучшему\" (чем меньше итоговая ставка, тем лучше)."
    )
    @PostMapping("/offers")
    public ResponseEntity<String> offers(@RequestBody @Parameter(description = "Данные клиенты для прескоринга", required = true) LoanStatementRequestDto loanStatement) throws IOException {
        return ResponseEntity.ok(prescoringService.prescoring(loanStatement).toString());
    }

    @Operation(
            summary = "Скоринг (валидация присланных данных + полный расчет параметров кредита)",
            description = "1. По API приходит ScoringDataDto.\n" +
                    "2. Происходит скоринг данных, высчитывание итоговой ставки(rate), полной стоимости кредита(psk), размер ежемесячного платежа(monthlyPayment), график ежемесячных платежей (List<PaymentScheduleElementDto>). Логику расчета параметров кредита можно найти в интернете, полученный результат сверять с имеющимися в интернете калькуляторами графиков платежей и ПСК.\n" +
                    "3. Ответ на API - CreditDto, насыщенный всеми рассчитанными параметрами."
    )
    @PostMapping("/calc")
    public ResponseEntity<String> calc(@RequestBody @Parameter(description = "Данные клиента для скоринга", required = true) ScoringDataDto scoringData) throws IOException {
        return ResponseEntity.ok(scoringService.scoring(scoringData).toString());
    }

}
