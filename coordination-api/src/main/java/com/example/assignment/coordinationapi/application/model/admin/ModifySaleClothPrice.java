package com.example.assignment.coordinationapi.application.model.admin;

import com.example.assignment.common.model.enums.ClothType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class ModifySaleClothPrice {

    @NotBlank
    @Length(max = 20)
    private String brandName;

    @NotNull
    private ClothType clothType;

    @NotBlank
    @Length(max = 30)
    private String clothName;

    @NotNull
    @DecimalMin("0")
    private BigDecimal price;
}
