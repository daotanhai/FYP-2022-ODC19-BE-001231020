package com.odc19.clients.billing;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillDTO {
    private String billId;

    private String customerId;

    private String medicalShopId;

    private String goodsIdStringList;

    private Double totalPrice;

    private String deliverToAddress;

    private String shipperId;

    private boolean isDelivered;

    private String paymentMethod;

    private String description;

    private String note;

    private boolean isPaymentConfirmed;
}
