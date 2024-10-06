package com.example.assignment.coordinationapi.application.repo.q;

import com.example.assignment.common.model.entity.Brand;

public interface QBrandRepo {
    boolean isExist(String brandName);
    long removeBrand(String brandName);

    Brand getBrand(String brandName);
}
