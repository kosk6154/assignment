package com.example.assignment.coordinationapi.application.service;

import com.example.assignment.common.model.enums.ClothType;
import com.example.assignment.coordinationapi.application.model.coordination.CheapestBrand;
import com.example.assignment.coordinationapi.application.model.coordination.ClothInfoWithName;
import com.example.assignment.coordinationapi.application.model.coordination.MinMaxPriceCategory;

import java.util.Collection;

public interface SaleClothService {
    CheapestBrand getCheapestBrand(Collection<ClothType> targetClothType);
    ClothInfoWithName getCheapestCloth(ClothType clothType);

    MinMaxPriceCategory getMinMaxCloth(ClothType clothType);
}
