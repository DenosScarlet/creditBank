package com.neostudy.creditBank.services;

import com.neostudy.creditBank.dto.CreditDto;
import com.neostudy.creditBank.dto.ScoringDataDto;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Properties;

@Service
public class ScoringService {
    // Файл с данными
    File propertiesFile = new File("creditBank/src/main/resources/data.properties");
    Properties properties = new Properties();

    public CreditDto scoring(ScoringDataDto scoringDataDto) {
        CreditDto creditDto = new CreditDto();

        //TODO Реализовать скоринг

        return creditDto;
    }

    public BigDecimal calculateTotalAmount(BigDecimal requestedAmount, boolean isInsuranceEnabled) throws IOException {
        // Данные для подсчёта ставки, ежемесячного платежа и итоговой суммы займа
        properties.load(new FileReader(propertiesFile));
        BigDecimal insuranceCost = new BigDecimal(properties.getProperty("insurance.cost"));

        if (!isInsuranceEnabled) {
            return requestedAmount;
        } else {
            return requestedAmount.add(insuranceCost);
        }
    }

    public BigDecimal calculateRate(boolean isInsuranceEnabled, boolean isSalaryClient) throws IOException {
        // Данные для подсчёта ставки, ежемесячного платежа и итоговой суммы займа
        properties.load(new FileReader(propertiesFile));
        BigDecimal baseRate = new BigDecimal(properties.getProperty("base.rate"));
        BigDecimal insuranceRateDecrease = new BigDecimal(properties.getProperty("insurance.rate.decrease"));
        BigDecimal salaryClientRateDecrease = new BigDecimal(properties.getProperty("salary.client.rate.decrease"));

        if (!isInsuranceEnabled && !isSalaryClient) {
            return baseRate;
        } else if (!isInsuranceEnabled && isSalaryClient) {
            return baseRate.subtract(salaryClientRateDecrease);
        } else if (isInsuranceEnabled && !isSalaryClient) {
            return baseRate.subtract(insuranceRateDecrease);
        } else {
            return baseRate.subtract(insuranceRateDecrease).subtract(salaryClientRateDecrease);
        }
    }

    public BigDecimal calculateMonthlyPayment(BigDecimal totalAmount, Integer term, BigDecimal rate) throws IOException {
        return (totalAmount.multiply(BigDecimal.ONE.add(rate.divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP)))).divide(new BigDecimal(term), 2, RoundingMode.HALF_UP);
    }

}
