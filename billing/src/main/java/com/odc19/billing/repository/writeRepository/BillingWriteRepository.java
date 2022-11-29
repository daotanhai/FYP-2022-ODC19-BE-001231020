package com.odc19.billing.repository.writeRepository;

import com.odc19.billing.entity.BillEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillingWriteRepository extends JpaRepository<BillEntity, String> {
}
