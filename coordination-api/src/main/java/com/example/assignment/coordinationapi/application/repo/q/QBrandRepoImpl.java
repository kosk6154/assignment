package com.example.assignment.coordinationapi.application.repo.q;

import com.example.assignment.common.model.entity.Brand;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.example.assignment.common.model.entity.QBrand.brand;

@RequiredArgsConstructor
public class QBrandRepoImpl implements QBrandRepo {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean isExist(String brandName) {
        Integer one = this.jpaQueryFactory
                .selectOne()
                .from(brand)
                .where(brand.name.eq(brandName))
                .fetchFirst();

        return one != null;
    }


    @Override
    public long removeBrand(String brandName) {
        return this.jpaQueryFactory
            .delete(brand)
            .where(brand.name.eq(brandName))
            .execute();
    }

    @Override
    public Brand getBrand(String brandName) {
        return this.jpaQueryFactory
                .selectFrom(brand)
                .where(brand.name.eq(brandName))
                .fetchFirst();
    }
}
