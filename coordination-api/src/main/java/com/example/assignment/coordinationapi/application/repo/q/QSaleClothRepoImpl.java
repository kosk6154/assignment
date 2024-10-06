package com.example.assignment.coordinationapi.application.repo.q;

import com.example.assignment.common.model.entity.SaleCloth;
import com.example.assignment.common.model.enums.ClothType;
import com.example.assignment.coordinationapi.application.model.coordination.ClothInfo;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.example.assignment.common.model.entity.QBrand.brand;
import static com.example.assignment.common.model.entity.QClothCategory.clothCategory;
import static com.example.assignment.common.model.entity.QSaleCloth.saleCloth;

@RequiredArgsConstructor
public class QSaleClothRepoImpl implements QSaleClothRepo {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ClothInfo> getCheapestCategoryInBrand(Integer brandId, Collection<ClothType> clothTypes) {
        return this.getCheapestCategoryInBrand(brandId, clothTypes, Collections.emptyList());
    }

    @Override
    public List<ClothInfo> getCheapestCategoryInBrand(Integer brandId, Collection<ClothType> clothTypes, Collection<BigInteger> excludeSaleClothIds) {
        return this.jpaQueryFactory.select(Projections.constructor(ClothInfo.class, clothCategory.category, saleCloth.brand.id, saleCloth.id,  saleCloth.price.min(), saleCloth.clothName))
                .from(saleCloth)
                .join(clothCategory)
                .on(saleCloth.category.code.eq(clothCategory.code))
                .fetchJoin()
                .where(
                        saleCloth.brand.id.eq(brandId)
                                .and(saleCloth.category.category.in(clothTypes))
                                .and(saleCloth.id.notIn(excludeSaleClothIds)))
                .groupBy(saleCloth.category)
                .fetch();
    }

    @Override
    public List<ClothInfo> getMostExpensiveCategoryInBrand(Integer brandId, Collection<ClothType> clothTypes, Collection<BigInteger> excludeSaleClothIds) {
        return this.jpaQueryFactory.select(Projections.constructor(ClothInfo.class, clothCategory.category, saleCloth.brand.id, saleCloth.id, saleCloth.price.max(), saleCloth.clothName))
                .from(saleCloth)
                .join(clothCategory)
                .on(saleCloth.category.code.eq(clothCategory.code))
                .fetchJoin()
                .where(
                        saleCloth.brand.id.eq(brandId)
                                .and(saleCloth.category.category.in(clothTypes))
                                .and(saleCloth.id.notIn(excludeSaleClothIds)))
                .groupBy(saleCloth.category)
                .fetch();
    }

    @Override
    public ClothInfo getCheapestCloth(ClothType clothType) {
        return this.getCheapestCloth(clothType, Collections.emptyList());
    }

    @Override
    public ClothInfo getCheapestCloth(ClothType clothType, Collection<BigInteger> excludeSaleClothIds) {
        NumberPath<BigDecimal> aliasPrice = Expressions.numberPath(BigDecimal.class, "price");

        return this.jpaQueryFactory.select(Projections.constructor(ClothInfo.class, clothCategory.category, saleCloth.brand.id, saleCloth.id, saleCloth.price.min().as(aliasPrice), saleCloth.clothName))
                .from(saleCloth)
                .join(clothCategory)
                .on(saleCloth.category.code.eq(clothCategory.code))
                .fetchJoin()
                .where(
                        saleCloth.category.category.eq(clothType)
                                .and(saleCloth.id.notIn(excludeSaleClothIds)))
                .groupBy(saleCloth.category.code, saleCloth.brand.id)
                .orderBy(aliasPrice.asc())
                .limit(1)
                .fetchFirst();
    }

    @Override
    public ClothInfo getMostExpensiveCloth(ClothType clothType) {
        return this.getMostExpensiveCloth(clothType, Collections.emptyList());
    }

    @Override
    public ClothInfo getMostExpensiveCloth(ClothType clothType, Collection<BigInteger> excludeSaleClothIds) {
        NumberPath<BigDecimal> aliasPrice = Expressions.numberPath(BigDecimal.class, "price");

        return this.jpaQueryFactory.select(Projections.constructor(ClothInfo.class, clothCategory.category, saleCloth.brand.id, saleCloth.id,  saleCloth.price.max().as(aliasPrice), saleCloth.clothName))
                .from(saleCloth)
                .join(clothCategory)
                .on(saleCloth.category.code.eq(clothCategory.code))
                .fetchJoin()
                .where(
                        saleCloth.category.category.eq(clothType)
                                .and(saleCloth.id.notIn(excludeSaleClothIds)))
                .groupBy(saleCloth.category.code, saleCloth.brand.id)
                .orderBy(aliasPrice.desc())
                .limit(1)
                .fetchFirst();
    }

    @Override
    public SaleCloth getSaleCloth(Integer brandId, ClothType clothType, String clothName) {
        return this.jpaQueryFactory
                .selectFrom(saleCloth)
                .where(
                        saleCloth.brand.id.eq(brandId)
                                .and(saleCloth.category.category.eq(clothType)
                                        .and(saleCloth.clothName.eq(clothName))))
                .fetchFirst();
    }

    @Override
    public boolean isExist(Integer brandId, ClothType clothType, String clothName) {
        Integer one = this.jpaQueryFactory
                .selectOne()
                .from(saleCloth)
                .where(
                        saleCloth.brand.id.eq(brandId)
                                .and(saleCloth.category.category.eq(clothType)
                                        .and(saleCloth.clothName.eq(clothName))))
                .fetchFirst();

        return one != null;
    }

    @Override
    public boolean isExist(Integer brandId) {
        Integer one = this.jpaQueryFactory
                .selectOne()
                .from(saleCloth)
                .where(
                        saleCloth.brand.id.eq(brandId))
                .fetchFirst();

        return one != null;
    }

    @Override
    public long modifySaleCloth(Integer brandId, ClothType clothType, String clothName, BigDecimal price) {
        return this.jpaQueryFactory
                .update(saleCloth)
                .set(saleCloth.price, price)
                .set(saleCloth.clothName, clothName)
                .where(
                        saleCloth.brand.id.eq(brandId)
                                .and(saleCloth.category.category.eq(clothType)
                                        .and(saleCloth.clothName.eq(clothName))))
                .execute();
    }

    @Override
    public long removeSaleCloth(Integer brandId, ClothType clothType, String clothName) {
        return this.jpaQueryFactory
                .delete(saleCloth)
                .where(
                        saleCloth.brand.id.eq(brandId)
                                .and(saleCloth.category.category.eq(clothType)
                                        .and(saleCloth.clothName.eq(clothName))))
                .execute();
    }
}
