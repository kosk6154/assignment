package com.example.assignment.coordinationapi.application.cache;

import com.example.assignment.common.model.entity.Brand;
import com.example.assignment.common.model.entity.SaleCloth;
import com.example.assignment.common.model.enums.ClothType;
import com.example.assignment.coordinationapi.application.model.coordination.CheapestBrand;
import com.example.assignment.coordinationapi.application.model.coordination.ClothInfo;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 * 상품 가격을 캐시하고, 해당 정보를 제공하기 위한 interface
 * 해당 interface를 구현하여 다른 캐시 저장소에 질의하여 붙일수도 있다.
 */
public interface ClothPriceCacheProvider {
    void insertCloth(List<SaleCloth> saleClothList);

    void insertCloth(SaleCloth saleCloth);
    void deleteCloth(SaleCloth saleCloth);

    void updateClothName(SaleCloth saleCloth);
    void updatePrice(SaleCloth saleCloth, BigDecimal originalPrice, BigDecimal newPrice);
    void updateBrandName(Brand brand, String newBrandName);

    void insertBrandCache(Brand brand);
    void insertBrandCache(List<Brand> brandList);

    String getBrandName(Integer brandId);
    void removeBrand(Integer brandId);

    CheapestBrand getCheapestBrand(Collection<ClothType> clothTypes);
    ClothInfo getCheapestCloth(ClothType clothType);
    ClothInfo getMostExpensiveCloth(ClothType clothType);
}
