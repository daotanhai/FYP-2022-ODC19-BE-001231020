package com.odc19.fraud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class FraudCheckService {
    @Autowired
    FraudCheckHistoryRepository fraudCheckHistoryRepository;

    public boolean isFraudulentCustomer(Integer customerId) {
        FraudCheckHistoryEntity fraudCheckHistoryEntity = new FraudCheckHistoryEntity();
        fraudCheckHistoryEntity.setCustomerId(customerId);
        fraudCheckHistoryEntity.setFraudster(false);
        fraudCheckHistoryEntity.setCreatedAt(LocalDateTime.now());
        fraudCheckHistoryRepository.save(fraudCheckHistoryEntity);
        return false;
    }
}
