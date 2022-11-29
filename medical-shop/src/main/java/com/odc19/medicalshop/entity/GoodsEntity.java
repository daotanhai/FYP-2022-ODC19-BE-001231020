package com.odc19.medicalshop.entity;

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
@Table(name = "goods")
@Getter
@Setter
public class GoodsEntity extends BaseEntity {
    @Id
    @GenericGenerator(name = "sequence_goods_id",
            parameters = @Parameter(name = "prefix", value = "goods"),
            strategy = "com.odc19.baseEntity.CustomIdGenerator.CustomIdGenerator")
    @GeneratedValue(generator = "sequence_goods_id")
    @Column(name = "goods_id")
    private String goodsId;

    private String goodsName;

    private String goodsUrlImage;

    private String quantity;

    private String price;

    @Column(name = "medical_shop_id",nullable = false)
    private String medicalShopId;
}
