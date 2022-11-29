package com.odc19.billing.controller;

import com.odc19.billing.paypal.PaypalClient;
import com.odc19.clients.billing.BillDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/paypal")
public class PayPalController {

    @Autowired
    PaypalClient paypalClient;

    @PostMapping(value = "/make/payment")
    public Map<String, Object> makePayment(@RequestParam("sum") String sum){
        return paypalClient.createPayment(sum);
    }

    @PostMapping(value = "/complete/payment")
    public Map<String, Object> completePayment(HttpServletRequest request, @RequestBody BillDTO billDTO){
        return paypalClient.completePayment(request, billDTO);
    }
}
