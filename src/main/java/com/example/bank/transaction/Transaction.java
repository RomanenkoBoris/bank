package com.example.bank.transaction;

import com.example.bank.account.Account;
import com.example.bank.account.AccountType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    @NotBlank(message = "Transaction type is mandatory")
    @Column(name = "type", columnDefinition = "byte", nullable = false) // нужно ли делать кастомную аннотацию Between?
    private TransactionType transactionType;

    @NotBlank(message = "Transaction amount is mandatory") //?
    @Column(columnDefinition = "decimal(12,4)", nullable = false)
    private BigDecimal amount;

    @Column(columnDefinition = "varchar(255)")
    private String description;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime creationTime;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn (name = "debit_account_id", referencedColumnName = "id", unique = true, nullable = false)
    Account debitAccount;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn (name = "credit_account_id", referencedColumnName = "id", unique = true, nullable = false)
    Account creditAccount;
}
