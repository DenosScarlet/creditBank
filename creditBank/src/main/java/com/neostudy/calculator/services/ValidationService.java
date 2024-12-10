package com.neostudy.calculator.services;

import com.neostudy.calculator.dto.EmploymentDto;
import com.neostudy.calculator.dto.LoanStatementRequestDto;
import com.neostudy.calculator.dto.ScoringDataDto;
import com.neostudy.calculator.enums.EmploymentStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

@Service
public class ValidationService {
    // Данные для прескоринга и скоринга
    private static final Pattern namePattern = Pattern.compile("^[a-zA-Z]{2,30}$");
    private static final Pattern emailPattern = Pattern.compile("^[a-z0-9A-Z_!#$%&'*+/=?`{|}~^.-]+@[a-z0-9A-Z.-]+$");
    private static final Pattern passportSeriesPattern = Pattern.compile("^\\d{4}$");
    private static final Pattern passportNumberPattern = Pattern.compile("^\\d{6}$");
    private static final Pattern PASSPORT_BRANCH_PATTERN = Pattern.compile("^\\d{3}-\\d{3}$");
    private static final Pattern ACCOUNT_NUMBER_PATTERN = Pattern.compile("^\\d{20}$");
    private static final Pattern EMPLOYER_INN_PATTERN = Pattern.compile("^\\d{12}$");
    private static final BigDecimal MIN_SALARY = BigDecimal.valueOf(10_000);
    private static final BigDecimal minAmount = BigDecimal.valueOf(20000);
    private static final int minTerm = 6;
    private static final int minAge = 18;

    // Валидация данных для прескоринга
    public void validateAll(LoanStatementRequestDto loanStatementRequestDto) {
        // Валидация данных с заявки
        validateName(loanStatementRequestDto.getFirstName(), "Имя");
        validateName(loanStatementRequestDto.getLastName(), "Фамилия");
        if (loanStatementRequestDto.getMiddleName() != null && !loanStatementRequestDto.getMiddleName().isEmpty()) {
            validateName(loanStatementRequestDto.getMiddleName(), "Отчество");
        }
        validateEmail(loanStatementRequestDto.getEmail());
        validateBirthdate(loanStatementRequestDto.getBirthdate());
        validateAmount(loanStatementRequestDto.getAmount());
        validateTerm(loanStatementRequestDto.getTerm());
        validatePassport(loanStatementRequestDto.getPassportSeries(), loanStatementRequestDto.getPassportNumber());
    }

    // Валидация данных для скоринга
    public void validateAll(ScoringDataDto scoringDataDto) {
        validateAmount(scoringDataDto.getAmount());
        validateTerm(scoringDataDto.getTerm());
        validateName(scoringDataDto.getFirstName(), "Имя");
        validateName(scoringDataDto.getLastName(), "Фамилия");
        if (scoringDataDto.getMiddleName() != null && !scoringDataDto.getMiddleName().isEmpty()) {
            validateName(scoringDataDto.getMiddleName(), "Отчество");
        }
        validateGender(scoringDataDto.getGender());
        validateBirthdate(scoringDataDto.getBirthdate());
        validatePassport(scoringDataDto.getPassportSeries(), scoringDataDto.getPassportNumber());
        validatePassportIssueDate(scoringDataDto.getPassportIssueDate());
        validatePassportIssueBranch(scoringDataDto.getPassportIssueBranch());
        validateMaritalStatus(scoringDataDto.getMaritalStatus());
        validateDependentAmount(scoringDataDto.getDependentAmount());
        validateEmployment(scoringDataDto.getEmployment());
        validateAccountNumber(scoringDataDto.getAccountNumber());
    }

    public void validateName(String name, String fieldName) {
        if (name == null || !namePattern.matcher(name).matches()) {
            throw new IllegalArgumentException(fieldName + " должен содержать от 2 до 30 латинских букв.");
        }
    }

    public void validateEmail(String email) {
        if (email == null || !emailPattern.matcher(email).matches()) {
            throw new IllegalArgumentException("Email введён некорректно.");
        }
    }

    public void validateBirthdate(LocalDate birthdate) {
        if (birthdate == null) {
            throw new IllegalArgumentException("Дата рождения отсутсвует.");
        }
        int age = Period.between(birthdate, LocalDate.now()).getYears();
        if (age < minAge) {
            throw new IllegalArgumentException("Кандидат должен быть не моложе " + minAge + " лет.");
        }
    }

    public void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(minAmount) < 0) {
            throw new IllegalArgumentException("Сумма займа не должен быть меньше " + minAmount);
        }
    }

    public void validateTerm(Integer term) {
        if (term == null || term < minTerm) {
            throw new IllegalArgumentException("Срок займа не должен быть меньше " + minTerm + " месяцев.");
        }
    }

    public void validatePassport(String passportSeries, String passportNumber) {
        if (passportSeries == null || !passportSeriesPattern.matcher(passportSeries).matches()) {
            throw new IllegalArgumentException("Серия паспорта должна содержать 4 цифры.");
        }
        if (passportNumber == null || !passportNumberPattern.matcher(passportNumber).matches()) {
            throw new IllegalArgumentException("Номер паспорта должен содержать 6 цифр.");
        }
    }

    public void validateGender(Enum gender) {
        if (gender == null) {
            throw new IllegalArgumentException("Пол отсутствует.");
        }
    }

    public void validatePassportIssueDate(LocalDate passportIssueDate) {
        if (passportIssueDate == null) {
            throw new IllegalArgumentException("Дата выдачи паспорта отсутствует.");
        }
        if (passportIssueDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Дата выдачи паспорта не может быть позднее сегодняшней даты.");
        }
        if (Period.between(passportIssueDate, LocalDate.now()).getYears() > 20) {
            throw new IllegalArgumentException("Дата выдачи паспорта не может быть больше 20 лет.");
        }
    }

    public void validatePassportIssueBranch(String passportIssueBranch) {
        if (passportIssueBranch == null || !PASSPORT_BRANCH_PATTERN.matcher(passportIssueBranch).matches()) {
            throw new IllegalArgumentException("Номер места выдачи паспорта должен быть в формате NNN-NNN.");
        }
    }

    public void validateMaritalStatus(Enum maritalStatus) {
        if (maritalStatus == null) {
            throw new IllegalArgumentException("Семейное положение отсутствует.");
        }
    }

    public void validateDependentAmount(Integer dependentAmount) {
        if (dependentAmount == null || dependentAmount < 0 || dependentAmount > 10) {
            throw new IllegalArgumentException("Количество иждивенцев должно быть в диапазоне от 0 до 10.");
        }
    }

    public void validateEmployment(EmploymentDto employment) {
        if (employment == null) {
            throw new IllegalArgumentException("Информация о трудоустройстве отсутствует.");
        }

        if (employment.getEmploymentStatus() == null) {
            throw new IllegalArgumentException("Статус занятости отсутствует.");
        }

        if (employment.getEmploymentStatus() != EmploymentStatus.UNEMPLOYED && employment.getEmploymentStatus() != EmploymentStatus.RETIRED) {
            if (employment.getEmployerINN() == null || !EMPLOYER_INN_PATTERN.matcher(employment.getEmployerINN()).matches()) {
                throw new IllegalArgumentException("ИНН работодателя должен состоять из 12 цифр.");
            }
            if (employment.getSalary() == null || employment.getSalary().compareTo(MIN_SALARY) < 0) {
                throw new IllegalArgumentException("Зарплата должна быть не менее " + MIN_SALARY + ".");
            }
            if (employment.getPosition() == null) {
                throw new IllegalArgumentException("Не указана должность.");
            }
        }

        if (employment.getWorkExperienceTotal() == null || employment.getWorkExperienceTotal() < 18) {
            throw new IllegalArgumentException("Общий стаж работы должен быть не менее 18 месяцев.");
        }

        if (employment.getWorkExperienceCurrent() == null || employment.getWorkExperienceCurrent() < 3) {
            throw new IllegalArgumentException("Текущий опыт работы должен быть не менее 3 месяцев.");
        }

        if (employment.getWorkExperienceCurrent() > employment.getWorkExperienceTotal()) {
            throw new IllegalArgumentException("Текущий трудовой стаж не может превышать общий трудовой стаж.");
        }
    }

    public void validateAccountNumber(String accountNumber) {
        if (accountNumber == null || !ACCOUNT_NUMBER_PATTERN.matcher(accountNumber).matches()) {
            throw new IllegalArgumentException("Номер счета должен состоять из 20 цифр.");
        }
    }

}
