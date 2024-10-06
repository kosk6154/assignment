package com.example.assignment.coordinationapi.application.repo.q;

import com.example.assignment.common.model.entity.SaleCloth;
import com.example.assignment.common.model.enums.ClothType;
import com.example.assignment.coordinationapi.application.model.coordination.ClothInfo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

public interface QSaleClothRepo {
    List<ClothInfo> getCheapestCategoryInBrand(Integer brandId, Collection<ClothType> clothTypes);
    List<ClothInfo> getCheapestCategoryInBrand(Integer brandId, Collection<ClothType> clothTypes, Collection<BigInteger> excludeSaleClothIds);
    List<ClothInfo> getMostExpensiveCategoryInBrand(Integer brandId, Collection<ClothType> clothTypes, Collection<BigInteger> excludeSaleClothIds);
    ClothInfo getCheapestCloth(ClothType clothType);
    ClothInfo getCheapestCloth(ClothType clothType, Collection<BigInteger> excludeSaleClothIds);
    ClothInfo getMostExpensiveCloth(ClothType clothType);
    ClothInfo getMostExpensiveCloth(ClothType clothType, Collection<BigInteger> excludeSaleClothIds);

    SaleCloth getSaleCloth(Integer brandId, ClothType clothType, String clothName);

    boolean isExist(Integer brandId, ClothType clothType, String clothName);
    boolean isExist(Integer brandId);
    long modifySaleCloth(Integer brandId, ClothType clothType, String clothName, BigDecimal price);
    long removeSaleCloth(Integer brandId, ClothType clothType, String clothName);
}
