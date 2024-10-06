package com.example.assignment.coordinationapi.application.cache;

import com.example.assignment.common.model.entity.Brand;
import com.example.assignment.common.model.entity.SaleCloth;
import com.example.assignment.common.model.enums.ClothType;
import com.example.assignment.coordinationapi.application.model.coordination.CheapestBrand;
import com.example.assignment.coordinationapi.application.model.coordination.ClothInfo;
import com.example.assignment.coordinationapi.application.repo.SaleClothRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

/**
 * 메모리에 캐싱하여 상품 정보를 반환한다.
 * 요구사항은 H2 DB만 사용하게끔 되어있으므로
 * 다른 외부 캐시로 확장 가능하게끔 구현한다.
 *
 * insert나 update의 경우 동시 호출될 경우 캐시등이 꼬일 수 있으므로
 * synchronized를 붙인다.
 */

@Component
@RequiredArgsConstructor
public class InMemoryClothPriceCacheProvider implements ClothPriceCacheProvider {

    private final SaleClothRepo saleClothRepo;

    /*
     * 브랜드별 / 카테고리별로 모든 품목을 가격별로 정렬하여 유지한다.
     * 단 sorted map 특성 상 중복 가격에 대한 상품은 허용되지 않는다.
     */
    private final Map<Integer, HashMap<ClothType, SortedMap<ClothInfo, ClothInfo>>> brandSaleCache = new HashMap<>();

    /*
     * 카테고리별로 모든 품목을 가격별로 정렬하여 유지한다.
     * 마찬가지로 sorted map 특성 상 중복 가격에 대한 상품은 허용되지 않는다.
     */
    private final Map<ClothType, SortedMap<ClothInfo, ClothInfo>> categorySaleCache = new HashMap<>();

    // 브랜드의 이름을 캐싱한다.
    private final Map<Integer, String> brandNameCache = new HashMap<>();

    @Override
    public synchronized void insertCloth(List<SaleCloth> saleClothList) {
        for (SaleCloth saleCloth : saleClothList) {
            Integer brandId = saleCloth.getBrand().getId();
            ClothType category = saleCloth.getCategory().getCategory();

            ClothInfo clothInfo = new ClothInfo(saleCloth);

            this.setBrandCache(brandId, clothInfo);
            this.setCategoryCache(category, clothInfo);
        }
    }

    @Override
    public synchronized void insertCloth(SaleCloth saleCloth) {
        this.insertCloth(List.of(saleCloth));
    }

    @Override
    public void deleteCloth(SaleCloth saleCloth) {
        Integer brandId = saleCloth.getBrand().getId();
        ClothType category = saleCloth.getCategory().getCategory();

        ClothInfo clothInfo = new ClothInfo(saleCloth);

        this.brandSaleCache.get(brandId).get(category).remove(clothInfo);
        this.categorySaleCache.get(category).remove(clothInfo);
    }

    @Override
    public synchronized void updateClothName(SaleCloth saleCloth) {
        Integer brandId = saleCloth.getBrand().getId();
        ClothType category = saleCloth.getCategory().getCategory();

        ClothInfo clothInfo = new ClothInfo(saleCloth);
        brandSaleCache.get(brandId).get(category).remove(clothInfo);
        categorySaleCache.get(category).remove(clothInfo);

        brandSaleCache.get(brandId).get(category).put(clothInfo, clothInfo);
        categorySaleCache.get(category).put(clothInfo, clothInfo);
    }

    @Override
    public synchronized void updatePrice(SaleCloth saleCloth, BigDecimal originalPrice, BigDecimal newPrice) {
        Integer brandId = saleCloth.getBrand().getId();
        ClothType category = saleCloth.getCategory().getCategory();

        ClothInfo clothInfo = new ClothInfo(saleCloth);
        clothInfo.setPrice(originalPrice);

        SortedMap<ClothInfo, ClothInfo> cacheInBrand = this.brandSaleCache.get(brandId).get(category);

        ClothInfo minInBrand = cacheInBrand.firstKey();
        ClothInfo maxInBrand = cacheInBrand.lastKey();

        // 브랜드에서 캐시에 있는 최소가격의 상품의 가격을 올리는 경우.
        // treemap은 중복된 가격을 유지하지 않으므로 동일한 최소가격의 다른 상품이 캐시에 없을 가능성이 존재한다.
        // 아래 조건문들에 걸려 DB까지 조회되어 갱신되는 경우는 거의 낮다.
        if(newPrice.compareTo(minInBrand.getPrice()) <= 0
                || saleCloth.getId().equals(minInBrand.getSaleClothId())) {
            if(newPrice.compareTo(originalPrice) > 0) {
                List<ClothInfo> cheapestCategoryInBrand = this.saleClothRepo.getCheapestCategoryInBrand(brandId, List.of(category), List.of(saleCloth.getId()));

                for(ClothInfo cheapest : cheapestCategoryInBrand) {
                    if(cheapest.getPrice().compareTo(minInBrand.getPrice()) <= 0) {
                        brandSaleCache.get(brandId).get(category).put(cheapest, cheapest);
                    }
                }
            }
        }
        // 반대로 브랜드에서 캐시에 있는 최대가격의 상품 가격을 낮추는 경우
        // 마찬가지의 이유로 동일 최대 가격의 다른 상품이 캐시에 없을 가능성이 존재함
        else if(newPrice.compareTo(maxInBrand.getPrice()) >= 0
                || saleCloth.getId().equals(maxInBrand.getSaleClothId())) {
            if(newPrice.compareTo(originalPrice) < 0) {
                List<ClothInfo> expensiveCategoryInBrand = this.saleClothRepo.getMostExpensiveCategoryInBrand(brandId, List.of(category), List.of(saleCloth.getId()));

                for(ClothInfo expensive : expensiveCategoryInBrand) {
                    if(expensive.getPrice().compareTo(maxInBrand.getPrice()) >= 0) {
                        brandSaleCache.get(brandId).get(category).put(expensive, expensive);
                    }
                }
            }
        }

        ClothInfo min = this.getCheapestCloth(category);
        ClothInfo max = this.getMostExpensiveCloth(category);

        // 위의 조건문과 유사하며 전체 카테고리에서 가장 최하위 가격을 높게 조절하는 경우
        if(newPrice.compareTo(min.getPrice()) <= 0
                || saleCloth.getId().equals(min.getSaleClothId())) {
            if(newPrice.compareTo(originalPrice) > 0) {
                ClothInfo cheapest = this.saleClothRepo.getCheapestCloth(category, List.of(saleCloth.getId()));
                if(cheapest.getPrice().compareTo(min.getPrice()) <= 0) {
                    categorySaleCache.get(category).put(cheapest, cheapest);
                }
            }
        }
        else if(newPrice.compareTo(max.getPrice()) >= 0
                || saleCloth.getId().equals(max.getSaleClothId())) {
            if(newPrice.compareTo(originalPrice) < 0) {
                ClothInfo expensive = this.saleClothRepo.getMostExpensiveCloth(category, List.of(saleCloth.getId()));
                if(expensive.getPrice().compareTo(max.getPrice()) >= 0) {
                    categorySaleCache.get(category).put(expensive, expensive);
                }
            }
        }

        // price로만 비교하므로 값은 같으나 다른 상품이 지워질 수 있다.
        ClothInfo compareCacheInBrand = brandSaleCache.get(brandId).get(category).get(clothInfo);
        if(compareCacheInBrand.equals(clothInfo)) {
            brandSaleCache.get(brandId).get(category).remove(clothInfo);
        }

        // price로만 비교하므로 값은 같으나 다른 상품이 지워질 수 있다.
        ClothInfo compareCacheInCategory = categorySaleCache.get(category).get(clothInfo);
        if(compareCacheInCategory.equals(clothInfo)) {
            categorySaleCache.get(category).remove(clothInfo);
        }


        clothInfo.setPrice(newPrice);
        // 동일한 가격인 경우 key는 변하지 않고 value만 바뀌어 상품정보가 상이하게 될 수 있다.
        if(!brandSaleCache.get(brandId).get(category).containsKey(clothInfo)) {
            brandSaleCache.get(brandId).get(category).put(clothInfo, clothInfo);
        }

        // 동일한 가격인 경우 key는 변하지 않고 value만 바뀌어 상품정보가 상이하게 될 수 있다.
        if(!categorySaleCache.get(category).containsKey(clothInfo)) {
            categorySaleCache.get(category).put(clothInfo, clothInfo);
        }
    }

    @Override
    public void updateBrandName(Brand brand, String newBrandName) {
        brandNameCache.put(brand.getId(), newBrandName);
    }

    @Override
    public void insertBrandCache(Brand brand) {
        this.insertBrandCache(List.of(brand));
    }

    @Override
    public void insertBrandCache(List<Brand> brandList) {
        brandList.forEach((brand) -> brandNameCache.put(brand.getId(), brand.getName()));
    }

    @Override
    public String getBrandName(Integer brandId) {
        return this.brandNameCache.get(brandId);
    }

    @Override
    public void removeBrand(Integer brandId) {
        this.brandSaleCache.remove(brandId);
        this.brandNameCache.remove(brandId);

        this.categorySaleCache.keySet().forEach((clothType) -> {
            this.categorySaleCache.get(clothType)
                    .entrySet()
                    .removeIf((entry) ->
                            entry.getKey().getBrandId().equals(brandId));
        });
    }

    /**
     * 해당하는 카테고리의 모든 합이 가장 저렴한 가격의 브랜드를 반환한다.
     * 다만 특정 브랜드에서 해당하는 카테고리의 판매 상품이 없을 경우 해당 브랜드는 제외된다.
     * @param clothTypes
     * @return
     */
    @Override
    public CheapestBrand getCheapestBrand(Collection<ClothType> clothTypes) {
        String cheapestBrand = null;
        BigDecimal cheapestPrice = null;
        List<ClothInfo> cheapestClothInfoList = new ArrayList<>();

        for (Integer brandId : this.brandNameCache.keySet()) {
            boolean isInvalid = false;
            BigDecimal totalPrice = BigDecimal.ZERO;
            List<ClothInfo> clothInfoList = new ArrayList<>();

            for (ClothType clothType : clothTypes) {
                HashMap<ClothType, SortedMap<ClothInfo, ClothInfo>> clothTypeMap = brandSaleCache.get(brandId);

                if(clothTypeMap != null) {
                    SortedMap<ClothInfo, ClothInfo> cache = clothTypeMap.get(clothType);

                    if (cache != null) {
                        Map.Entry<ClothInfo, ClothInfo> entry = cache.firstEntry();

                        if (entry == null) {
                            isInvalid = true;
                            break;
                        }

                        totalPrice = totalPrice.add(entry.getValue().getPrice());
                        clothInfoList.add(entry.getKey());
                    } else {
                        isInvalid = true;
                        break;
                    }
                }
                else {
                    isInvalid = true;
                    break;
                }
            }

            if(!isInvalid) {
                if (cheapestPrice == null || totalPrice.compareTo(cheapestPrice) < 0) {
                    cheapestBrand = this.brandNameCache.get(brandId);
                    cheapestPrice = totalPrice;
                    cheapestClothInfoList = clothInfoList;
                }
            }
        }

        if(cheapestBrand == null) {
            return null;
        }

        List<CheapestBrand.CategoryPrice> categoryPriceList = cheapestClothInfoList.stream()
                .map(CheapestBrand.CategoryPrice::of)
                .toList();

        return new CheapestBrand(cheapestBrand, categoryPriceList, cheapestPrice);
    }

    @Override
    public ClothInfo getCheapestCloth(ClothType clothType) {
        SortedMap<ClothInfo, ClothInfo> cache = categorySaleCache.get(clothType);

        if (cache != null) {
            return cache.firstEntry().getKey();
        } else {
            return null;
        }
    }

    @Override
    public ClothInfo getMostExpensiveCloth(ClothType clothType) {
        SortedMap<ClothInfo, ClothInfo> cache = categorySaleCache.get(clothType);

        if (cache != null) {
            return cache.lastEntry().getKey();
        } else {
            return null;
        }
    }

    private void setBrandCache(Integer brandId, ClothInfo clothInfo) {
        Map<ClothType, SortedMap<ClothInfo, ClothInfo>> categoryMap = brandSaleCache.computeIfAbsent(brandId, k -> new HashMap<>());
        SortedMap<ClothInfo, ClothInfo> productMap = categoryMap.computeIfAbsent(clothInfo.getCategory(), k -> new TreeMap<>());
        productMap.put(clothInfo, clothInfo);
    }

    private void setCategoryCache(ClothType category, ClothInfo clothInfo) {
        SortedMap<ClothInfo, ClothInfo> categoryMap = categorySaleCache.computeIfAbsent(category, k -> new TreeMap<>());
        categoryMap.put(clothInfo, clothInfo);
    }
}
