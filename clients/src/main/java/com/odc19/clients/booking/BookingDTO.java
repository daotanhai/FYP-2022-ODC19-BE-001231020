package com.odc19.clients.booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingDTO {
    private String bookingId;

    private boolean isConfirmed;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm")
    private Date startDate;

    private String customerId;

    private String medicalShopId;

    private String description;

    private String purpose;
}
