package com.odc19.billing.entity;

import com.odc19.baseEntity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "billing")
@Getter
@Setter
public class BillEntity extends BaseEntity {
    @Id
    @GenericGenerator(name = "sequence_bill_id",
            parameters = @Parameter(name = "prefix", value = "bill"),
            strategy = "com.odc19.baseEntity.CustomIdGenerator.CustomIdGenerator")
    @GeneratedValue(generator = "sequence_bill_id")
    @Column(name = "bill_id")
    private String billId;

    private String customerId;

    private String medicalShopId;

    private String goodsIdStringList;

    private Double totalPrice;

    private String deliverToAddress;

    private String shipperId;

    private boolean isDelivered;

    private String paymentMethod;

    private boolean isPaymentConfirmed;

    private String description;

    private String note;

}
