package com.neostudy.creditBank.services;

import com.neostudy.creditBank.dto.LoanOfferDto;
import com.neostudy.creditBank.dto.LoanStatementRequestDto;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

@Service
public class PrescoringService {
    // Сервис для валидации данных с заявки
    ValidateService validateService = new ValidateService();
    // Файл с данными
    File propertiesFile = new File("C:\\Users\\dieni\\source\\creditBank\\creditBank\\src\\main\\resources\\data.properties");
    Properties properties = new Properties();

    // UUID для всех четырёх предложений
    UUID uuid1 = UUID.randomUUID();
    UUID uuid2 = UUID.randomUUID();
    UUID uuid3 = UUID.randomUUID();
    UUID uuid4 = UUID.randomUUID();
    // Метод прескоринга (Валидация)
    public List<LoanOfferDto> prescoring(LoanStatementRequestDto loanStatementRequestDto) throws IOException {

        validateService.validateAll(loanStatementRequestDto);

        List<LoanOfferDto> loanOfferDtos = new ArrayList<>();

        // Не зарплатный клиент без страховки
        LoanOfferDto loanOfferDto1 = createOffer(loanStatementRequestDto, uuid1, false, false);
        // Зарплатный клиент без страховки
        LoanOfferDto loanOfferDto2 = createOffer(loanStatementRequestDto, uuid2, false, true);
        // Не зарплатный клиент со страховкой
        LoanOfferDto loanOfferDto3 = createOffer(loanStatementRequestDto, uuid3, true, false);
        // Зарплатный клиент со страховкой
        LoanOfferDto loanOfferDto4 = createOffer(loanStatementRequestDto, uuid4, true, true);

        loanOfferDtos.add(loanOfferDto1);
        loanOfferDtos.add(loanOfferDto2);
        loanOfferDtos.add(loanOfferDto3);
        loanOfferDtos.add(loanOfferDto4);
        return loanOfferDtos;
    }
    // Метод для создания кредитного предложения
    private LoanOfferDto createOffer(LoanStatementRequestDto loanStatementRequestDto, UUID statementId, Boolean isInsuranceEnabled, Boolean isSalaryClient) throws IOException {
        BigDecimal requestedAmount = loanStatementRequestDto.getAmount();
        BigDecimal totalAmount;
        BigDecimal rate;
        BigDecimal monthlyPayment;
        Integer term = loanStatementRequestDto.getTerm();

        // Данные для подсчёта ставки, ежемесячного платежа и пск
        properties.load(new FileReader(propertiesFile));
        BigDecimal baseRate = new BigDecimal(properties.getProperty("base.rate"));
        BigDecimal insuranceRateDecrease = new BigDecimal(properties.getProperty("insurance.rate.decrease"));
        BigDecimal salaryClientRateDecrease = new BigDecimal(properties.getProperty("salary.client.rate.decrease"));
        BigDecimal insuranceCost = new BigDecimal(properties.getProperty("insurance.cost"));

        if (!isInsuranceEnabled && !isSalaryClient) {
            rate = baseRate;
            totalAmount = requestedAmount;
        } else if (!isInsuranceEnabled && isSalaryClient) {
            rate = baseRate.subtract(salaryClientRateDecrease);
            totalAmount = requestedAmount;
        } else if (isInsuranceEnabled && !isSalaryClient) {
            rate = baseRate.subtract(insuranceRateDecrease);
            totalAmount = requestedAmount.add(insuranceCost);
        } else {
            rate = baseRate.subtract(insuranceRateDecrease).subtract(salaryClientRateDecrease);
            totalAmount = requestedAmount.add(insuranceCost);
        }
        monthlyPayment = (totalAmount.multiply(BigDecimal.ONE.add(rate.divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP)))).divide(new BigDecimal(term), 2, RoundingMode.HALF_UP);

        LoanOfferDto loanOfferDto = new LoanOfferDto();
        loanOfferDto.setStatementId(statementId);
        loanOfferDto.setRequestedAmount(loanStatementRequestDto.getAmount());
        loanOfferDto.setTotalAmount(totalAmount);
        loanOfferDto.setTerm(term);
        loanOfferDto.setMonthlyPayment(monthlyPayment);
        loanOfferDto.setRate(rate);
        loanOfferDto.setIsInsuranceEnabled(isInsuranceEnabled);
        loanOfferDto.setIsSalaryClient(isSalaryClient);

        return loanOfferDto;
    }
}
