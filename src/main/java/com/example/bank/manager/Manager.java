package com.example.bank.manager;

import com.example.bank.client.ClientStatus;
import jakarta.persistence.*;
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
@Table(name = "managers")
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

    @Enumerated(EnumType.STRING)
    @NotBlank (message = "Manager status is mandatory")
    @Column(name = "manager_status", columnDefinition = "VARCHAR(50)", nullable = false)
    private ManagerStatus managerStatus;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime creationTime;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime lastModifiedTime;
}
