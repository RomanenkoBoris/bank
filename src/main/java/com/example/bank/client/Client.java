package com.example.bank.client;

import com.example.bank.manager.Manager;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @NotBlank (message = "Client status is mandatory") // можно ли задать дефолтное значение?
    @Column(name = "client_status", columnDefinition = "VARCHAR(20)", nullable = false)
    private ClientStatus clientStatus;

    @Pattern(regexp = "[0-9]{11}", message = "Tax code must be always 11 numbers")
    @NotBlank (message = "Client tax code is mandatory")
    @Column(name = "tax_code", columnDefinition = "BIGINT(11)", nullable = false, unique = true)
    private long taxCode;

    @Length(min = 1, max = 50, message = "Name must be from 1 to 50 characters")
    @NotBlank (message = "Client name is mandatory")
    @Pattern(regexp = "[A-Z][a-z]*", message = "Name should start with a capital letter")
    @Column (name = "first_name", columnDefinition = "varchar(50)", nullable = false)
    private String firstName;

    @Length(min = 1, max = 50, message = "Surname must be from 1 to 50 characters")
    @NotBlank (message = "Client surname is mandatory")
    @Pattern(regexp = "[A-Z][a-z]*", message = "Surname should start with a capital letter")
    @Column (name = "last_name", columnDefinition = "varchar(50)", nullable = false)
    private String lastName;

    @Length(min = 6, max = 60, message = "Email must be from 6 to 60 characters")
    @NotBlank (message = "Client email is mandatory")
    @Email(message = "Email must be valid")
    @Column (columnDefinition = "varchar(60)", nullable = false)
    private String email;

    @Length(min = 10, max = 60, message = "Address must be from 10 to 80 characters")
    @NotBlank (message = "Client address is mandatory")
    @Column (columnDefinition = "varchar(80)", nullable = false)
    private String address;

    @Length(min = 5, max = 20, message = "Phone must be from 5 to 20 characters")
    @NotBlank (message = "Client phone is mandatory")
    @Pattern(regexp = "[0-9\\+\\-\\s][a-z]*", message = "The phone number should contain only digits, the symbols '+/-', or spaces")
    @Column (columnDefinition = "varchar(20)", nullable = false)
    private String phone;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime creationTime;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime lastModifiedTime;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // почему немогу задать mappedBy?
    @JoinColumn (name = "manager_id", referencedColumnName = "id", nullable = false)
    private Manager manager;

}
