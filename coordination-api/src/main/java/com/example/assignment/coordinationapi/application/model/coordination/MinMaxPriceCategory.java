package com.example.assignment.coordinationapi.application.model.coordination;

import com.example.assignment.common.model.enums.ClothType;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class MinMaxPriceCategory {
    private final ClothType category;
    private final BrandPrice min;
    private final BrandPrice max;

    public record BrandPrice(String brand, BigDecimal price) {
    }

    public MinMaxPriceCategory(ClothInfo min, String minBrandName, ClothInfo max, String maxBrandName) {
        this.category = min.getCategory();

        this.min = new BrandPrice(minBrandName, min.getPrice());
        this.max = new BrandPrice(maxBrandName, max.getPrice());
    }
}
