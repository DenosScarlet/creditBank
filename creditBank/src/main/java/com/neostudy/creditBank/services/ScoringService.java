package com.neostudy.creditBank.services;

import com.neostudy.creditBank.dto.CreditDto;
import com.neostudy.creditBank.dto.EmploymentDto;
import com.neostudy.creditBank.dto.PaymentScheduleElementDto;
import com.neostudy.creditBank.dto.ScoringDataDto;
import com.neostudy.creditBank.enums.EmploymentStatus;
import com.neostudy.creditBank.enums.Gender;
import com.neostudy.creditBank.enums.MaritalStatus;
import com.neostudy.creditBank.enums.Position;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class ScoringService {
    // Файл с данными
    File propertiesFile = new File("creditBank/src/main/resources/data.properties");
    Properties properties = new Properties();
    
    public CreditDto scoring(ScoringDataDto scoringDataDto) throws IOException {
        CreditDto creditDto = new CreditDto();
        properties.load(new FileReader(propertiesFile));

        BigDecimal rate = calculateRate(scoringDataDto.getIsInsuranceEnabled(), scoringDataDto.getIsSalaryClient());

        // Возраст клиента
        int age = Period.between(scoringDataDto.getBirthdate(), LocalDate.now()).getYears();
        if (age < 20 || age > 65) {
            throw new IllegalArgumentException("Клиент не соответствует возрастным требованиям.");
        }

        // Статус занятости
        EmploymentDto employment = scoringDataDto.getEmployment();
        if (employment.getEmploymentStatus() == EmploymentStatus.UNEMPLOYED) {
            throw new IllegalArgumentException("Клиент безработный. Кредит не может быть предоставлен.");
        } else if (employment.getEmploymentStatus() == EmploymentStatus.SELF_EMPLOYED) {
            rate = rate.add(BigDecimal.valueOf(2));
        } else if (employment.getEmploymentStatus() == EmploymentStatus.BUSINESS_OWNER) {
            rate = rate.add(BigDecimal.valueOf(1));
        }

        // Позиция на работе
        if (employment.getPosition() == Position.MANAGER) {
            rate = rate.subtract(BigDecimal.valueOf(2));
        } else if (employment.getPosition() == Position.DIRECTOR) {
            rate = rate.subtract(BigDecimal.valueOf(3));
        }

        // Стаж работы
        if (employment.getWorkExperienceTotal() < 18 || employment.getWorkExperienceCurrent() < 3) {
            throw new IllegalArgumentException("Недостаточный стаж работы.");
        }

        // Сумма займа относительно зарплаты
        BigDecimal maxLoanAmount = employment.getSalary().multiply(BigDecimal.valueOf(24));
        if (scoringDataDto.getAmount().compareTo(maxLoanAmount) > 0) {
            throw new IllegalArgumentException("Сумма займа превышает допустимый предел.");
        }

        // Семейное положение
        if (scoringDataDto.getMaritalStatus() == MaritalStatus.MARRIED) {
            rate = rate.subtract(BigDecimal.valueOf(3));
        } else if (scoringDataDto.getMaritalStatus() == MaritalStatus.DIVORCED) {
            rate = rate.add(BigDecimal.valueOf(1));
        }

        // Пол и возрастные группы
        if (scoringDataDto.getGender() == Gender.FEMALE && age >= 32 && age <= 60) {
            rate = rate.subtract(BigDecimal.valueOf(3));
        } else if (scoringDataDto.getGender() == Gender.MALE && age >= 30 && age <= 55) {
            rate = rate.subtract(BigDecimal.valueOf(3));
        } else if (scoringDataDto.getGender() == Gender.NON_BINARY) {
            rate = rate.add(BigDecimal.valueOf(7));
        }

        BigDecimal psk = calculateMonthlyPayment(calculateTotalAmount(scoringDataDto.getAmount(), scoringDataDto.getIsInsuranceEnabled()), scoringDataDto.getTerm(), new BigDecimal(scoringDataDto.getTerm())).multiply(new BigDecimal(scoringDataDto.getTerm()));

        creditDto.setAmount(calculateTotalAmount(scoringDataDto.getAmount(), scoringDataDto.getIsInsuranceEnabled()));
        creditDto.setTerm(scoringDataDto.getTerm());
        creditDto.setMonthlyPayment(calculateMonthlyPayment(calculateTotalAmount(scoringDataDto.getAmount(), scoringDataDto.getIsInsuranceEnabled()), scoringDataDto.getTerm(), rate));
        creditDto.setRate(rate);
        creditDto.setPsk(psk);
        creditDto.setIsInsuranceEnabled(scoringDataDto.getIsInsuranceEnabled());
        creditDto.setIsSalaryClient(scoringDataDto.getIsSalaryClient());
        creditDto.setPaymentSchedule(generatePaymentSchedule(calculateTotalAmount(scoringDataDto.getAmount(), scoringDataDto.getIsInsuranceEnabled()), scoringDataDto.getTerm(), rate, LocalDate.now()));

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

    public BigDecimal calculateMonthlyPayment(BigDecimal totalAmount, Integer term, BigDecimal rate) {
        return (totalAmount.multiply(BigDecimal.ONE.add(rate.divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP)))).divide(new BigDecimal(term), 2, RoundingMode.HALF_UP);
    }

    public List<PaymentScheduleElementDto> generatePaymentSchedule(
            BigDecimal totalAmount,
            Integer term,
            BigDecimal rate,
            LocalDate startDate
    ) {
        List<PaymentScheduleElementDto> schedule = new ArrayList<>();

        // Ежемесячная процентная ставка
        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(12), 6, RoundingMode.HALF_UP).divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);
        BigDecimal monthlyPayment = calculateMonthlyPayment(totalAmount, term, rate);

        BigDecimal remainingDebt = totalAmount;

        for (int i = 1; i <= term; i++) {
            BigDecimal interestPayment = remainingDebt.multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP);
            BigDecimal debtPayment = monthlyPayment.subtract(interestPayment).setScale(2, RoundingMode.HALF_UP);
            remainingDebt = remainingDebt.subtract(debtPayment).setScale(2, RoundingMode.HALF_UP);

            // Элемент графика
            PaymentScheduleElementDto element = new PaymentScheduleElementDto();
            element.setNumber(i);
            element.setDate(startDate.plusMonths(i - 1));
            element.setTotalPayment(monthlyPayment);
            element.setInterestPayment(interestPayment);
            element.setDebtPayment(debtPayment);
            element.setRemainingDebt(remainingDebt.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : remainingDebt);

            schedule.add(element);
        }

        return schedule;
    }
}
