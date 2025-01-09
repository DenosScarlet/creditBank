package com.neostudy.deal.entity;

import com.neostudy.deal.enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Timestamp;
import java.util.UUID;

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
