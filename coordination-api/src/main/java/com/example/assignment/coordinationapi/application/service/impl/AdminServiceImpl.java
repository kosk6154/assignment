package com.example.assignment.coordinationapi.application.service.impl;

import com.example.assignment.common.model.entity.Brand;
import com.example.assignment.common.model.entity.ClothCategory;
import com.example.assignment.common.model.entity.SaleCloth;
import com.example.assignment.common.model.enums.ClothType;
import com.example.assignment.common.model.exception.CoordinationException;
import com.example.assignment.common.model.exception.ExceptionMessage;
import com.example.assignment.coordinationapi.application.cache.ClothPriceCacheProvider;
import com.example.assignment.coordinationapi.application.repo.BrandRepo;
import com.example.assignment.coordinationapi.application.repo.ClothCategoryRepo;
import com.example.assignment.coordinationapi.application.repo.SaleClothRepo;
import com.example.assignment.coordinationapi.application.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * 브랜드나 상품을 추가, 수정, 삭제 시 사용된다.
 * @author kokyeomjae
 * @version 0.0.1
 */
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final ClothPriceCacheProvider clothPriceCacheProvider;
    private final BrandRepo brandRepo;
    private final ClothCategoryRepo clothCategoryRepo;
    private final SaleClothRepo saleClothRepo;


    @Override
    @Transactional
    public Brand addBrand(String brandName) {
        if (this.brandRepo.isExist(brandName)) {
            throw new CoordinationException(ExceptionMessage.ALREADY_EXIST_DATA);
        } else {
            Brand brand = this.brandRepo.save(new Brand(brandName));
            clothPriceCacheProvider.insertBrandCache(brand);

            return brand;
        }
    }

    @Override
    @Transactional
    public Brand modifyBrand(String oldBrandName, String newBrandName) {
        if (this.brandRepo.isExist(oldBrandName)) {
            Brand brand = this.brandRepo.getBrand(oldBrandName);
            brand.setName(newBrandName);
            Brand saved = this.brandRepo.save(brand);

            this.clothPriceCacheProvider.updateBrandName(brand, newBrandName);

            return saved;
        } else {
            throw new CoordinationException(ExceptionMessage.NOT_EXIST_BRAND);
        }
    }

    @Override
    @Transactional
    public void removeBrand(String brandName) {
        Brand brand = this.brandRepo.getBrand(brandName);

        if (brand == null) {
            throw new CoordinationException(ExceptionMessage.NOT_EXIST_BRAND);
        } else if (this.saleClothRepo.isExist(brand.getId())) {
            throw new CoordinationException(ExceptionMessage.SALE_CLOTH_EXIST);
        } else {
            this.brandRepo.removeBrand(brandName);
            this.clothPriceCacheProvider.removeBrand(brand.getId());
        }
    }

    @Override
    @Transactional
    public SaleCloth addCloth(String brandName, ClothType clothType, String clothName, BigDecimal price) {
        Brand brand = this.brandRepo.getBrand(brandName);

        if (brand == null) {
            throw new CoordinationException(ExceptionMessage.NOT_EXIST_BRAND);
        } else if (this.saleClothRepo.isExist(brand.getId(), clothType, clothName)) {
            throw new CoordinationException(ExceptionMessage.ALREADY_EXIST_DATA);
        } else {
            ClothCategory category = this.clothCategoryRepo.getClothCategory(clothType);

            SaleCloth saleCloth = SaleCloth.ofDefault(brand, category, price, clothName);
            SaleCloth incremented = this.saleClothRepo.save(saleCloth);

            this.clothPriceCacheProvider.insertCloth(incremented);

            return incremented;
        }
    }

    /**
     * 가격의 경우 캐싱되어 서버에 있다.
     * 운영툴에서 가격 수정이 매우 빈번하게 수정되는 그런 일은 거의 없을듯 하긴 하지만
     * 가장 최신의 가격정보를 항상 가져와서 캐시에 넣기 위해 격리수준을 조절한다.
     * @param brandName
     * @param clothType
     * @param clothName
     * @param price
     * @return
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public SaleCloth modifyClothPrice(String brandName, ClothType clothType, String clothName, BigDecimal price) {
        Brand brand = this.brandRepo.getBrand(brandName);

        if (brand == null) {
            throw new CoordinationException(ExceptionMessage.NOT_EXIST_BRAND);
        } else {
            SaleCloth saleCloth = this.saleClothRepo.getSaleCloth(brand.getId(), clothType, clothName);

            if (saleCloth == null) {
                throw new CoordinationException(ExceptionMessage.NOT_EXIST_DATA);
            } else {
                BigDecimal oldPrice = saleCloth.getPrice();

                saleCloth.setPrice(price);

                SaleCloth saved = this.saleClothRepo.saveAndFlush(saleCloth);
                this.clothPriceCacheProvider.updatePrice(saved, oldPrice, price);

                return saved;
            }
        }
    }

    @Override
    @Transactional
    public SaleCloth modifyClothName(String brandName, ClothType clothType, String oldClothName, String newClothName) {
        Brand brand = this.brandRepo.getBrand(brandName);

        if (brand == null) {
            throw new CoordinationException(ExceptionMessage.NOT_EXIST_BRAND);
        } else if (this.saleClothRepo.isExist(brand.getId(), clothType, newClothName)) {
            throw new CoordinationException(ExceptionMessage.ALREADY_EXIST_DATA);
        } else {
            SaleCloth saleCloth = this.saleClothRepo.getSaleCloth(brand.getId(), clothType, oldClothName);

            if (saleCloth == null) {
                throw new CoordinationException(ExceptionMessage.NOT_EXIST_DATA);
            } else {
                saleCloth.setClothName(newClothName);

                SaleCloth saved = this.saleClothRepo.save(saleCloth);
                this.clothPriceCacheProvider.updateClothName(saved);

                return saved;
            }
        }
    }

    @Override
    @Transactional
    public void removeCloth(String brandName, ClothType clothType, String clothName) {
        Brand brand = this.brandRepo.getBrand(brandName);

        if (brand == null) {
            throw new CoordinationException(ExceptionMessage.NOT_EXIST_BRAND);
        } else {
            SaleCloth saleCloth = this.saleClothRepo.getSaleCloth(brand.getId(), clothType, clothName);

            if (saleCloth == null) {
                throw new CoordinationException(ExceptionMessage.NOT_EXIST_DATA);
            } else {
                this.saleClothRepo.delete(saleCloth);
                this.clothPriceCacheProvider.deleteCloth(saleCloth);
            }
        }
    }
}
