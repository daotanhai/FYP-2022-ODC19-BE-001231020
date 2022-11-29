package com.odc19.rating.repository.readRepository;

import com.odc19.rating.entity.RatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReadRatingRepository extends JpaRepository<RatingEntity, String> {
    List<RatingEntity> findRatingEntitiesByMedicalShopId(String medicalShopId);
    List<RatingEntity> findRatingEntitiesByShipperId(String shipperId);
}
