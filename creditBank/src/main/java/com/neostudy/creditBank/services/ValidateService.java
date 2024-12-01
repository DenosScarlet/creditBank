package com.neostudy.creditBank.services;

import com.neostudy.creditBank.dto.LoanStatementRequestDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

@Service
public class ValidateService {
    // Условия прескоринга
    static final Pattern namePattern = Pattern.compile("^[a-zA-Z]{2,30}$");
    static final Pattern emailPattern = Pattern.compile("^[a-z0-9A-Z_!#$%&'*+/=?`{|}~^.-]+@[a-z0-9A-Z.-]+$");
    static final Pattern passportSeriesPattern = Pattern.compile("^\\d{4}$");
    static final Pattern passportNumberPattern = Pattern.compile("^\\d{6}$");
    static final BigDecimal minAmount = BigDecimal.valueOf(20000);
    static final int minTerm = 6;
    static final int minAge = 18;

    public void validateAll(LoanStatementRequestDto loanStatementRequestDto) {
        // Валидация данных с заявки
        validateName(loanStatementRequestDto.getFirstName(), "First name");
        validateName(loanStatementRequestDto.getLastName(), "Last name");
        if (loanStatementRequestDto.getMiddleName() != null && !loanStatementRequestDto.getMiddleName().isEmpty()) {
            validateName(loanStatementRequestDto.getMiddleName(), "Middle name");
        }
        validateEmail(loanStatementRequestDto.getEmail());
        validateBirthdate(loanStatementRequestDto.getBirthdate());
        validateAmount(loanStatementRequestDto.getAmount());
        validateTerm(loanStatementRequestDto.getTerm());
        validatePassport(loanStatementRequestDto.getPassportSeries(), loanStatementRequestDto.getPassportNumber());
    }

    private void validateName(String name, String fieldName) {
        if (name == null || !namePattern.matcher(name).matches()) {
            throw new IllegalArgumentException(fieldName + " должен содержать от 2 до 30 латинских букв.");
        }
    }

    private void validateEmail(String email) {
        if (email == null || !emailPattern.matcher(email).matches()) {
            throw new IllegalArgumentException("Email введён некорректно.");
        }
    }

    private void validateBirthdate(LocalDate birthdate) {
        if (birthdate == null) {
            throw new IllegalArgumentException("Дата рождения отсутсвует.");
        }
        int age = Period.between(birthdate, LocalDate.now()).getYears();
        if (age < minAge) {
            throw new IllegalArgumentException("Кандидат должен быть не моложе " + minAge + " лет.");
        }
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(minAmount) < 0) {
            throw new IllegalArgumentException("Сумма займа не должен быть меньше " + minAmount);
        }
    }

    private void validateTerm(Integer term) {
        if (term == null || term < minTerm) {
            throw new IllegalArgumentException("Срок займа не должен быть меньше " + minTerm + " месяцев.");
        }
    }

    private void validatePassport(String passportSeries, String passportNumber) {
        if (passportSeries == null || !passportSeriesPattern.matcher(passportSeries).matches()) {
            throw new IllegalArgumentException("Серия паспорта должна содержать 4 цифры.");
        }
        if (passportNumber == null || !passportNumberPattern.matcher(passportNumber).matches()) {
            throw new IllegalArgumentException("Номер паспорта должен содержать 6 цифр.");
        }
    }
}
