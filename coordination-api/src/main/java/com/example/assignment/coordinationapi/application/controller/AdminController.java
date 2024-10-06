package com.example.assignment.coordinationapi.application.controller;

import com.example.assignment.common.model.entity.Brand;
import com.example.assignment.common.model.entity.SaleCloth;
import com.example.assignment.common.model.enums.ClothType;
import com.example.assignment.coordinationapi.application.model.ResponseBase;
import com.example.assignment.coordinationapi.application.model.admin.*;
import com.example.assignment.coordinationapi.application.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 브랜드 및 상품을 관리하기 위한 controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin")
public class AdminController {

    private final AdminService adminService;

    /**
     * 브랜드를 추가한다.
     * @param createBrand
     * @return
     */
    @PostMapping("/brand/add")
    public ResponseBase<Brand> addBrand(@Valid @RequestBody CreateBrand createBrand) {
        return ResponseBase.success(
                this.adminService.addBrand(createBrand.getName()));
    }

    /**
     * 브랜드 정보를 수정한다.
     * 현재는 이름만 가능하다.
     * @param modifyBrand
     * @return
     */
    @PutMapping("/brand/modify")
    public ResponseBase<Brand> modifyBrand(@Valid @RequestBody ModifyBrand modifyBrand) {
        return ResponseBase.success(
                this.adminService.modifyBrand(modifyBrand.getOldBrandName(), modifyBrand.getNewBrandName()));
    }

    /**
     * 브랜드를 제거한다.
     * 브랜드에서 판매중인 상품이 있다면 제거가 불가능하다.
     * @param brandName
     * @return
     */
    @DeleteMapping("/brand/remove")
    public ResponseBase<Void> removeBrand(@Valid @RequestParam String brandName) {
        this.adminService.removeBrand(brandName);
        return ResponseBase.success();
    }

    /**
     * 상품을 추가한다.
     * 브랜드명, 타입, 상품명, 가격 정보가 필요하다.
     * @param createSaleCloth
     * @return
     */
    @PostMapping("/cloth/add")
    public ResponseBase<SaleCloth> addCloth(@Valid @RequestBody CreateSaleCloth createSaleCloth) {
        return ResponseBase.success(
                this.adminService.addCloth(
                        createSaleCloth.getBrandName(), createSaleCloth.getClothType(), createSaleCloth.getClothName(), createSaleCloth.getPrice()));
    }

    /**
     * 상품명을 수정한다.
     * 입력한 브랜드명, 타입, 상품명에 해당하는 상품명을 수정한다.
     * 변경하려는 상품명이 있을 경우 수정할 수 없다.
     * @param modifySaleClothName
     * @return
     */
    @PatchMapping("/cloth/modify/name")
    public ResponseBase<SaleCloth> modifyClothName(@Valid @RequestBody ModifySaleClothName modifySaleClothName) {
        return ResponseBase.success(
                this.adminService.modifyClothName(
                        modifySaleClothName.getBrandName(), modifySaleClothName.getClothType(), modifySaleClothName.getOldClothName(), modifySaleClothName.getNewClothName()));
    }

    /**
     * 상품 가격을 수정한다.
     * 입력한 브랜드명, 타입, 상품명에 해당하는 상품 가격을 수정한다.
     * @param modifySaleClothPrice
     * @return
     */
    @PatchMapping("/cloth/modify/price")
    public ResponseBase<SaleCloth> modifyClothPrice(@Valid @RequestBody ModifySaleClothPrice modifySaleClothPrice) {
        return ResponseBase.success(
                this.adminService.modifyClothPrice(
                        modifySaleClothPrice.getBrandName(), modifySaleClothPrice.getClothType(), modifySaleClothPrice.getClothName(), modifySaleClothPrice.getPrice()));
    }

    /**
     * 입력한 브랜드명, 타입, 상품명에 해당하는 판매중인 상품 정보를 삭제한다.
     * @param brandName
     * @param clothType
     * @param clothName
     * @return
     */
    @DeleteMapping("/cloth/remove")
    public ResponseBase<Void> removeCloth(@Valid @RequestParam String brandName,
                                          ClothType clothType,
                                          String clothName) {
        this.adminService.removeCloth(brandName, clothType, clothName);
        return ResponseBase.success();
    }

}
