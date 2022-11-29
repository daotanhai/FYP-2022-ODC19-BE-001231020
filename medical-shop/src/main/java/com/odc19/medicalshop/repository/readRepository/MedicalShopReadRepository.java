package com.odc19.medicalshop.repository.readRepository;

import com.odc19.medicalshop.entity.MedicalShopEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalShopReadRepository extends JpaRepository<MedicalShopEntity, String> {
    List<MedicalShopEntity> findMedicalShopEntitiesByPostCode(String postCode);
    List<MedicalShopEntity> findMedicalShopEntitiesByUserId(String userId);
    @Query(value = "SELECT * FROM medicalshop  " +
            " FULL JOIN goods g on medicalshop.medical_shop_id = g.medical_shop_id " +
            " WHERE medicalshop.medical_shop_id =:medicalShopId", nativeQuery = true)
    MedicalShopEntity getMedicalShopAndGoods(@Param("medicalShopId") String medicalShopId);

    List<MedicalShopEntity> findByMedicalShopNameStartingWith(String searchParam);

    @Query(value = "SELECT * FROM medicalshop WHERE medical_shop_id =:medicalShopId", nativeQuery = true)
    MedicalShopEntity findMedicalShopByIdCustom(@Param("medicalShopId") String medicalShopId);

}
