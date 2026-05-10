package com.example.jpaexercise.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "name can not be empty")
    @Size(min = 3, message = "name must at least be 3 letters")
    @Column(columnDefinition = "varchar(15) not null")
    private String name;

    @NotNull(message = "price an not be empty")
    @Positive(message = "price must be positive number")
    @Column(columnDefinition = "int not null")
    private Integer price;

    @NotNull(message = "category id can not be empty")
    @Column(columnDefinition = "int not null")
    private Integer categoryId;
    // extra
    @Min(0)
    @Column(columnDefinition = "int not null default 0")
    private Integer salesCount = 0;
}
