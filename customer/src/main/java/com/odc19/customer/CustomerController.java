package com.odc19.customer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @GetMapping(value = "/list")
    public CustomerDTO getCustomer() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(1);
        customerDTO.setFirstName("Hai");
        customerDTO.setLastName("Dao");
        return customerDTO;
    }

    @PostMapping(value = "/new")
    public void saveCustomer(@RequestBody CustomerDTO customerDTO) {
        customerService.saveCustomer(customerDTO);
    }
}
