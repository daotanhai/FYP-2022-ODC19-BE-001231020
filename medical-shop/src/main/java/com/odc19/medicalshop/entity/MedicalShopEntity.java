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
import java.util.List;

@Entity
@Table(name = "medicalshop")
@Getter
@Setter
public class MedicalShopEntity extends BaseEntity {
    @Id
    @GenericGenerator(name = "sequence_medical_shop_id",
            parameters = @Parameter(name = "prefix", value = "medical_shop"),
            strategy = "com.odc19.baseEntity.CustomIdGenerator.CustomIdGenerator")
    @GeneratedValue(generator = "sequence_medical_shop_id")
    @Column(name = "medical_shop_id")
    private String medicalShopId;

    @Column(name = "ownerId")
    private String userId;

    private String medicalShopName;

    //    @Column(unique=true)
    private Double latitude;

    //    @Column(unique=true)
    private Double longitude;

    private String streetNumber;

    private String postCode;

    private String detailAddress;

    private String medicalShopUrlImage;

    public String getMedicalShopId() {
        return medicalShopId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMedicalShopName() {
        return medicalShopName;
    }

    public void setMedicalShopName(String medicalShopName) {
        this.medicalShopName = medicalShopName;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }
}
