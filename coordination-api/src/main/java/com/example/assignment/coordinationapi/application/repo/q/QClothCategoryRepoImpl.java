package com.example.assignment.coordinationapi.application.repo.q;

import com.example.assignment.common.model.entity.ClothCategory;
import com.example.assignment.common.model.enums.ClothType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.example.assignment.common.model.entity.QClothCategory.clothCategory;

@RequiredArgsConstructor
public class QClothCategoryRepoImpl implements QClothCategoryRepo {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public ClothCategory getClothCategory(ClothType clothType) {
        return this.jpaQueryFactory
                .selectFrom(clothCategory)
                .where(clothCategory.category.eq(clothType))
                .fetchFirst();
    }
}
