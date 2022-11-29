package com.odc19.medicalshop.repository.writeRepository;

import com.odc19.medicalshop.entity.MedicalShopEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalShopWriteRepository extends JpaRepository<MedicalShopEntity, String> {
}
