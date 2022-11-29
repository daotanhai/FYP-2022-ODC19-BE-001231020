package com.odc19.clients.medicalShop;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("medical-shop")
public interface MedicalShopClient {
    @GetMapping("/api/v1/medical-shop/list/{postCode}")
    List<MedicalShopDTO> getAllMedicalShopInPostCode(@PathVariable("postCode") String postCode);

    @GetMapping("/api/v1/medical-shop/{medicalShopId}")
    MedicalShopDTO getMedicalShopById(@PathVariable("medicalShopId") String medicalShopId);

    @GetMapping("/list/{goodsIdList}")
    List<GoodsDTO> getGoodsByIdString(@PathVariable("goodsIdList") String goodsIdList);

    @GetMapping("/api/v1/medical-shop/goods/{goodsId}")
    GoodsDTO getGoodsById(@PathVariable("goodsId") String goodsId);
}
