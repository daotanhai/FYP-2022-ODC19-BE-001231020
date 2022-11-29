package com.odc19.clients.rating;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingDTO {
    private String ratingId;

    private Integer numberOfStartOnMedicalShop;

    private Integer numberOfStartOnShipper;

    private String feedbackPictureUrl;

    private String commentOnMedicalShop;

    private String commentOnShipper;

    private String customerId;

    private String medicalShopId;

    private String shipperId;

    private String billId;

    private String bookingId;
}
