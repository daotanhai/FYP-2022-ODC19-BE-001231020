package com.odc19.clients.location;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("location")
public interface LocationClients {
    @GetMapping("/api/v1/location/current")
    LocationDTO getCurrentLocationBasedOnIP(@RequestBody LocationDTO locationDTO);
}
