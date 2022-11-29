package com.odc19.clients.location;

import com.odc19.clients.BaseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LocationDTO extends BaseDTO {
    private String locationId;
    private String ipAddress;
    private String userId;
    private String country;
    private String city;
    private String state;
    private String postal;
    private Double latitude;
    private Double longitude;
    private String detailsAddress;
}
