package com.example.bank.manager;

import com.example.bank.client.ClientStatus;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "managers")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(min = 1, max = 50, message = "Name must be from 1 to 50 characters")
    @NotBlank(message = "Manager name is mandatory")
    @Pattern(regexp = "[A-Z][a-z]*", message = "Name should start with a capital letter")
    @Column (name = "first_name", columnDefinition = "varchar(50)", nullable = false)
    private String firstName;

    @Length(min = 1, max = 50, message = "Surname must be from 1 to 50 characters")
    @NotBlank (message = "Manager surname is mandatory")
    @Pattern(regexp = "[A-Z][a-z]*", message = "Surname should start with a capital letter")
    @Column (name = "last_name", columnDefinition = "varchar(50)", nullable = false)
    private String lastName;

    @Length(min = 1, max = 20, message = "Login must be from 1 to 20 characters")
    @NotBlank (message = "Login is mandatory")
    @Column (columnDefinition = "varchar(20)", nullable = false, unique = true)
    private String login;

    @Length(min = 1, max = 20, message = "Password must be from 1 to 20 characters")
    @NotBlank (message = "Password is mandatory")
    @Column (columnDefinition = "varchar(100)", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Manager status is mandatory")
    @Column(name = "manager_status", columnDefinition = "VARCHAR(50)", nullable = false)
    private ManagerStatus managerStatus;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime creationTime;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime lastModifiedTime;

}
