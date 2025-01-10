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

![Screenshot_135](https://github.com/user-attachments/assets/741ec9ae-a8f5-4d7b-a3d9-9ee183724995)

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
![Screenshot_133](https://github.com/user-attachments/assets/5791738a-3a1d-4781-92e4-64a55bd01839)

Табличку генерировать он отказался :(

### 1.5 Задокументировал API через Swagger

Заняться логгированием и тестами пока не успеваю в срок, пока что только вот

![image](https://github.com/user-attachments/assets/51e8928b-c8a1-4a96-9be7-88d385c458e8)

![image](https://github.com/user-attachments/assets/51065978-f1ee-4150-b234-dc6b762320fd)

![image](https://github.com/user-attachments/assets/33894273-55c2-4528-aa03-aef60b32c0b3)

![image](https://github.com/user-attachments/assets/ace7a77f-2ca5-4971-b87c-802b8128939b)

![image](https://github.com/user-attachments/assets/727a3d24-10d7-4d82-8886-d838f628cbef)

![image](https://github.com/user-attachments/assets/871aea75-2b9b-4d0d-98e7-cfed1eef350f)

![image](https://github.com/user-attachments/assets/4be67d48-9de0-4d7f-aa19-6560167ffb04)

## MVP Level 2 Реализация микросервиса Сделка (deal)

### 2.1. Инициализировал Spring-Boot приложение deal

### 2.2. Определил сущности для БД

![image](https://github.com/user-attachments/assets/2afcd66a-9642-4172-8e52-03903aeee1f8)

Сущность _Client_
```Java
@Entity
@Accessors(chain = true)
@Data
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID client_id;

    @Column(name = "last_name")
    private String last_name;

    @Column(name = "first_name")
    private String first_name;

    @Column(name = "middle_name")
    private String middle_name;

    @Column(name = "birth_date")
    private Date birth_date;

    @Column(name = "email")
    private String email;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "marital_status")
    private MaritalStatus marital_status;

    @Column(name = "dependentAmount")
    private BigInteger dependentAmount;

    @Column(columnDefinition = "jsonb", name = "passport_id")
    private String passport_id;

    @Column(columnDefinition = "jsonb", name = "employment_id")
    private String employment_id;

    @Column(name = "accountNumber")
    private String accountNumber;
}
```

Сущность _Statement_

```Java
@Entity
@Accessors(chain = true)
@Data
@Table(name = "statement")
public class Statement {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID statement_id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", foreignKey = @ForeignKey(name = "fk_statement_client_id"))
    private Client client;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_id", foreignKey = @ForeignKey(name = "fk_statement_credit_id"))
    private Credit credit;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ApplicationStatus status;

    @Column(name = "creation_date")
    private Timestamp creation_date;

    @Column(columnDefinition = "jsonb", name = "applied_offer")
    private String applied_offer;

    @Column(name = "sign_date")
    private Timestamp sign_date;

    @Column(name = "ses_code")
    private String ses_code;

    @Column(columnDefinition = "jsonb", name = "status_history")
    private String status_history;
}
```

Сущность _Credit_

```Java
@Entity
@Accessors(chain = true)
@Data
@Table(name = "credit")
public class Credit {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID credit_id;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "term")
    private Integer term;

    @Column(name = "monthly_payment")
    private BigDecimal monthly_payment;

    @Column(name = "rate")
    private BigDecimal rate;

    @Column(name = "psk")
    private BigDecimal psk;

    @Column(columnDefinition = "jsonb", name = "payment_schedule")
    private String payment_schedule;

    @Column(name = "insurance_enabled")
    private Boolean insurance_enabled;

    @Column(name = "salary_client")
    private Boolean salary_client;

    @Enumerated(EnumType.STRING)
    @Column(name = "credit_status")
    private CreditStatus credit_status;
}
```
### 2.3. Подключил проект к СУБД Postgres
Итог после запуска приложения:

![image](https://github.com/user-attachments/assets/7cd2c718-51aa-4355-893a-9585a4f30a5b)

(Я не знаю почему на схеме Postgre нарисовали связи один ко многим, хотя я явно указывал 1 к 1)


