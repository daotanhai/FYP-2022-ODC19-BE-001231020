package com.odc19.rating.entity;

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
@Table(name = "rating")
@Getter
@Setter
public class RatingEntity extends BaseEntity {
    @Id
    @GenericGenerator(name = "sequence_rating_id",
            parameters = @Parameter(name = "prefix", value = "rating"),
            strategy = "com.odc19.baseEntity.CustomIdGenerator.CustomIdGenerator")
    @GeneratedValue(generator = "sequence_rating_id")
    @Column(name = "rating_id")
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
