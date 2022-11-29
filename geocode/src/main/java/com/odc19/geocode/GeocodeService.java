package com.odc19.geocode;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.odc19.clients.medicalShop.MedicalShopDTO;
import com.odc19.clients.user.UserClient;
import com.odc19.clients.user.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class GeocodeService {

    @Autowired
    UserClient userClient;

    public static final double R = 6371;
    private GeoApiContext geoApiContext;
    private static final String apiKey = "AIzaSyBwxHyA25RlKXpfM7jLjoPG4x2oyLPlCeU";

    /*
     * Init context with api key get from application.properties through bootstrap class
     * */
    private void initGeoApiContext() {
        geoApiContext = new GeoApiContext();
        geoApiContext.setApiKey(apiKey);
    }

    /**
     * @param geocodeDTO contains shopName ( not required) and details address ( required)
     * @return latLng
     */
    public LatLng getLatitudeLongitudeBasedOnDetailsAddress(GeocodeDTO geocodeDTO) {
        initGeoApiContext();
        GeocodingResult[] geocodingResult;
        LatLng latLng = null;
        try {
            geocodingResult = GeocodingApi.geocode(geoApiContext, formatDetailsAddress(geocodeDTO)).await();
            latLng = geocodingResult[0].geometry.location;
        } catch (Exception e) {
            log.info("Exception in get geocode: ", e.getCause());
        }
        return latLng;
    }

    public String reverseGeocodeByLatLong(LatLng latLng) {
        initGeoApiContext();
        GeocodingResult[] geocodingResult;
        try {
            geocodingResult = GeocodingApi.reverseGeocode(geoApiContext, latLng).await();
            return geocodingResult[0].addressComponents[geocodingResult[0].addressComponents.length-1].longName;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<MedicalShopDTO> findNearestShopBasedOnUserLatitudeAndLongitude(LatLng latLng, List<MedicalShopDTO> medicalShopDTOList) {
//        user lat and long
        double userLat = latLng.lat;
        double userLong = latLng.lng;
        double nearestDistance = -100;
        MedicalShopDTO nearestMedicalShopDTO = new MedicalShopDTO();
        List<MedicalShopDTO> nearestMedicalShopList = new ArrayList<>();
        for (MedicalShopDTO dto : medicalShopDTOList) {
            double shopLat = dto.getLatitude();
            double shopLong = dto.getLongitude();
            double distanceUserToShop = haversineFormula(userLat, userLong, shopLat, shopLong);
            if (distanceUserToShop < nearestDistance || nearestDistance == -100) {
                nearestMedicalShopDTO = dto;
                nearestDistance = distanceUserToShop;
                nearestMedicalShopList.add(nearestMedicalShopDTO);
            }
        }
        log.info("found: " + nearestDistance + " KM");
        return nearestMedicalShopList;
    }

    public UserDTO findNearestShipperBasedOnMedicalShopLatitudeAndLongitude(LatLng latLng, List<UserDTO> shipperList) {
        double shopLat = latLng.lat;
        double shopLong = latLng.lat;
        double nearestDistance = -100;
        UserDTO nearestShipperToShop = new UserDTO();
        shipperList = shipperList.stream()
                .filter(userDTO -> userDTO.getLatitude() !=null)
                .filter(userDTO -> userDTO.getLongitude() != null)
                .collect(Collectors.toList());
        for (UserDTO dto : shipperList) {
            double shipperLat = dto.getLatitude();
            double shipperLong = dto.getLongitude();
            double distanceShipperToShop = haversineFormula(shopLat, shopLong, shipperLat, shipperLong);
            if (distanceShipperToShop < nearestDistance || nearestDistance == -100) {
                nearestShipperToShop = dto;
                nearestDistance = distanceShipperToShop;
            }
        }
        log.info("found: " + nearestDistance + " KM");
        return nearestShipperToShop;
    }



    /**
     * @param geocodeDTO to get all variables
     * @return a formatted address
     */
    private String formatDetailsAddress(GeocodeDTO geocodeDTO) {
        StringBuilder formattedAddress = new StringBuilder();
        if (Objects.nonNull(geocodeDTO.getShopName())) {
            formattedAddress.append(geocodeDTO.getShopName()).append(",");
        }
        if (Objects.nonNull(geocodeDTO.getStreetNumber())) {
            formattedAddress.append(geocodeDTO.getStreetNumber()).append(",");
        }
        if (Objects.nonNull(geocodeDTO.getDetailAddress())) {
            formattedAddress.append(geocodeDTO.getDetailAddress()).append(",");
        }
        if (Objects.nonNull(geocodeDTO.getPostCode())) {
            formattedAddress.append(geocodeDTO.getPostCode());
        }
        log.info("The formatted address need to find latitude and longitude: {}", formattedAddress.toString());
        return formattedAddress.toString();
    }

    /**
     * @param userLatitude  as user's latitude
     * @param userLongitude as user's longitude
     * @param shopLatitude  as shop's latitude
     * @param shopLongitude as shop's longitude
     * @return distance between two points in km(s)
     */
    private double haversineFormula(double userLatitude, double userLongitude, double shopLatitude, double shopLongitude) {
        double distanceUserToShopLatitude = Math.toRadians(shopLatitude - userLatitude);
        double distanceUserToShopLongitude = Math.toRadians(shopLongitude - userLongitude);
        userLatitude = Math.toRadians(userLatitude);
        shopLatitude = Math.toRadians(shopLatitude);
        double a = Math.pow(Math.sin(distanceUserToShopLatitude / 2), 2) + Math.pow(Math.sin(distanceUserToShopLongitude / 2), 2) * Math.cos(userLatitude) * Math.cos(shopLatitude);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
