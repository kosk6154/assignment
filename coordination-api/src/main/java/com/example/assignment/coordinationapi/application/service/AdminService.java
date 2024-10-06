package com.example.assignment.coordinationapi.application.service;

import com.example.assignment.common.model.entity.Brand;
import com.example.assignment.common.model.entity.SaleCloth;
import com.example.assignment.common.model.enums.ClothType;

import java.math.BigDecimal;

public interface AdminService {

    Brand addBrand(String brandName);
    Brand modifyBrand(String oldBrandName, String newBrandName);
    void removeBrand(String brandName);

    SaleCloth addCloth(String brandName, ClothType clothType, String clothName, BigDecimal price);
    SaleCloth modifyClothPrice(String brandName, ClothType clothType, String clothName, BigDecimal price);
    SaleCloth modifyClothName(String brandName, ClothType clothType, String oldClothName, String newClothName);
    void removeCloth(String brandName, ClothType clothType, String clothName);
}
