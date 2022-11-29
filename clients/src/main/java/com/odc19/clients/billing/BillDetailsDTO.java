package com.odc19.clients.billing;

import com.odc19.clients.medicalShop.GoodsDTO;
import com.odc19.clients.medicalShop.MedicalShopDTO;
import com.odc19.clients.user.UserDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BillDetailsDTO {
    private String billId;

    private UserDTO customerDTO;

    private MedicalShopDTO medicalShopDTO;

    private List<GoodsDTO> goodsDTOS;

    private Double totalPrice;

    private String deliverToAddress;

    private UserDTO shipperDTO;

    private boolean isDelivered;

    private String paymentMethod;

    private String description;

    private String note;

    private boolean isPaymentConfirmed;
}
