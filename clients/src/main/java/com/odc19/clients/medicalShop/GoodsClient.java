package com.odc19.clients.medicalShop;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("goods")
public interface GoodsClient {
    @GetMapping("/api/v1/medical-shop/goods/{goodsId}")
    GoodsDTO getGoodsById(@PathVariable(name = "goodsId") String goodsId);
}
