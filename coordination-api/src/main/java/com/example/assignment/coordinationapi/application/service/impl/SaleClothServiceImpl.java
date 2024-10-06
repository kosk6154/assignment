package com.example.assignment.coordinationapi.application.service.impl;

import com.example.assignment.common.model.entity.Brand;
import com.example.assignment.common.model.enums.ClothType;
import com.example.assignment.common.model.exception.CoordinationException;
import com.example.assignment.common.model.exception.ExceptionMessage;
import com.example.assignment.coordinationapi.application.cache.ClothPriceCacheProvider;
import com.example.assignment.coordinationapi.application.model.coordination.BrandIdSum;
import com.example.assignment.coordinationapi.application.model.coordination.CheapestBrand;
import com.example.assignment.coordinationapi.application.model.coordination.ClothInfo;
import com.example.assignment.coordinationapi.application.model.coordination.MinMaxPriceCategory;
import com.example.assignment.coordinationapi.application.repo.BrandRepo;
import com.example.assignment.coordinationapi.application.repo.SaleClothRepo;
import com.example.assignment.coordinationapi.application.service.SaleClothService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleClothServiceImpl implements SaleClothService {

    private final ClothPriceCacheProvider clothPriceCacheProvider;
    private final SaleClothRepo saleClothRepo;
    private final BrandRepo brandRepo;

    @Override
    public CheapestBrand getCheapestBrand(Collection<ClothType> targetClothType) {
        CheapestBrand result = this.clothPriceCacheProvider.getCheapestBrand(targetClothType);

        if(result == null) {
            BrandIdSum brandIdSum = this.saleClothRepo.getCheapestBrandSum();
            List<ClothInfo> clothInfoList = this.saleClothRepo.getCheapestCategoryInBrand(brandIdSum.id(), targetClothType);

            List<CheapestBrand.CategoryPrice> categoryPriceList = clothInfoList.stream()
                    .map(CheapestBrand.CategoryPrice::of)
                    .toList();

            Brand brand = this.brandRepo.findById(brandIdSum.id())
                    .orElseThrow(() -> new CoordinationException(ExceptionMessage.NOT_EXIST_BRAND));

            return new CheapestBrand(brand.getName(), categoryPriceList, brandIdSum.sum());
        }

        return result;
    }

    @Override
    public ClothInfo getCheapestCloth(ClothType clothType) {
        ClothInfo clothInfo = this.clothPriceCacheProvider.getCheapestCloth(clothType);

        if(clothInfo == null) {
            return this.saleClothRepo.getCheapestCloth(clothType);
        }

        return clothInfo;
    }

    @Override
    public MinMaxPriceCategory getMinMaxCloth(ClothType clothType) {
        ClothInfo min = this.clothPriceCacheProvider.getCheapestCloth(clothType);
        ClothInfo max = this.clothPriceCacheProvider.getMostExpensiveCloth(clothType);

        if(min == null || max == null) {
            min = this.saleClothRepo.getCheapestCloth(clothType);
            max = this.saleClothRepo.getMostExpensiveCloth(clothType);
        }

        if(min == null || max == null) {
            throw new CoordinationException(ExceptionMessage.NOT_EXIST_DATA);
        }

        String minBrandName = this.clothPriceCacheProvider.getBrandName(min.getBrandId());
        String maxBrandName = this.clothPriceCacheProvider.getBrandName(max.getBrandId());

        return new MinMaxPriceCategory(min, minBrandName, max, maxBrandName);
    }
}
