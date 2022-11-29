package com.odc19.fraud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/fraud-check")
public class FraudController {

    @Autowired
    FraudCheckService fraudCheckService;

    @GetMapping(value = "/{customerId}")
    public boolean isFraudster(
            @PathVariable(name = "customerId") Integer customerId) {
        log.info("successfully save new customer {}", customerId);
        return fraudCheckService.isFraudulentCustomer(customerId);
    }

}
