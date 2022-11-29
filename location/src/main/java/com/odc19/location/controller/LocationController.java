package com.odc19.location.controller;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.odc19.clients.location.LocationDTO;
import com.odc19.location.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController()
@RequestMapping("/api/v1/location")
public class LocationController {
    @Autowired
    LocationService locationService;

    @GetMapping("/current")
    public LocationDTO getCurrentLocationBasedOnIP(@RequestBody LocationDTO locationDTO) throws IOException, GeoIp2Exception {
        return locationService.getUserLocationBasedOnIpAddress(locationDTO);
    }

    @PostMapping("/new")
    public void saveNewLocation(@RequestBody LocationDTO locationDTO) {
        locationService.saveNewLocation(locationDTO);
    }

    @GetMapping("/list")
    public List<LocationDTO> listAllLocation() {
        return locationService.getAllLocation();
    }
}
