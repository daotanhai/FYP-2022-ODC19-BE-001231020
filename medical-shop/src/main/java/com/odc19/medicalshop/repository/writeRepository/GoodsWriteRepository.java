package com.odc19.medicalshop.repository.writeRepository;

import com.odc19.medicalshop.entity.GoodsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsWriteRepository extends JpaRepository<GoodsEntity, String> {
}
