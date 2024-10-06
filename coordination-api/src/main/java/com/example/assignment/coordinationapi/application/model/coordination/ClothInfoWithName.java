package com.example.assignment.coordinationapi.application.model.coordination;

import com.example.assignment.common.model.enums.ClothType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ClothInfoWithName {
    private ClothType category;
    private String brandName;
    private String clothName;
    private BigDecimal price;
}
