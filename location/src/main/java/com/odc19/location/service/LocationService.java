package com.odc19.location.service;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.odc19.clients.location.LocationDTO;

import java.io.IOException;
import java.util.List;

public interface LocationService {
    LocationDTO getUserLocationBasedOnIpAddress(LocationDTO locationDTO) throws IOException, GeoIp2Exception;

    void saveNewLocation(LocationDTO locationDTO);

    List<LocationDTO> getAllLocation();
}
