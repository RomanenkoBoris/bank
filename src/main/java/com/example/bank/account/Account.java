package com.example.bank.account;

import com.example.bank.client.Client;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(min = 1, max = 100, message = "Name must be from 1 to 100 characters")
    @NotBlank(message = "Account name is mandatory")
    @Column (columnDefinition = "varchar(100)")
    private String name;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Account type is mandatory")
    @Column(name = "account_type", columnDefinition = "varchar(50)", nullable = false)
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Account status is mandatory")
    @Column(name = "account_status", columnDefinition = "varchar(50)", nullable = false)
    private AccountStatus accountStatus;

    @NotNull(message = "Account balance is mandatory")
    @DecimalMin(value = "0.00", message = "Balance must be positive")
    @Column(columnDefinition = "decimal(15,2) default '0.0'", nullable = false)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Currency code is mandatory")
    @Column(name = "currency_code", columnDefinition = "varchar(50)", nullable = false)
    private CurrencyCode currencyCode;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime creationTime;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime lastModifiedTime;

    @ManyToOne(targetEntity = Client.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = true)
    @JoinColumn (name = "client_id", referencedColumnName = "id", nullable = false)
    @JsonIdentityReference(alwaysAsId = true)
    private Client client;
}
