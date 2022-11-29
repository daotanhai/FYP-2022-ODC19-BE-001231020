package com.odc19.location.service.impl;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.odc19.clients.location.LocationDTO;
import com.odc19.location.mapper.LocationMapper;
import com.odc19.location.repository.readRepository.LocationReadRepository;
import com.odc19.location.repository.writeRepository.LocationWriteRepository;
import com.odc19.location.service.LocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    LocationReadRepository locationReadRepository;

    @Autowired
    LocationWriteRepository locationWriteRepository;

    @Autowired
    LocationMapper locationMapper;

    @Override
    public LocationDTO getUserLocationBasedOnIpAddress(LocationDTO locationDTO) throws IOException, GeoIp2Exception {
        String ip = locationDTO.getIpAddress();
        String dbLocation = "C:\\microservice learning\\GeoLite2 DB\\GeoLite2-City_20220603\\GeoLite2-City.mmdb";

        File database = new File(dbLocation);
        DatabaseReader databaseReader = new DatabaseReader.Builder(database).build();

        InetAddress inetAddress = InetAddress.getByName(ip);
        CityResponse response = databaseReader.city(inetAddress);

        LocationDTO location = new LocationDTO();
        location.setIpAddress(ip);
        location.setCity(response.getCity().getName());
        location.setCountry(response.getCountry().getName());
        location.setLatitude(response.getLocation().getLatitude());
        log.info("Latitude: " + response.getLocation().getLatitude());
        location.setLongitude(response.getLocation().getLongitude());
        log.info("Longitude: " + response.getLocation().getLongitude());
        location.setState(response.getLeastSpecificSubdivision().getName());
        location.setPostal(response.getPostal().getCode());

        return location;
    }

    @Override
    public void saveNewLocation(LocationDTO locationDTO) {
        locationWriteRepository.save(locationMapper.dtoToEntity(locationDTO));
    }

    @Override
    public List<LocationDTO> getAllLocation() {
        return locationWriteRepository.findAll().stream()
                .map(locationEntity -> locationMapper.entityToDto(locationEntity))
                .collect(Collectors.toList());
    }
}
