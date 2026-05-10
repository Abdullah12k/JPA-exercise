package com.example.jpaexercise.Model;

import jakarta.persistence.*;
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
public class PurchaseHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "user id id can not be null")
    @Column(columnDefinition = "int not null")
    private Integer userId;

    @NotNull(message = "product id can not be null")
    @Column(columnDefinition = "int not null")
    private Integer productId;

    @NotNull(message = "merchant id can not be null")
    @Column(columnDefinition = "int not null")
    private Integer merchantId;

    @NotNull(message = "price can not be null")
    @Column(columnDefinition = "int not null")
    private double pricePaid;

    @NotEmpty(message = "status can not be empty")
    @Pattern(regexp = "^(COMPLETED|REFUNDED|EXCHANGED)$", message = "status must be COMPLETED, REFUNDED, or EXCHANGED")
    @Column(columnDefinition = "varchar(15) default 'COMPLETED'")
    @Check(constraints = "status in ('COMPLETED', 'REFUNDED', 'EXCHANGED')")
    private String status = "COMPLETED";
}
