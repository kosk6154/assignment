package com.example.assignment.coordinationapi.application.model.coordination;

import com.example.assignment.common.model.entity.SaleCloth;
import com.example.assignment.common.model.enums.ClothType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClothInfo implements Comparable<ClothInfo> {
    private ClothType category; // do not update
    private Integer brandId; // do not update
    private BigInteger saleClothId; // do not update

    @Setter
    private BigDecimal price;

    @Setter
    private String clothName;

    public ClothInfo(ClothType category, Integer brandId, BigInteger saleClothId, BigDecimal price, String clothName) {
        this.category = category;
        this.brandId = brandId;
        this.saleClothId = saleClothId;
        this.price = price;
        this.clothName = clothName;
    }

    public ClothInfo(SaleCloth saleCloth) {
        this.brandId = saleCloth.getBrand().getId();
        this.category = saleCloth.getCategory().getCategory();
        this.saleClothId = saleCloth.getId();
        this.price = saleCloth.getPrice();
        this.clothName = saleCloth.getClothName();
    }

    /**
     * TreeMap에서 key는 comparator로 결정된다.
     * @param o
     * @return
     */
    @Override
    public int compareTo(ClothInfo o) {
        return this.price.compareTo(o.price);
    }

    /**
     * category, brand, 상품이 같으면 같다고 판단함
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClothInfo clothInfo = (ClothInfo) o;

        if (category != clothInfo.category) return false;
        if (!brandId.equals(clothInfo.brandId)) return false;
        return saleClothId.equals(clothInfo.saleClothId);
    }

    /**
     * category, brand, 상품이 같으면 같다고 판단함
     */
    @Override
    public int hashCode() {
        int result = category.hashCode();
        result = 31 * result + brandId.hashCode();
        result = 31 * result + saleClothId.hashCode();
        return result;
    }
}
