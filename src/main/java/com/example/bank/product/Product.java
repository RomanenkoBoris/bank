package com.example.bank.product;

import com.example.bank.account.AccountStatus;
import com.example.bank.account.CurrencyCode;
import com.example.bank.manager.Manager;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "Product name is mandatory")
    @Length(min = 4, max = 70, message = "Product name must be from 4 to 70 characters")
    @Column(columnDefinition = "varchar(70)", nullable = false)
    String name;

    @Enumerated(EnumType.STRING)
    @NotBlank(message = "Product status is mandatory")
    @Column(name = "product_status", columnDefinition = "varchar(50)", nullable = false)
    private ProductStatus productStatus;

    @Enumerated(EnumType.STRING)
    @NotBlank(message = "Account status is mandatory")
    @Column(name = "currency_code", columnDefinition = "varchar(50)", nullable = false)
    private CurrencyCode currencyCode;

    @NotBlank(message = "Interest rate is mandatory")
    @Column(name = "interest_rate", columnDefinition = "decimal(6,4) default '0.0'", nullable = false)
    Double interestRate;

    @NotBlank(message = "Product limit is mandatory")
    @Column(name = "product_limit", columnDefinition = "integer", nullable = false)
    Integer productLimit;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime creationTime;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime lastModifiedTime;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "manager_id", referencedColumnName = "id", nullable = false)
    Manager manager;

}
