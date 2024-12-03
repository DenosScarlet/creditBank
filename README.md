# creditBank
Проектное задание: Java Development (осенний набор 2024). Бэкенд-приложение с микросервисной архитектурой - прототип небольшого банка.

>[!NOTE]
> Я занимаюсь таким большим проектом впервые, поэтому на многие вещи у меня может уходить больше времени, чем предполагалось. :sweat:  
> Многие технологие, используемые здесь я изучаю первый раз, поскольку в ВУЗе у меня их банально не преподавали, разве что основы Java (сейчас я на 4 курсе ННГАСУ).  
> В общем, прошу понять и простить за возможные будущие задержки в сдаче заданий. Мне дико нравится этим заниматься, я прям потею над каждым заданием. :nerd_face:
## MVP Level 1 Реализация микросервиса Калькулятор (calculator)
### 1.1 Определена ветка develop, отведённая от main и 2 ветки feature и bugfix, отведённые от develop.

![image](https://github.com/user-attachments/assets/9daee438-6e81-484d-9329-ef8aa1094484)

История коммитов 

![image](https://github.com/user-attachments/assets/4461fded-398f-4d19-ae0f-02560a8d0c94)

### 1.2 Определил DTO
(Использовал аннотацию @Data Lombok для создания геттеров и сеттеров)

_LoanStatementRequestDto_
```Java
public class LoanStatementRequestDto {
    private BigDecimal amount;
    private Integer term;
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private LocalDate birthdate;
    private String passportSeries;
    private String passportNumber;
}
```
_LoanOfferDto_
```Java
public class LoanOfferDto {
    private UUID statementId;
    private BigDecimal requestedAmount;
    private BigDecimal totalAmount;
    private Integer term;
    private BigDecimal monthlyPayment;
    private BigDecimal rate;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;
}
```
_ScoringDataDto_
```Java
public class ScoringDataDto {
    private BigDecimal amount;
    private Integer term;
    private String firstName;
    private String lastName;
    private String middleName;
    private Gender gender;
    private LocalDate birthdate;
    private String passportSeries;
    private String passportNumber;
    private LocalDate passportIssueDate;
    private String passportIssueBranch;
    private MaritalStatus maritalStatus;
    private Integer dependentAmount;
    private EmploymentDto employment;
    private String accountNumber;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;
}
```
_CreditDto_
```Java
public class CreditDto {
    private BigDecimal amount;
    private Integer term;
    private BigDecimal monthlyPayment;
    private BigDecimal rate;
    private BigDecimal psk;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;
    private List<PaymentScheduleElementDto> paymentSchedule;
}
```
_EmploymentDto_
```Java
public class EmploymentDto {
    private EmploymentStatus employmentStatus;
    private String employerINN;
    private BigDecimal salary;
    private Position position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}
```
_PaymentScheduleElementDto_
```Java
public class PaymentScheduleElementDto {
    private Integer number;
    private LocalDate date;
    private BigDecimal totalPayment;
    private BigDecimal interestPayment;
    private BigDecimal debtPayment;
    private BigDecimal remainingDebt;
}
```

### 1.3 Определил API в контроллере _CreditBankCalculatorController_

POST: /calculator/offers - расчёт возможных условий кредита. Request - _LoanStatementRequestDto_, response - _List<LoanOfferDto>_

```Java
@PostMapping("/offers")
    public ResponseEntity<String> offers(@RequestBody LoanStatementRequestDto loanStatement) throws IOException {
        return ResponseEntity.ok(prescoringService.prescoring(loanStatement).toString());
    }
```

POST: /calculator/calc - валидация присланных данных + скоринг данных + полный расчет параметров кредита. Request - _ScoringDataDto_, response _CreditDto_.

```Java
@PostMapping("/calc")
    public ResponseEntity<String> calc(@RequestBody ScoringDataDto scoringData) throws IOException {
        return ResponseEntity.ok(scoringService.scoring(scoringData).toString());
    }
```

### 1.4 Реализовал логику работы API

**POST: /calculator/offers**

1. По API приходит _LoanStatementRequestDto_.  
2. На основании _LoanStatementRequestDto_ происходит прескоринг, создаётся 4 кредитных предложения _LoanOfferDto_ на основании всех возможных комбинаций булевских полей _isInsuranceEnabled_ и _isSalaryClient_ (_false-false, false-true, true-false, true-true_).
Логику формирования кредитных предложений можно придумать самому.  
К примеру: в зависимости от страховых услуг увеличивается/уменьшается процентная ставка и сумма кредита, базовая ставка хардкодится в коде через _property_ файл. Например цена страховки 100к (или прогрессивная, в зависимости от запрошенной суммы кредита), ее стоимость добавляется в тело кредита, но она уменьшает ставку на 3. Цена зарплатного клиента 0, уменьшает ставку на 1.
Ответ на API - список из 4х _LoanOfferDto_ от "худшего" к "лучшему" (чем меньше итоговая ставка, тем лучше).

Результат в Postman по запросу

```JSON
{
  "amount": "200000",
  "term": "12",
  "firstName": "Denis",
  "lastName": "Davydov",
  "middleName": "Nikolaevich",
  "email": "asdaddad@mail.ru",
  "birthdate": "2003-05-12",
  "passportSeries": "4564",
  "passportNumber": "564654"
}
```

![Screenshot_133](https://github.com/user-attachments/assets/5791738a-3a1d-4781-92e4-64a55bd01839)

Также он сгенерировал таблицу
![Screenshot_134](https://github.com/user-attachments/assets/45500c85-6f20-450a-b60e-8772498a7e67)


**POST: /calculator/calc**

По API приходит _ScoringDataDto_.
Происходит скоринг данных, высчитывание итоговой ставки(_rate_), полной стоимости кредита(_psk_), размер ежемесячного платежа(_monthlyPayment_), график ежемесячных платежей (_List<PaymentScheduleElementDto>_). Логику расчета параметров кредита можно найти в интернете, полученный результат сверять с имеющимися в интернете калькуляторами графиков платежей и ПСК.
Ответ на API - _CreditDto_, насыщенный всеми рассчитанными параметрами.

Результат в Postman по запросу

```JSON
{
  "amount": 200000,
  "term": 12,
  "firstName": "Denis",
  "lastName": "Davydov",
  "middleName": "Nikolaevich",
  "gender": "MALE",
  "birthdate": "2003-12-03",
  "passportSeries": "4564",
  "passportNumber": "667546",
  "passportIssueDate": "2023-12-03",
  "passportIssueBranch": "999-888",
  "maritalStatus": "SINGLE",
  "dependentAmount": 0,
  "employment": {
    "employmentStatus": "BUSINESS_OWNER",
    "employerINN": "456789123456",
    "salary": 300000,
    "position": "OWNER",
    "workExperienceTotal": 36,
    "workExperienceCurrent": 12
  },
  "accountNumber": "48569321568247965368",
  "isInsuranceEnabled": true,
  "isSalaryClient": true
}
```
![Screenshot_135](https://github.com/user-attachments/assets/741ec9ae-a8f5-4d7b-a3d9-9ee183724995)

Табличку генерировать он отказался :(

### 1.5 Начал документировать API через Swagger

Заняться логгированием, тестами и полной документацией пока не успеваю в срок, пока что только вот

![image](https://github.com/user-attachments/assets/51e8928b-c8a1-4a96-9be7-88d385c458e8)

![image](https://github.com/user-attachments/assets/307c77b2-3733-4830-bbcc-323c79efbced)

![image](https://github.com/user-attachments/assets/61144066-9e8c-41e2-9f98-eb4de13d821a)

![image](https://github.com/user-attachments/assets/f18ab5ea-7d7b-47af-a212-72b57d78922b)

![image](https://github.com/user-attachments/assets/d79f24ad-3ae1-4741-bc6d-698f5bb2f7b8)

![image](https://github.com/user-attachments/assets/7b11b856-04ba-440c-89b3-7f8b23d36454)





