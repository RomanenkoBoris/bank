package com.example.bank.agreement;


import com.example.bank.account.Account;
import com.example.bank.product.Product;
import com.example.bank.product.ProductStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "agreements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Agreement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "Interest rate is mandatory")
    @Column(name = "interest_rate", columnDefinition = "decimal(6,4) default '0.0'", nullable = false)
    Double interestRate;

    @Enumerated(EnumType.STRING)
    @NotBlank(message = "Agreement status is mandatory")
    @Column(name = "agreement_status", columnDefinition = "varchar(50)", nullable = false)
    private ProductStatus agreementStatus;

    @NotBlank(message = "Agreement sum is mandatory")
    @Column(columnDefinition = "decimal(15,2)", nullable = false)
    private BigDecimal sum;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime creationTime;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime lastModifiedTime;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    Product product;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn (name = "account_id", referencedColumnName = "id", nullable = false)
    Account account;

}
