package com.odc19.fraud;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FraudCheckHistoryRepository extends JpaRepository<FraudCheckHistoryEntity, Integer> {
}
