package com.odc19.billing.repository.readRepository;

import com.odc19.billing.entity.BillEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillingReadRepository extends JpaRepository<BillEntity, String> {
    List<BillEntity> findBillEntitiesByShipperId(String shipperId);
}
