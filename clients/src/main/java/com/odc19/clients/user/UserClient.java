package com.odc19.clients.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("user")
public interface UserClient {
    @GetMapping("/api/v1/user/{id}")
    UserDTO getUserById(@PathVariable(name = "id") String id);

    @GetMapping("/api/v1/user/list/shipper")
    List<UserDTO> getShipperList();
}
