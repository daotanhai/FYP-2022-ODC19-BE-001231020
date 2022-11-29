package com.odc19.clients.medicalShop;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoodsDTO {
    private String goodsId;

    private String medicalShopId;

    private String goodsName;

    private String goodsUrlImage;

    private String quantity;

    private String price;
}
