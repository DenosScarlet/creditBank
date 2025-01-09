package com.neostudy.deal.entity;

import com.neostudy.calculator.dto.PaymentScheduleElementDto;
import com.neostudy.deal.enums.CreditStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

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
