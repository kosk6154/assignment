package com.example.assignment.coordinationapi.application.model.coordination;

import java.math.BigDecimal;
import java.util.List;

public record CheapestBrand(String brand,
                            List<CategoryPrice> categoryPriceList,
                            BigDecimal totalPrice) {

    public record CategoryPrice(String category, BigDecimal price) {
        public static CategoryPrice of(ClothInfo clothInfo) {
            return new CategoryPrice(clothInfo.getCategory().getCode(), clothInfo.getPrice());
        }
    }

}
