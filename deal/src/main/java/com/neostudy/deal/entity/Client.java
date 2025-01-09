package com.neostudy.deal.entity;

import com.neostudy.calculator.enums.Gender;
import com.neostudy.calculator.enums.MaritalStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;
import java.util.Date;
import java.util.UUID;

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
