package com.odc19.customer;

import com.odc19.clients.fraud.FraudClient;
import com.odc19.clients.notification.NotificationClient;
import com.odc19.clients.notification.NotificationDTO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    FraudClient fraudClient;

    @Autowired
    NotificationClient notificationClient;

    public void saveCustomer(CustomerDTO customerDTO) {
        CustomerEntity customer = new CustomerEntity();
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customerRepository.saveAndFlush(customer);
//        fraudClient has URL API + method which customer service need to do business
//        if this customer service need a fraud DTO, then in fraudClient should have a fraudDTO
        if (fraudClient.isFraudster(customer.getId())) {
            throw new IllegalStateException("fraudster");
        }
        if (!fraudClient.isFraudster(customer.getId())) {
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setSender(customer.getFirstName() + customer.getLastName());
//            notificationDTO.setToCustomerId(customer.getId());
            notificationDTO.setToCustomerEmail(customer.getFirstName() + "@email.com");
            notificationDTO.setMessage("What's up?");
            notificationClient.saveNotification(notificationDTO);
        }
    }
}
