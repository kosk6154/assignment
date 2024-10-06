package com.example.assignment.coordinationapi.application.controller;

import com.example.assignment.common.model.enums.ClothType;
import com.example.assignment.coordinationapi.application.model.ResponseBase;
import com.example.assignment.coordinationapi.application.model.coordination.CheapestBrand;
import com.example.assignment.coordinationapi.application.model.coordination.ClothInfoWithName;
import com.example.assignment.coordinationapi.application.model.coordination.MinMaxPriceCategory;
import com.example.assignment.coordinationapi.application.service.SaleClothService;
import com.example.assignment.coordinationapi.configuration.AssignmentData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 서비스에서 사용하기 위한 controller
 * endpoint가 많지 않기 때문에 한곳에서 관리한다.
 *
 * @author kokyeomjae
 * @version 0.0.1
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/coordination")
public class SaleController {

    private final SaleClothService saleClothService;

    /**
     * 카테고리별로 가장 저렴한 상품을 조회하여 반환한다.
     * @return
     */
    @GetMapping("/category/cheapest")
    public ResponseBase<List<ClothInfoWithName>> getCheapestClothPerCategory() {
        return ResponseBase.success(
                AssignmentData.REQUIRED_CATEGORY.stream()
                        .map(this.saleClothService::getCheapestCloth)
                        .toList());
    }

    /**
     * 어떤 브랜드가 해당 카테고리들의 상품을 다 구매했을 때 가장 저렴한지 브랜드 정보와 상품정보를 반환한다.
     * 특정 브랜드에 요구하는 카테고리 판매상품이 없을 경우 해당 브랜드는 제외된다.
     * @return
     */
    @GetMapping("/brand/cheapest")
    public ResponseBase<CheapestBrand> getCheapestClothPerBrand() {
        return ResponseBase.success(
                this.saleClothService.getCheapestBrand(AssignmentData.REQUIRED_CATEGORY));
    }

    /**
     * 카테고리별로 가장 저렴한, 가장 비싼 상품정보와 브랜드 정보를 반환한다.
     * @param category
     * @return
     */
    @GetMapping("/category/minmax")
    public ResponseBase<MinMaxPriceCategory> getMinMaxPriceCategory(@RequestParam ClothType category) {
        return ResponseBase.success(
                this.saleClothService.getMinMaxCloth(category));
    }

}
