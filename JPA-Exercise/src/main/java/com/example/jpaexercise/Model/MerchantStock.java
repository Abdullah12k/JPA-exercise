package com.example.jpaexercise.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MerchantStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "product id can not be empty")
    @Column(columnDefinition = "int not null")
    private Integer productId;
    @NotNull(message = "merchant id can not be empty")
    @Column(columnDefinition = "int not null")
    private Integer merchantId;
    @NotNull(message = "stock can not be empty")
    @Min(10)
    @Column(columnDefinition = "int not null")
    private Integer stock;
}
