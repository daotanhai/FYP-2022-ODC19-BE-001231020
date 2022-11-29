package com.odc19.location.entity;

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

@Table(name = "location")
@Entity
@Getter
@Setter
public class LocationEntity extends BaseEntity {
    @Id
    @GenericGenerator(name = "sequence_location_id",
            parameters = @Parameter(name = "prefix", value = "location"),
            strategy = "com.odc19.baseEntity.CustomIdGenerator.CustomIdGenerator")
    @GeneratedValue(generator = "sequence_location_id")
    @Column(name = "location_id")
    private String locationId;
    private String userId;
    private String country;
    private String ipAddress;
    private String detailsAddress;
    private String city;
    private String state;
    private String postal;
    private Double latitude;
    private Double longitude;
}
