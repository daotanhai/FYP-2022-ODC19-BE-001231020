package com.odc19.booking.entity;

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
import java.util.Date;

@Entity
@Table(name = "booking")
@Getter
@Setter
public class BookingEntity extends BaseEntity {
    @Id
    @GenericGenerator(name = "sequence_booking_id",
            parameters = @Parameter(name = "prefix", value = "booking"),
            strategy = "com.odc19.baseEntity.CustomIdGenerator.CustomIdGenerator")
    @GeneratedValue(generator = "sequence_booking_id")
    @Column(name = "booking_id")
    private String bookingId;

    private boolean isConfirmed;

    private Date startDate;

    private String customerId;

    private String medicalShopId;

    private String description;

    private String purpose;
}
