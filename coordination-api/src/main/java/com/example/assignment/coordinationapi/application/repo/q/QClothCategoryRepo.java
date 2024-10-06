package com.example.assignment.coordinationapi.application.repo.q;

import com.example.assignment.common.model.entity.ClothCategory;
import com.example.assignment.common.model.enums.ClothType;

public interface QClothCategoryRepo {
    ClothCategory getClothCategory(ClothType clothType);
}
