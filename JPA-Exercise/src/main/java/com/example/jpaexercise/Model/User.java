package com.example.jpaexercise.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "username can not be empty")
    @Column(columnDefinition = "varchar(25) not null")
    private String username;

    @NotEmpty(message = "password can not be empty")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,16}$",
            message = "Password must be 8-16 characters and include letters and numbers")
    @Column(columnDefinition = "varchar(16) not null")
    private String password;

    @NotEmpty(message = "email can not be empty")
    @Email
    @Column(columnDefinition = "varchar(50) not null")
    private String email;

    @NotEmpty(message = "role can not be empty")
    @Pattern(regexp = "(?i)^(ADMIN|CUSTOMER)$", message = "role must be either Admin or Customer")
    @Column(columnDefinition = "varchar(25) not null")
    @Check(constraints = "role in ('ADMIN','CUSTOMER')")
    private String role;
    @NotNull(message = "balance can not be empty")
    private double balance;
}
