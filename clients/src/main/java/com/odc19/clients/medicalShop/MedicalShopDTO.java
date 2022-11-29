package com.odc19.clients.medicalShop;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MedicalShopDTO {
    private String medicalShopId;

    private List<GoodsDTO> goodsDTOS;

    private String userId;

    private String medicalShopName;

    private Double latitude;

    private Double longitude;

    private String medicalShopUrlImage;

    private String streetNumber;

    private String postCode;

    private String detailAddress;
}
