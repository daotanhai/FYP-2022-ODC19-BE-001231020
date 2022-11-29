package com.odc19.clients.billing;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("bill")
public interface BillClient {
    @PutMapping("/api/v1/bill/update")
    BillDTO updateBill(@RequestBody BillDTO billDTO);
}
