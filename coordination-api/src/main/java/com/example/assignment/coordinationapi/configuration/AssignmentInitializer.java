package com.example.assignment.coordinationapi.configuration;

import com.example.assignment.common.model.entity.Brand;
import com.example.assignment.common.model.entity.ClothCategory;
import com.example.assignment.common.model.entity.SaleCloth;
import com.example.assignment.coordinationapi.application.cache.ClothPriceCacheProvider;
import com.example.assignment.coordinationapi.application.repo.BrandRepo;
import com.example.assignment.coordinationapi.application.repo.ClothCategoryRepo;
import com.example.assignment.coordinationapi.application.repo.SaleClothRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class AssignmentInitializer {

    private final BrandRepo brandRepo;
    private final ClothCategoryRepo clothCategoryRepo;
    private final SaleClothRepo saleClothRepo;

    private final ClothPriceCacheProvider clothPriceCacheProvider;

    private static boolean isInitialized = false;

    /**
     * ApplicationReadyEvent는 경우에 따라 여러번 발생할 수 있다.
     * 최초 한번만 init 하게끔 flag로 제어하고 synchronized 처리한다.
     */
    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public synchronized void initializeData() {
        if (!isInitialized) {
            List<Brand> allBrandList = this.brandRepo.saveAll(AssignmentData.BRAND_DATA.values());
            this.clothCategoryRepo.saveAll(AssignmentData.CATEGORY_DATA.values());

            // 상의, 아우터, 바지, 스니커즈, 가방, 모자, 양말, 액세서리 총 8개 * 브랜드 개수
            // 사전에 개수를 어느정도 예상할 수 있는경우 collection 생성자에 크기를 초기화 하여 넣어주는게 성능에 유리하다.
            List<SaleCloth> saleClothList = new ArrayList<>(AssignmentData.BRAND_DATA.size() * 8);

            for (String brandStr : AssignmentData.SALES_DATA.keySet()) {
                List<SaleCloth> list = AssignmentData.SALES_DATA.get(brandStr)
                        .stream()
                        .map((pair) -> {
                            Brand brand = AssignmentData.BRAND_DATA.get(brandStr);
                            ClothCategory category = AssignmentData.CATEGORY_DATA.get(pair.getLeft());
                            BigDecimal price = pair.getRight();
                            String clothName = pair.getLeft().getCode().toUpperCase(Locale.ROOT);

                            return SaleCloth.ofDefault(brand, category, price, clothName);
                        })
                        .toList();

                saleClothList.addAll(list);
            }

            List<SaleCloth> incrementedList = this.saleClothRepo.saveAll(saleClothList);
            this.clothPriceCacheProvider.insertBrandCache(allBrandList);
            this.clothPriceCacheProvider.insertCloth(incrementedList);

            isInitialized = true;
        }
    }


}
